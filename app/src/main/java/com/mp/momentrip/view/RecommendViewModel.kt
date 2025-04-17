package com.mp.momentrip.view

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mp.momentrip.data.Place
import com.mp.momentrip.service.RecommendService
import com.mp.momentrip.service.TourService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class RecommendViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _recommendPlace = MutableStateFlow<List<Place>?>(null)
    val recommendPlace: StateFlow<List<Place>?> = _recommendPlace.asStateFlow()

    private val _recommendRestaurant = MutableStateFlow<List<Place>?>(null)
    val recommendRestaurant: StateFlow<List<Place>?> = _recommendRestaurant.asStateFlow()

    private val _recommendDormitory = MutableStateFlow<List<Place>?>(null)
    val recommendDormitory: StateFlow<List<Place>?> = _recommendDormitory.asStateFlow()

    private val _favoriteFoodType = MutableStateFlow<String?>(null)
    val favoriteFoodType: StateFlow<String?> = _favoriteFoodType.asStateFlow()

    private val _recommendTourSpot = MutableStateFlow<List<Place>?>(null)
    val recommendTourSpot: StateFlow<List<Place>?> = _recommendTourSpot.asStateFlow()

    private val _userState = MutableStateFlow<UserViewModel?>(null)
    val userState: StateFlow<UserViewModel?> = _userState.asStateFlow()

    fun setUser(userState: UserViewModel) {
        _userState.value = userState
    }
    fun setLoading(status: Boolean){
        _isLoading.value = status
    }

    private val _isInitialized = mutableStateOf(false)
    val isInitialized: State<Boolean> = _isInitialized

    fun initialize(userState: UserViewModel) {
        if (!_isInitialized.value) {
            setUser(userState)
            recommendRegion()
            loadRecommendPlaces()
            _isInitialized.value = true
        }
    }

    fun recommendRegion(){
        viewModelScope.launch {
            val recommended = RecommendService.getRegionByPreference(userState.value?.getUserPreference())
            userState.value?.setRegion(recommended)
        }
    }

    fun getFavoriteFoodType(){
        viewModelScope.launch {
            val type = RecommendService.getFavoriteFoodType(userState.value?.getUserPreference())
            _favoriteFoodType.value = type
        }
    }
    fun loadRecommendPlaces() {
        viewModelScope.launch {
            _isLoading.value = true

            val region = userState.value?.getRegion()
            val preference = userState.value?.getUserPreference()
            if (region != null && preference != null) {

                // 비동기적으로 병렬 작업을 시작합니다
                val restaurantDeferred = async { TourService.getRestaurantByRegion(region) }
                val dormitoryDeferred = async { TourService.getDormitoryByRegion(region) }
                val tourSpotDeferred = async { TourService.getTourSpotByRegion(region) }

                // 각 작업 완료된 후에 결과값을 받아옵니다
                _recommendRestaurant.value = restaurantDeferred.await()
                _recommendDormitory.value = dormitoryDeferred.await()
                _recommendTourSpot.value = tourSpotDeferred.await()

            }

            _isLoading.value = false
        }
    }

    fun loadRecommendDormitory() {
        viewModelScope.launch {
            _isLoading.value = true

            // 예시로, 만약 여기에 추가적인 로직이 필요하다면 이곳에 작성합니다.

            _isLoading.value = false
        }
    }
}
