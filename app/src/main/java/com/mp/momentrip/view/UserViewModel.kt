package com.mp.momentrip.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.User
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.service.AccountService

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class UserViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _schedule = MutableStateFlow<Schedule?>(null)
    val schedule: StateFlow<Schedule?> = _schedule.asStateFlow()

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules: StateFlow<List<Schedule>> get() = _schedules.asStateFlow()

    private val _isLoading = MutableStateFlow(false) // 로딩 상태 추가
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val region = MutableStateFlow<String?>(null)
    // 테마 설정
    private val _themeState = MutableStateFlow(ThemeState())

    val themeState: StateFlow<ThemeState> = _themeState.asStateFlow()

    // 테마 변경 메서드
    fun updatePrimary(color: Color) {
        _themeState.update { it.copy(primaryColor = color) }
    }

    fun updateSecondary(color: Color) {
        _themeState.update { it.copy(secondaryColor = color) }
    }

    fun loadUser(firebaseUser: FirebaseUser) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = AccountService.loadUser(firebaseUser) // suspend 함수로 변경
                _user.value = user
                _isLoggedIn.value = true
                Log.d("test","${_user.value?.userPreference}")
                loadSchedules() // 사용자 로드 시 스케줄도 자동 로드
            } catch (e: Exception) {

                logOut()
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun loadSchedules() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val schedules = AccountService.loadSchedulesOfUser() // suspend 함수로 변경
                _schedules.value = schedules
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun updateUser(updater: User?) {
        updater?.userPreference?.preferenceVector?.get(0)?.let { Log.d("test", it.toString()) }
        if (updater != null) {
            AccountService.updateUser(updater)
        }
    }

// 사용 예시
// viewModel.updateUser { it.copy(name = "새 이름") }


    // 사용자 정보 설정
    fun setUser(newUser: User) {
        _user.value = newUser
        _isLoggedIn.value = true // 사용자 설정 시 로그인 상태로 변경
    }
    suspend fun setRegion(region : String){
        this.region.value = region
    }
    fun getRegion() : String?{
        return this.region.value
    }

    // 유효한 사용자 정보 가져오기
    fun getUser(): User? {
        return _user.value

    }
    fun setSchedule(schedule: Schedule){
        viewModelScope.launch {
            _schedule.emit(schedule)  // emit을 사용해 상태 변경
        }
    }
    fun getSchedule(): Schedule?{
        return schedule.value
    }
    // 사용자 선호도 가져오기
    fun getUserPreference(): UserPreference? {
        return getUser()?.userPreference
    }
    fun isValidUserPreference(): Boolean? {

        return _user.value?.userPreference?.preferenceVector?.isNotEmpty()
    }

    fun generateSchedule(startAt: LocalDate, endAt:LocalDate, region: String) {
        viewModelScope.launch {

        }
    }

    fun logIn(email:String, password:String) {
        viewModelScope.launch {
            try {
                AccountService.signIn(email, password)
                val user = AccountService.getCurrentUser()?.let { AccountService.loadUser(it) }
                _user.value = user
                _isLoggedIn.value = true

            } catch (e: Exception) {

            }
        }

    }

    fun logOut() {
        viewModelScope.launch {
            try {
                _user.value = null
                _isLoggedIn.value = false
            } catch (e: Exception) {

            }
        }
    }

}

// 테마 상태를 별도 데이터 클래스로 분리
data class ThemeState(
    val primaryColor: Color = Color(0xFF2196F3),
    val secondaryColor: Color = Color(0xFF03A9F4),
    val backgroundColor: Color = Color(0xFFE3F2FD),
    val surfaceColor: Color = Color.White,
    val isDarkMode: Boolean = false
) {
    // 테마 변경 편의 메서드
    fun copyWithPrimary(color: Color) = copy(primaryColor = color)
    fun copyWithSecondary(color: Color) = copy(secondaryColor = color)
    fun copyWithBackground(color: Color) = copy(backgroundColor = color)
    fun copyWithSurface(color: Color) = copy(surfaceColor = color)
    fun toggleDarkMode() = copy(isDarkMode = !isDarkMode)

    // 프리셋 테마 적용
    fun applyPresetTheme(preset: ThemePreset) = when (preset) {
        ThemePreset.BLUE -> ThemeState(
            primaryColor = Color(0xFF2196F3),
            secondaryColor = Color(0xFF03A9F4),
            backgroundColor = Color(0xFFE3F2FD),
            surfaceColor = Color.White
        )
        ThemePreset.GREEN -> ThemeState(
            primaryColor = Color(0xFF4CAF50),
            secondaryColor = Color(0xFF8BC34A),
            backgroundColor = Color(0xFFE8F5E9),
            surfaceColor = Color.White
        )
        ThemePreset.PURPLE -> ThemeState(
            primaryColor = Color(0xFF9C27B0),
            secondaryColor = Color(0xFFE91E63),
            backgroundColor = Color(0xFFF3E5F5),
            surfaceColor = Color.White
        )
    }
}

enum class ThemePreset { BLUE, GREEN, PURPLE }