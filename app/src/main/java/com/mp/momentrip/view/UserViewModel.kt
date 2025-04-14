package com.mp.momentrip.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.User
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.service.AccountService
import com.mp.momentrip.service.TourService

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _place = MutableStateFlow<Place?>(null)
    val place: StateFlow<Place?> = _place.asStateFlow()


    private val region = MutableStateFlow<String?>(null)



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

    fun setPlace(place: Place?){
        _place.value = place
    }

    fun loadPlaceDetail(){
        viewModelScope.launch {
            _isLoading.value = true
            setPlace(TourService.getDetail(place.value))
            _isLoading.value = false

        }
    }
    fun like() {
        val place = place.value
        viewModelScope.launch {
            val currentUser = _user.value
            if (currentUser != null && place != null) {
                val updatedLiked = currentUser.liked.toMutableList()

                // 이미 좋아요를 눌렀다면 취소, 아니면 추가
                if (updatedLiked.contains(place)) {
                    updatedLiked.remove(place) // 이미 존재하면 제거 (취소)
                } else {
                    updatedLiked.add(place) // 존재하지 않으면 추가 (좋아요)
                }

                // 사용자 업데이트
                _user.value = currentUser.copy(liked = updatedLiked.distinct())
                updateUser(currentUser.copy(liked = updatedLiked.distinct()))
            }
        }
    }


    fun isLiked(): Boolean?{
        return getUser()?.liked?.contains(place.value)
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
