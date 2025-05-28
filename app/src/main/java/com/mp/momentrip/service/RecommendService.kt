package com.mp.momentrip.service

import android.util.Log
import com.mp.momentrip.data.Category
import com.mp.momentrip.data.FoodCategory
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.openkoreantext.processor.KoreanPosJava
import org.openkoreantext.processor.OpenKoreanTextProcessorJava

object RecommendService {

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

    suspend fun getFavoriteFoodType(userPreference: UserPreference?): String? {
        // 유저의 foodPreference가 null일 경우를 체크
        val foodTypeCode = userPreference?.foodPreference?.foodTypeId?.maxByOrNull{ it.value }?.key

        // foodTypeCode가 null이 아니면 해당 카테고리 찾기
        return if (foodTypeCode != null) {
            FoodCategory.fromCode(foodTypeCode)?.description
        } else {
            null  // foodTypeCode가 null일 경우 null 반환
        }
    }

    suspend fun getRegionByPreference(userPreference: UserPreference?): String {
        val regionVectors = regions.mapValues { (_, keywords) ->
            val keywordList = keywords.split(" ")
            Word2VecModel.getVectorByList(keywordList)
        }
        return regionVectors.maxByOrNull { (_, regionVector) ->
            cosineSimilarity(userPreference?.preferenceVector!!, regionVector)
        }?.key ?: "추천할 지역 없음"
    }


    suspend fun getRecommendRestaurants(
        userPreference: UserPreference,
        region: String
    ): List<Place> {
        val restaurants = TourService.getRestaurantsByRegion(region)

        return calculateTopPlaces(userPreference, restaurants)
    }

    suspend fun getRecommendTourSpots(
        userPreference: UserPreference,
        region: String
    ): List<Place> {
        val tourSpots = TourService.getTouristSpotsByRegion(region)

        return calculateTopPlaces(userPreference, tourSpots)
    }

    /**
     * 숙소 추천
     */
    suspend fun getRecommendDormitories(
        userPreference: UserPreference,
        region: String
    ): List<Place> {
        val accommodations = TourService.getAccommodationsByRegion(region)

        return calculateTopPlaces(userPreference, accommodations)
    }

    /**
     * 공통 추천 계산 로직
     */
    private suspend fun calculateTopPlaces(
        userPreference: UserPreference,
        places: List<Place>,
        topN: Int = 10
    ): List<Place> = coroutineScope {
        val userVec = userPreference.preferenceVector ?: return@coroutineScope emptyList()

        places.map { place ->
            async {
                val placeVec = Word2VecModel.getVectorByPlace(place)
                if (placeVec == null) {
                    Log.w("RecommendService", "place vector null: ${place.title}")
                    null
                } else {
                    val similarity = cosineSimilarity(userVec, placeVec)
                    similarity to place
                }
            }
        }.awaitAll()
            .filterNotNull()
            .sortedByDescending { it.first }
            .take(topN)
            .map { it.second }
    }

    // 코사인 유사도 함수
    private fun cosineSimilarity(vec1: List<Float>, vec2: List<Float>): Float {
        if (vec1.size != vec2.size) return 0f

        var dot = 0f
        var mag1 = 0f
        var mag2 = 0f

        for (i in vec1.indices) {
            val a = vec1[i]
            val b = vec2[i]
            dot += a * b
            mag1 += a * a
            mag2 += b * b
        }

        val denom = kotlin.math.sqrt(mag1) * kotlin.math.sqrt(mag2)
        return if (denom == 0f) 0f else dot / denom
    }


}
