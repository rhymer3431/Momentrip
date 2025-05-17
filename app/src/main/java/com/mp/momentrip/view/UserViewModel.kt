package com.mp.momentrip.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import com.mp.momentrip.data.Activity
import com.mp.momentrip.data.Day
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.User
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.service.AccountService
import com.mp.momentrip.service.TourService
import com.mp.momentrip.service.Word2VecModel
import com.mp.momentrip.ui.MainDestinations
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class UserViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()



    private val _selectedScheduleIndex = MutableStateFlow<Int?>(null)
    val selectedScheduleIndex: StateFlow<Int?> get() = _selectedScheduleIndex.asStateFlow()


    private val _isLoading = MutableStateFlow(false) // 로딩 상태 추가
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _place = MutableStateFlow<Place?>(null)
    val place: StateFlow<Place?> = _place.asStateFlow()

    private val _isLiked = MutableStateFlow(false)
    val isLikedFlow: StateFlow<Boolean> = _isLiked.asStateFlow()

    private var currentEditingDayIndex: Int = 0

    fun setEditingDayIndex(index: Int) {
        currentEditingDayIndex = index
    }


    private val region = MutableStateFlow<String?>(null)

    fun loadUser(firebaseUser: FirebaseUser) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = AccountService.loadUser(firebaseUser) // suspend 함수로 변경
                _user.value = user

                _isLoggedIn.value = true
            } catch (e: Exception) {

                logOut()
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun updateUser(updater: User?) {
        if (updater != null) {
            setUser(updater)
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


    fun getSchedule(): Schedule? {
        val index = _selectedScheduleIndex.value
        return if (index != null && _user.value?.schedules?.indices?.contains(index) == true ) {
            _user.value?.schedules!![index]
        } else {
            null
        }
    }


    fun createSchedule(
        region: String,
        startDate: String,
        endDate: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val user = _user.value
            if (user == null) {
                onError("사용자 정보가 없습니다.")
                return@launch
            }

            val duration = calculateDuration(startDate, endDate)
            if (duration <= 0) {
                onError("시작일과 종료일을 다시 확인하세요.")
                return@launch
            }

            val newSchedule = Schedule(
                user = user.email,
                startDate = startDate,
                endDate = endDate,
                duration = duration,
                days = List(duration.toInt()) { Day() },
                region = region
            )

            val updatedSchedules = user.schedules.orEmpty().toMutableList().apply { add(newSchedule) }
            val updatedUser = user.copy(schedules = updatedSchedules)
            try {
                updateUser(updatedUser)

                onSuccess()
            } catch (e: Exception) {
                onError("스케줄 저장에 실패했습니다.")
            }
        }
    }

    private fun calculateDuration(startDate: String, endDate: String): Long {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val start = LocalDate.parse(startDate, formatter)
            val end = LocalDate.parse(endDate, formatter)
            ChronoUnit.DAYS.between(start, end) + 1
        } catch (e: Exception) {
            0
        }
    }

    fun getScheduleSize(): Int{
        return _user.value?.schedules!!.size
    }
    // 사용자 선호도 가져오기
    fun getUserPreference(): UserPreference? {
        return getUser()?.userPreference
    }
    fun isValidUserPreference(): Boolean? {

        return _user.value?.userPreference?.preferenceVector?.isNotEmpty()
    }

    fun setPlace(place: Place?){
        _place.value = place
        updateIsLikedState()
    }
    fun like(currentPlace: Place?) {
        viewModelScope.launch {
            val currentUser = _user.value
            _isLoading.value = true

            if (currentUser != null && currentPlace != null) {
                try {
                    // 좋아요 상태 변경 (UI 스레드)
                    val updatedLiked = currentUser.liked.toMutableList()
                    val isNowLiked = if (updatedLiked.contains(currentPlace)) {
                        updatedLiked.remove(currentPlace)
                        false
                    } else {
                        updatedLiked.add(currentPlace)
                        true
                    }

                    // 사용자 정보 업데이트 (UI 스레드)
                    val updatedUser = currentUser.copy(liked = updatedLiked)
                    _user.value = updatedUser
                    _isLiked.value = isNowLiked

                    // 무거운 연산을 백그라운드로 이동
                    withContext(Dispatchers.Default) {
                        if (isNowLiked) {
                            // 벡터 계산은 백그라운드에서 수행
                            Word2VecModel.like(
                                currentUser.userPreference.preferenceVector,
                                currentPlace
                            )
                        }
                        else{
                            Word2VecModel.dislike(
                                currentUser.userPreference.preferenceVector,
                                currentPlace
                            )
                        }

                        // 백엔드 동기화 (IO 스레드)
                        withContext(Dispatchers.IO) {
                            AccountService.updateUser(updatedUser)
                        }
                    }
                } catch (e: Exception) {
                    // 오류 발생 시 롤백
                    _user.value = currentUser

                    Log.e("UserViewModel", "Like failed", e)
                } finally {
                    _isLoading.value = false
                }
            } else {
                _isLoading.value = false
            }
        }
    }

    fun isLiked(): Boolean {
        val currentPlace = place.value ?: return false
        return _user.value?.liked?.contains(currentPlace) ?: false
    }

    private fun updateIsLikedState() {
        _isLiked.value = isLiked()
    }


    fun logIn(email:String, password:String, navController: NavController) {
        viewModelScope.launch {
            try {
                AccountService.signIn(email, password)
                val user = AccountService.getCurrentUser()?.let { AccountService.loadUser(it) }
                _user.value = user
                _isLoggedIn.value = true
                navController.navigate(MainDestinations.FEED_ROUTE)

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
