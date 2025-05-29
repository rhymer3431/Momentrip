package com.mp.momentrip.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.service.RecommendService
import com.mp.momentrip.service.TourService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class RecommendInitData(
    val region: String,
    val userPreference: UserPreference
)



class RecommendViewModel : ViewModel() {
    private val _preference = MutableStateFlow<UserPreference?>(null)
    val preference: StateFlow<UserPreference?> = _preference.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _recommendRestaurant = MutableStateFlow<List<Place>?>(null)
    val recommendRestaurant: StateFlow<List<Place>?> = _recommendRestaurant.asStateFlow()

    private val _recommendDormitory = MutableStateFlow<List<Place>?>(null)
    val recommendDormitory: StateFlow<List<Place>?> = _recommendDormitory.asStateFlow()

    private val _recommendTourSpot = MutableStateFlow<List<Place>?>(null)
    val recommendTourSpot: StateFlow<List<Place>?> = _recommendTourSpot.asStateFlow()

    private val _favoriteFoodType = MutableStateFlow<String?>(null)
    val favoriteFoodType: StateFlow<String?> = _favoriteFoodType.asStateFlow()

    private var isInitialized = false

    fun initialize(initData: RecommendInitData) {
        if (!isInitialized) {
            Log.d("InitCheck", "Initializing with: ${initData.region}")
            _preference.value = initData.userPreference
            loadRecommendPlaces(
                region = initData.region)
            loadFavoriteFoodType(initData.userPreference)
            isInitialized = true
        }
    }

    fun loadRecommendPlaces(
        region: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val restaurantDeferred = async {
                    RecommendService.getRecommendRestaurants(
                        userPreference = _preference.value!!,
                        region = region)
                }
                val dormitoryDeferred = async {
                    RecommendService.getRecommendDormitories(
                        userPreference = _preference.value!!,
                        region = region)
                }
                val tourSpotDeferred = async {
                    RecommendService.getRecommendTourSpots(
                        userPreference = _preference.value!!,
                        region = region)
                }

                val (restaurants, dormitories, tourSpots) = awaitAll(
                    restaurantDeferred, dormitoryDeferred, tourSpotDeferred
                )

                _recommendRestaurant.value = restaurants
                _recommendDormitory.value = dormitories
                _recommendTourSpot.value = tourSpots
            } catch (e: Exception) {
                // 예외 처리
                Log.e("RecommendViewModel", "Failed to load recommendations", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadFavoriteFoodType(userPreference: UserPreference) {
        viewModelScope.launch {
            _favoriteFoodType.value = RecommendService.getFavoriteFoodType(userPreference)
        }
    }
}
