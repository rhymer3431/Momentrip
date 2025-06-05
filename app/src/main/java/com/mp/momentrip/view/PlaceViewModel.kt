package com.mp.momentrip.view

import androidx.lifecycle.ViewModel
import com.mp.momentrip.data.place.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor() : ViewModel() {
    private val _selectedPlace = MutableStateFlow<Place?>(null)
    val selectedPlace: StateFlow<Place?> = _selectedPlace

    fun setPlace(place: Place) {
        _selectedPlace.value = place
    }

    fun clearPlace() {
        _selectedPlace.value = null
    }
}
