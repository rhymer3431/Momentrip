package com.mp.momentrip.data


data class UserPreference(
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
    
    private val foodMap = mutableMapOf(
    "한식" to 3.5,  // 예시 실수 값
    "양식" to 2.8,
    "일식" to 4.2,
    "중식" to 3.0
)
    
    override fun hashCode(): Int {
        return preferenceVector?.hashCode() ?: 0
    }

    fun update(target: List<Float>) {
        preferenceVector?.let { current ->
            for (index in current.indices) {
                current[index] = ((current[index] + target[index]) / 2.0f)
            }
        }
    }
}
