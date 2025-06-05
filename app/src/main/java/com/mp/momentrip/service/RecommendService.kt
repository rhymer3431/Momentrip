package com.mp.momentrip.service

import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.tourAPI.Category
import com.mp.momentrip.data.tourAPI.ContentType
import com.mp.momentrip.data.user.FoodPreference
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

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

    suspend fun getFavoriteFoodType(foodPreference: FoodPreference?): String? {
        val foodTypeCode = foodPreference?.foodTypeId?.maxByOrNull { it.value }?.key
        return foodTypeCode?.let { Category.fromCode(it)?.categoryName }
    }

    suspend fun getRegionByVector(userVector: List<Float>): String {
        val regionVectors = regions.mapValues { (_, keywords) ->
            val keywordList = keywords.split(" ")
            Word2VecModel.getVectorByList(keywordList)
        }

        return regionVectors.maxByOrNull { (_, regionVec) ->
            cosineSimilarity(userVector, regionVec)
        }?.key ?: "추천할 지역 없음"
    }

    suspend fun getRecommendRestaurants(userVector: List<Float>, region: String): List<Place> {
        val restaurants = TourService.getRestaurantsByRegion(region)
        return calculateTopPlaces(userVector, restaurants)
    }

    suspend fun getRecommendTourSpots(userVector: List<Float>, region: String): List<Place> {
        val spots = TourService.getTouristSpotsByRegion(region)
        return calculateTopPlaces(userVector, spots)
    }

    suspend fun getRecommendDormitories(userVector: List<Float>, region: String): List<Place> {
        val dorms = TourService.getAccommodationsByRegion(region)
        return calculateTopPlaces(userVector, dorms)
    }

    suspend fun getRecommendedPlacesByRegion(
        userVector: List<Float>,
        region: String,
        topN: Int = 10
    ): Map<ContentType, List<Place>> {
        val allPlaces = TourService.getAllPlacesByRegion(region)

        return allPlaces
            .groupBy { ContentType.fromId(it.contentTypeId) }
            .mapNotNull { (type, places) ->
                if (type == null) return@mapNotNull null

                val ranked = places.mapNotNull { place ->
                    val vec = Word2VecModel.getVectorByPlace(place)
                    vec?.let { cosineSimilarity(userVector, it) to place }
                }.sortedByDescending { it.first }
                    .take(topN)
                    .map { it.second }

                if (ranked.isNotEmpty()) type to ranked else null
            }
            .toMap()
    }

    private suspend fun calculateTopPlaces(
        userVector: List<Float>,
        places: List<Place>,
        topN: Int = 10
    ): List<Place> = coroutineScope {
        places.map { place ->
            async {
                val vec = Word2VecModel.getVectorByPlace(place)
                vec?.let { cosineSimilarity(userVector, it) to place }
            }
        }.awaitAll()
            .filterNotNull()
            .sortedByDescending { it.first }
            .take(topN)
            .map { it.second }
    }

    fun cosineSimilarity(vec1: List<Float>, vec2: List<Float>): Float {
        if (vec1.size != vec2.size) return 0f

        var dot = 0f
        var mag1 = 0f
        var mag2 = 0f

        for (i in vec1.indices) {
            dot += vec1[i] * vec2[i]
            mag1 += vec1[i] * vec1[i]
            mag2 += vec2[i] * vec2[i]
        }

        val denom = kotlin.math.sqrt(mag1) * kotlin.math.sqrt(mag2)
        return if (denom == 0f) 0f else dot / denom
    }

    fun getFavoriteFood(foodPreference: FoodPreference): Pair<String?, String?> {
        val topType = foodPreference.foodTypeId?.maxByOrNull { it.value }?.key
        val topName = foodPreference.foodNameId?.maxByOrNull { it.value }?.key
        return Pair(topType, topName)
    }
}
