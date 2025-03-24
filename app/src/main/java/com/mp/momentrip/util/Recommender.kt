package com.mp.momentrip.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.service.RetrofitClient
import com.mp.momentrip.service.convertPlaces
import com.mp.momentrip.util.api.getKakaoAPI
import com.mp.momentrip.util.vector.cosineSimilarity
import com.mp.momentrip.util.vector.textToVector

object Recommender {

    // 비동기 요청으로 변경된 searchPlaces 함수
    private suspend fun searchPlaces(
        query: String,
        category: String,
        region: String,
        apiKey: String
    ): List<Place> {
        val combinedQuery = "$region $query"
        val kakaoApi = RetrofitClient.kakaoApiService
        val places = mutableListOf<Place>()

        // 페이지 1, 2에 대해 반복 요청
        for (page in 1..2) {
            try {
                val response = withContext(Dispatchers.IO) {
                    kakaoApi.searchPlaces(
                        authorization = "KakaoAK $apiKey",
                        query = combinedQuery,
                        categoryGroupCode = com.mp.momentrip.data.categoryCode[category] ?: "FD6",
                        size = 15,
                        page = page,
                        sort = "accuracy"
                    ).execute()  // 비동기 요청을 비동기 콜백으로 변경
                }

                if (response.isSuccessful) {
                    response.body()?.let { convertPlaces(it.documents) }
                        ?.let { places.addAll(it) }
                } else {
                    println("Error: ${response.code()}, ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }

        // 결과 리스트 반환
        return places
    }

    // 비동기적으로 호출하는 getRestaurantList 함수
    suspend fun getRestaurantList(region: String): List<Place> {
        return searchPlaces(
            query = "맛집",
            category = "음식점",
            region = region,
            apiKey = getKakaoAPI()
        )
    }

    // 비동기적으로 호출하는 getHotelList 함수
    suspend fun getHotelList(region: String): List<Place> {
        return searchPlaces(
            query = "호텔",
            category = "숙박",
            region = region,
            apiKey = getKakaoAPI()
        )
    }

    // 비동기적으로 호출하는 getAttractionList 함수
    suspend fun getAttractionList(region: String): List<Place> {
        return searchPlaces(
            query = "관광명소",
            category = "관광명소",
            region = region,
            apiKey = getKakaoAPI()
        )
    }

    private val regions = mapOf(
        "제주도" to "자연 경관 하이킹 캠핑 산 휴식",
        "경주" to "문화 역사 탐방 박물관 유적지 전통 건축물",
        "서울" to "도시 편리함 현대적 시설 쇼핑 미술관 나이트라이프",
        "강릉" to "휴식 웰빙 스파 마사지 요가 힐링 리조트 경관 자연 산",
        "부산" to "도시 현대적 시설 쇼핑 해변 휴식",
        "전주" to "문화 역사 전통 음식 건축물",
        "속초" to "자연 경관 휴식 해변 힐링",
        "안동" to "문화 역사 전통 건축물 음식"
    )

    // 사용자의 선호에 맞는 지역 추천
    fun getRecommendRegion(userPreference: UserPreference): String {
        val userVector = userPreference.preferenceVector
        val bestMatch = regions.maxByOrNull { (_, description) ->
            cosineSimilarity(userVector, textToVector(description))
        }

        return bestMatch?.key ?: "추천할 여행지가 없습니다."
    }
}
