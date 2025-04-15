package com.mp.momentrip.service


import android.util.Log
import com.google.gson.JsonObject
import com.mp.momentrip.api.HuggingFaceApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.*




fun main() = runBlocking {
    // 사용자의 프로필과 장소 설명을 입력값으로 설정
    val userProfile = "저는 자연을 좋아하고 산책을 자주 합니다."
    val placeDesc = "이곳은 산책로와 아름다운 자연 경관을 자랑하는 공원입니다."

    // TravelRecommendation 객체 생성
    val travelRecommendation = TravelRecommendation()

    // 매칭 점수를 받아오는 비동기 호출
    val score = travelRecommendation.getMatchScore(userProfile, placeDesc)

    // 결과 출력
    println("매칭 점수: $score")
}

class TravelRecommendation {
    private val api: HuggingFaceApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-inference.huggingface.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(HuggingFaceApi::class.java)
    }

    suspend fun getMatchScore(userProfile: String, placeDesc: String): Float {

        val payload = mapOf(
            "inputs" to "사용자: $userProfile\n장소: $placeDesc\n이 장소는 사용자에게 얼마나 맞을까?"
        )
        val token = "Bearer YOUR_HUGGINGFACE_TOKEN"

        // 네트워크 호출을 기다리게 처리

        return withContext(Dispatchers.IO) {
            try {

                val response = api.compareTexts(token, payload).execute()

                if (response.isSuccessful) {

                    val score = parseScoreFromResponse(response.body())
                    return@withContext score
                }
            } catch (e: Exception) {

                Log.e("API_CALL", "Error: ${e.message}")
            }
            println("test")
            return@withContext 0f
        }
    }

    private fun parseScoreFromResponse(json: JsonObject?): Float {
        // 실제 응답 구조에 따라 파싱 로직 구현
        return json?.get("score")?.asFloat ?: 0f
    }
}
