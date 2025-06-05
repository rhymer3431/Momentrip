package com.mp.momentrip.view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class AuthViewModel : ViewModel() {
    private val auth = Firebase.auth

    sealed class AuthState {
        object Loading : AuthState()
        data class Authenticated(val user: FirebaseUser) : AuthState()
        object Unauthenticated : AuthState()
    }

    val authState = mutableStateOf<AuthState>(AuthState.Loading)

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        if (auth.currentUser == null) {
            authState.value = AuthState.Unauthenticated
        }
        // else -> 유지 (Loading 상태 유지)
    }

    fun setAuthenticated() {
        auth.currentUser?.let {
            authState.value = AuthState.Authenticated(it)
        }
    }
}
