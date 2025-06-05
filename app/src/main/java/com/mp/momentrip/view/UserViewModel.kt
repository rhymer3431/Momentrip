package com.mp.momentrip.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.schedule.Day
import com.mp.momentrip.data.schedule.Schedule
import com.mp.momentrip.data.user.User

import com.mp.momentrip.data.user.dto.UserDto
import com.mp.momentrip.service.AccountService
import com.mp.momentrip.service.Word2VecModel
import com.mp.momentrip.ui.MainDestinations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class UserViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false) // 로딩 상태 추가
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _place = MutableStateFlow<Place?>(null)
    val place: StateFlow<Place?> = _place.asStateFlow()

    private val _isLiked = MutableStateFlow(false)
    val isLikedFlow: StateFlow<Boolean> = _isLiked.asStateFlow()

    private var currentEditingDayIndex: Int = 0

    private val _schedules = MutableStateFlow<List<Schedule>?>(null)
    val schedules: StateFlow<List<Schedule>?> = _schedules.asStateFlow()

    private val _region = MutableStateFlow<String?>(null)
    val region: StateFlow<String?> = _region.asStateFlow()


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

    suspend fun updateUser(user: User) {
        setUser(user)
        AccountService.updateUser(user.toDto())
    }

// 사용 예시
// viewModel.updateUser { it.copy(name = "새 이름") }


    // 사용자 정보 설정
    fun setUser(newUser: User) {
        _user.value = newUser
        _isLoggedIn.value = true // 사용자 설정 시 로그인 상태로 변경
    }
    suspend fun setRegion(region : String){
        _region.value = region
    }


    // 유효한 사용자 정보 가져오기
    fun getUser(): User? {
        return _user.value
    }




    fun createSchedule(
        title: String,
        region: String,
        startDate: LocalDate,
        endDate: LocalDate,
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
                title = title,
                startDate = startDate,
                endDate = endDate,
                duration = duration,
                days = List(duration.toInt()) { index ->
                    Day(index = index.toLong(), date = startDate.plusDays(index.toLong()))
                },
                region = region
            )

            val updatedSchedules = user.schedules.orEmpty().toMutableList().apply { add(newSchedule) }
            val updatedUser = user.copy(schedules = updatedSchedules)

            try {
                // 🔽 Dto로 변환 후 저장

                updateUser(updatedUser)

                // 🔽 로컬 상태 업데이트
                _user.value = updatedUser

                onSuccess()
            } catch (e: Exception) {
                onError("스케줄 저장에 실패했습니다.")
            }
        }
    }
    fun updateSchedules(updated: List<Schedule>) {
        viewModelScope.launch {
            val current = _user.value ?: return@launch
            updateUser(current.copy(schedules = updated))
        }
    }
    fun refreshSchedules() {
        val uid = Firebase.auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val snapshot = Firebase.firestore
                    .collection("users")
                    .document(uid)
                    .get()
                    .await()

                val user = snapshot.toObject(UserDto::class.java)
                _user.value = user?.toModel()
            } catch (e: Exception) {
                Log.e("UserViewModel", "스케줄 새로고침 실패", e)
            }
        }
    }


    fun addScheduleToUser(newSchedule: Schedule, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val user = _user.value
        if (user == null) {
            onError("유저 정보가 없습니다.")
            return
        }

        val updatedSchedules = user.schedules.orEmpty().toMutableList().apply { add(newSchedule) }
        val updatedUser = user.copy(schedules = updatedSchedules)

        viewModelScope.launch {
            try {
                updateUser(updatedUser) // Firebase 저장
                _user.value = updatedUser // 로컬 상태 반영
                onSuccess()
            } catch (e: Exception) {
                onError("스케줄 저장에 실패했습니다.")
            }
        }
    }


    private fun calculateDuration(startDate: LocalDate, endDate: LocalDate): Long {
        return try {
            ChronoUnit.DAYS.between(startDate, endDate) + 1
        } catch (e: Exception) {
            0
        }
    }


    fun getScheduleSize(): Int{
        return _user.value?.schedules!!.size
    }


    fun isValidUserVector(): Boolean? {
        return _user.value?.userVector?.isNotEmpty()
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
                    val updatedLiked = currentUser.liked.toMutableList()
                    val isNowLiked = if (updatedLiked.contains(currentPlace)) {
                        updatedLiked.remove(currentPlace)
                        false
                    } else {
                        updatedLiked.add(currentPlace)
                        true
                    }

                    // ✅ FoodPreference와 userVector 복사본 준비
                    val newFoodPref = currentUser.foodPreference.copy()
                    val newVector = currentUser.userVector?.toMutableList() ?: MutableList(100) { 0f }

                    withContext(Dispatchers.Default) {
                        if (isNowLiked) {
                            // 음식점인 경우 음식 선호도 반영
                            if (currentPlace.contentTypeId == 39) {
                                currentPlace.cat3?.let { newFoodPref.addFoodTypeCount(it) }
                                currentPlace.title.let { newFoodPref.addFoodNameCount(it) }
                            }

                            // 벡터 업데이트
                            Word2VecModel.like(newVector, currentPlace)
                        } else {
                            Word2VecModel.dislike(newVector, currentPlace)
                        }
                    }

                    val updatedUser = currentUser.copy(
                        liked = updatedLiked,
                        foodPreference = newFoodPref,
                        userVector = newVector
                    )
                    _user.value = updatedUser
                    _isLiked.value = isNowLiked

                    withContext(Dispatchers.IO) {
                        AccountService.updateUser(updatedUser.toDto())
                    }
                } catch (e: Exception) {
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
