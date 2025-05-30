package com.mp.momentrip.service

import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.User
import com.mp.momentrip.data.UserDto
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.data.UserRegisterForm
import com.mp.momentrip.data.toModel
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
                "email" to userRegisterForm.email,
                "name" to userRegisterForm.name,
                "gender" to userRegisterForm.gender,
                "age" to userRegisterForm.age,
                "userPreference" to UserPreference(),
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
        navController.navigate(MainDestinations.SIGN_IN_ROUTE)
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

}