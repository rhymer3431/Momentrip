package com.mp.momentrip.data.user


data class FoodPreference(
    val foodTypeId: MutableMap<String, Int>? = null,
    val foodNameId: MutableMap<String, Int>? = null
){
    fun addFoodTypeCount(code: String){
        foodTypeId?.set(code, foodTypeId[code]?.plus(1) ?: 1)
    }
    fun addFoodNameCount(code: String){
        foodNameId?.set(code, foodNameId[code]?.plus(1) ?: 1)
    }
}
