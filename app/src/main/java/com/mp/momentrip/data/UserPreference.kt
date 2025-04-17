package com.mp.momentrip.data

import android.util.Log


data class UserPreference(
    val foodPreference: FoodPreference = FoodPreference(),
    val preferenceVector: MutableList<Float>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserPreference

        if (preferenceVector != null) {
            if (other.preferenceVector == null) return false
            if (preferenceVector != other.preferenceVector) return false
        } else if (other.preferenceVector != null) return false

        return true
    }
    
    override fun hashCode(): Int {
        return preferenceVector?.hashCode() ?: 0
    }

    fun like(target: List<Float>) {
        preferenceVector?.let { current ->
            for (index in current.indices) {
                current[index] = ((current[index] + target[index]) / 2.0f)
            }
        }
        Log.d("test",preferenceVector?.get(0).toString())
    }
    fun dislike(target: List<Float>) {
        preferenceVector?.let { current ->
            for (index in current.indices) {
                current[index] = (2*current[index] - target[index])
            }
        }
    }
}

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
