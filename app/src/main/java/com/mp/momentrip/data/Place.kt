package com.mp.momentrip.data

data class Place(
    val name: String,
    val category: String,
    val address: String,
    val phone: String,
    val x: Double, // longitude
    val y: Double  // latitude
)
