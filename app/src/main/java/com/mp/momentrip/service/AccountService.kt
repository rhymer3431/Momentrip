package com.mp.momentrip.service


import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.user.User

import com.mp.momentrip.data.user.UserRegisterForm
import com.mp.momentrip.data.user.dto.UserDto
import com.mp.momentrip.ui.MainDestinations
import kotlinx.coroutines.tasks.await

object AccountService{
    suspend fun signUp(userRegisterForm: UserRegisterForm): Result<Unit> {
        return try {
            val auth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()

            // 1. Firebase 인증
            val authResult = auth.createUserWithEmailAndPassword(
                userRegisterForm.email,
                userRegisterForm.password
            ).await()

            // 2. Firestore에 사용자 정보 저장
            val userId = authResult.user?.uid ?: throw Exception("User ID is null")
            val user = hashMapOf(
                "id" to userId,
                "email" to userRegisterForm.email,
                "name" to userRegisterForm.name,
                "gender" to userRegisterForm.gender,
                "age" to userRegisterForm.age,
                "userVector" to emptyList<Float>(),                        // 초기값 빈 벡터
                "foodPreference" to hashMapOf(                             // 구조 명시
                    "foodTypeId" to emptyMap<String, Int>(),
                    "foodNameId" to emptyMap<String, Int>()
                ),
                "liked" to emptyList<Any>(),
                "schedules" to emptyList<Any>()
            )

            db.collection("users").document(userId).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("Firebase", "SignUp failed", e)
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val auth = FirebaseAuth.getInstance()
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { Result.success(it) } ?: Result.failure(Exception("User is null"))

        } catch (e: Exception) {
            Log.e("Firebase", "SignIn failed", e)
            Result.failure(e)
        }
    }

    fun signOut(navController: NavController){
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        Log.d("test","in sign out")
        navController.navigate(MainDestinations.SIGN_IN_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true // 현재 그래프 전체 제거
            }
            launchSingleTop = true
            restoreState = false
        }
    }

    suspend fun loadUser(firebaseUser: FirebaseUser): User? {
        return try {
            val uid = firebaseUser.uid
            val db = FirebaseFirestore.getInstance()

            // await()를 사용해 동기적으로 처리
            val document = db.collection("users").document(uid).get().await()

            if (document.exists()) {
                val userDto = document.toObject(UserDto::class.java)
                val user = userDto?.toModel()


                user
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("Firebase", "Failed to fetch user info", e)
            null
        }
    }
    fun getCurrentUser() : FirebaseUser?{
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser
    }

    suspend fun updateUser(userDto: UserDto): Boolean {
        return try {
            val uid = getCurrentUser()?.uid ?: return false
            val db = FirebaseFirestore.getInstance()
            Log.d("Firebase", "updateUser: ${userDto.email}")
            db.collection("users").document(uid).set(userDto).await()
            true
        } catch (e: Exception) {
            Log.e("Firebase", "Update failed", e)
            false
        }
    }
    suspend fun refreshUser(): Result<User> {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
                ?: return Result.failure(Exception("로그인된 사용자가 없습니다."))

            // Firebase 인증 정보 새로고침
            currentUser.reload().await()

            // Firestore에서 사용자 정보 다시 로드
            val user = loadUser(currentUser)
                ?: return Result.failure(Exception("사용자 정보를 찾을 수 없습니다."))

            Result.success(user)
        } catch (e: Exception) {
            Log.e("Firebase", "사용자 새로고침 실패", e)
            Result.failure(e)
        }
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        return try {
            val user = FirebaseAuth.getInstance().currentUser
                ?: return Result.failure(Exception("로그인된 사용자가 없습니다."))

            val email = user.email
                ?: return Result.failure(Exception("이메일 정보를 찾을 수 없습니다."))

            // 1. 현재 비밀번호로 재인증
            val credential = EmailAuthProvider.getCredential(email, currentPassword)
            user.reauthenticate(credential).await()

            // 2. 새 비밀번호로 업데이트
            user.updatePassword(newPassword).await()

            Log.d("Firebase", "비밀번호 변경 성공")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("Firebase", "비밀번호 변경 실패", e)
            Result.failure(e)
        }
    }
    suspend fun fetchSimilarUsersPlaces(
        currentUserId: String,
        userVector: FloatArray,
        topN: Int = 5
    ): List<Place> {
        val db = FirebaseFirestore.getInstance()
        val usersSnapshot = db.collection("users").get().await()

        val similarities = usersSnapshot.documents
            .filter { it.id != currentUserId && it.contains("userVector") }
            .mapNotNull { doc ->
                val uid = doc.id
                val rawVector = doc["userVector"] as? List<*>
                Log.d("VectorDebug", "Checking $uid: rawVector=${rawVector?.size}")

                val otherVector = rawVector?.mapNotNull { it.toString().toFloatOrNull() }
                if (otherVector != null && otherVector.size == userVector.size) {
                    val similarity = RecommendService.cosineSimilarity(userVector.toList(), otherVector)
                    uid to similarity
                } else {
                    Log.w("VectorDebug", "Skipped $uid due to vector size mismatch or null")
                    null
                }
            }
            .sortedByDescending { it.second }
            .take(topN)

        Log.d("SimilarUsers", "Total scanned: ${usersSnapshot.size()}, TopN: ${similarities.size}")
        similarities.forEachIndexed { index, (uid, sim) ->
            Log.d("SimilarUsers", "[$index] $uid → similarity: $sim")
        }

        val similarUserIds = similarities.map { it.first }
        val placeSet = mutableSetOf<Place>()

        for (uid in similarUserIds) {
            Log.d("SimilarUsers", "Fetching schedule for user: $uid")
            val schedulesSnapshot = db.collection("users").document(uid).collection("schedules").get().await()

            for (scheduleDoc in schedulesSnapshot) {
                val days = scheduleDoc.get("days") as? List<Map<String, Any>> ?: continue
                for (day in days) {
                    val places = day["places"] as? List<Map<String, Any>> ?: continue
                    for (placeMap in places) {
                        val place = placeMap.toPlace()
                        if (place != null) {
                            Log.d("SimilarUsers", "Place added: ${place.title}")
                            placeSet.add(place)
                        } else {
                            Log.w("SimilarUsers", "Invalid place: $placeMap")
                        }
                    }
                }
            }
        }

        return placeSet.toList()
    }


    fun Map<String, Any>.toPlace(): Place? {
        val contentid = this["contentId"]?.toString() ?: return null
        val title = this["title"]?.toString() ?: return null
        val image = this["firstImage"]?.toString()
        return Place(contentId = contentid.toInt(), title = title, firstImage = image)
    }


}