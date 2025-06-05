package com.mp.momentrip.view

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mp.momentrip.data.community.Post
import com.mp.momentrip.data.user.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


enum class PostSortOption {
    LATEST, MOST_LIKED
}

@HiltViewModel
class CommunityViewModel @Inject constructor() : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _postList = MutableStateFlow<List<Post>>(emptyList())
    val postList: StateFlow<List<Post>> = _postList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sortOption = MutableStateFlow(PostSortOption.LATEST)
    val sortOption: StateFlow<PostSortOption> = _sortOption

    private val _likedPostIds = MutableStateFlow<Set<String>>(emptySet())
    val likedPostIds: StateFlow<Set<String>> = _likedPostIds

    fun setSortOption(option: PostSortOption) {
        _sortOption.value = option
        refreshPosts()
    }

    fun refreshPosts() {
        _isRefreshing.value = true
        val query = when (_sortOption.value) {
            PostSortOption.LATEST ->
                firestore.collection("posts").orderBy("createdAt", Query.Direction.DESCENDING)
            PostSortOption.MOST_LIKED ->
                firestore.collection("posts").orderBy("likeCount", Query.Direction.DESCENDING)
        }

        query.get()
            .addOnSuccessListener { snapshot ->
                val posts = snapshot.documents.mapNotNull {
                    it.toObject(com.mp.momentrip.data.community.dto.PostDto::class.java)?.toModel()
                }
                _postList.value = posts
                _isRefreshing.value = false
                loadLikedPostIds()
            }
            .addOnFailureListener { e ->
                Log.e("CommunityVM", "불러오기 실패", e)
                _isRefreshing.value = false
            }
    }

    fun uploadPost(post: Post, user: UserDto) {
        val uid = auth.currentUser?.uid ?: return
        val postId = if (post.id.isNotBlank()) post.id else firestore.collection("posts").document().id
        val finalPost = post.copy(
            id = postId,
            author = user.toModel(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        firestore.collection("posts").document(postId)
            .set(finalPost.toDto()) // ✅ DTO 변환 후 저장
            .addOnSuccessListener { refreshPosts() }
            .addOnFailureListener { e -> Log.e("CommunityVM", "업로드 실패", e) }
    }

    fun updatePost(post: Post) {
        if (post.id.isBlank()) return
        val updated = post.copy(updatedAt = System.currentTimeMillis())

        firestore.collection("posts").document(post.id)
            .set(updated.toDto()) // ✅ DTO 저장
            .addOnSuccessListener { refreshPosts() }
            .addOnFailureListener { e -> Log.e("CommunityVM", "수정 실패", e) }
    }

    fun deletePost(postId: String) {
        firestore.collection("posts").document(postId)
            .delete()
            .addOnSuccessListener { refreshPosts() }
            .addOnFailureListener { e -> Log.e("CommunityVM", "삭제 실패", e) }
    }

    private fun loadLikedPostIds() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("posts").get()
            .addOnSuccessListener { snapshot ->
                val liked = mutableSetOf<String>()
                snapshot.documents.forEach { doc ->
                    val postId = doc.id
                    doc.reference.collection("likes").document(uid)
                        .get()
                        .addOnSuccessListener { likeDoc ->
                            if (likeDoc.exists()) {
                                liked.add(postId)
                                _likedPostIds.value = liked
                            }
                        }
                }
            }
    }

    fun refreshLikedPosts(userId: String) {
        firestore.collection("posts").get().addOnSuccessListener { snapshot ->
            val liked = mutableSetOf<String>()
            snapshot.documents.forEach { doc ->
                val postId = doc.id
                doc.reference.collection("likes").document(userId).get()
                    .addOnSuccessListener { likeDoc ->
                        if (likeDoc.exists()) {
                            liked.add(postId)
                            _likedPostIds.value = liked
                        }
                    }
            }
        }
    }

    fun toggleLike(postId: String, userId: String) {
        val isLiked = _likedPostIds.value.contains(postId)
        val postRef = firestore.collection("posts").document(postId)
        val likeRef = postRef.collection("likes").document(userId)

        firestore.runBatch { batch ->
            if (isLiked) {
                batch.update(postRef, "likeCount", FieldValue.increment(-1))
                batch.delete(likeRef)
            } else {
                batch.update(postRef, "likeCount", FieldValue.increment(1))
                batch.set(likeRef, mapOf("liked" to true))
            }
        }.addOnSuccessListener {
            refreshLikedPosts(userId)
        }
    }

    suspend fun checkIfUserLikedPost(
        postId: String,
        userId: String
    ): Boolean {
        return try {
            val snapshot = firestore
                .collection("posts")
                .document(postId)
                .collection("likes")
                .document(userId)
                .get()
                .await()
            snapshot.exists()
        } catch (e: Exception) {
            Log.e("PostLike", "좋아요 확인 실패", e)
            false
        }
    }
}
