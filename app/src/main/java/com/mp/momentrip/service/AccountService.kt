package com.mp.momentrip.service

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.User
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.data.UserRegisterForm

import com.mp.momentrip.util.MainDestinations
import com.mp.momentrip.view.UserViewModel
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
                val user = document.toObject(User::class.java)

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

    suspend fun updateUser(user: User): Boolean {
        return try {
            val uid = getCurrentUser()?.uid ?: return false
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(uid).set(user).await()
            true
        } catch (e: Exception) {
            Log.e("Firebase", "Update failed", e)
            false
        }
    }
    suspend fun saveSchedule(schedule: Schedule): String? {
        return try {
            val uid = getCurrentUser()?.uid ?: return null
            val db = FirebaseFirestore.getInstance()
            val document = db.collection("users")
                .document(uid)
                .collection("schedules")
                .add(schedule)
                .await()
            document.id
        } catch (e: Exception) {
            Log.e("Firebase", "Save schedule failed", e)
            null
        }
    }
    suspend fun loadSchedulesOfUser(): List<Schedule> {
        return try {
            val uid = getCurrentUser()?.uid ?: return emptyList()
            val db = FirebaseFirestore.getInstance()
            val result = db.collection("users")
                .document(uid)
                .collection("schedules")
                .get()
                .await()
            result.documents.mapNotNull { it.toObject(Schedule::class.java) }

        } catch (e: Exception) {
            Log.e("Firebase", "Load schedules failed", e)
            emptyList()
        }
    }





}