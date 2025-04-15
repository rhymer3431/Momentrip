package com.mp.momentrip.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mp.momentrip.data.Place
import com.mp.momentrip.service.TourService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecommendViewModel() : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _recommendPlace = MutableStateFlow<List<Place>?>(null)
    val recommendPlace: StateFlow<List<Place>?> = _recommendPlace.asStateFlow()

    private val _recommendRestaurant = MutableStateFlow<List<Place>?>(null)
    val recommendRestaurant: StateFlow<List<Place>?> = _recommendRestaurant.asStateFlow()

    private val _userState = MutableStateFlow<UserViewModel?>(null)
    val userState: StateFlow<UserViewModel?> = _userState.asStateFlow()

    fun setUser(userState: UserViewModel){
        _userState.value = userState
    }
    fun loadRecommendRestaurant() {
        viewModelScope.launch {
            _isLoading.value = true

            val region = userState.value?.getRegion()

            val restaurants = region?.let { TourService.getRestaurantByRegion(it) }
            _recommendRestaurant.value = restaurants

            _isLoading.value = false
        }
    }
}
