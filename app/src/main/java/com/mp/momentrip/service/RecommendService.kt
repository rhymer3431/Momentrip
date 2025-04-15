package com.mp.momentrip.service

import android.util.Log
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.view.UserViewModel
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

    

    // 수정 가능한(mutable) 리스트 선언:
    private val foodList = mutableListOf("한식", "양식", "일식", "중식")\
    
    suspend fun getRegionByPreference(userPreference: UserPreference?): String {
        val regionVectors = regions.mapValues { (_, keywords) ->
            val keywordList = keywords.split(" ")
            Word2VecModel.getVectorByList(keywordList)
        }
        return regionVectors.maxByOrNull { (_, regionVector) ->
            cosineSimilarity(userPreference?.preferenceVector!!, regionVector)
        }?.key ?: "추천할 지역 없음"
    }

    suspend fun getRecommendTourSpots(
        userPreference: UserPreference,
        region: String
    ): List<Place> {
        val tourSpots = TourService.getTourSpotByRegion(region)

        return calculateTopPlaces(userPreference, tourSpots)
    }

    /**
     * 숙소 추천
     */
    suspend fun getRecommendAccommodations(
        userPreference: UserPreference,
        region: String
    ): List<Place> {
        val accommodations = TourService.getDormitoryByRegion(region)

        return calculateTopPlaces(userPreference, accommodations)
    }

    /**
     * 공통 추천 계산 로직
     */
    private suspend fun calculateTopPlaces(
        userPreference: UserPreference,
        places: List<Place>
    ): List<Place> {
        val placeSimilarities = places.mapNotNull { place ->
            placeToVector(place)?.let { vector ->
                Pair(cosineSimilarity(userPreference.preferenceVector!!, vector), place)
            }
        }

        return placeSimilarities
            .sortedByDescending { it.first }
            .take(10)
            .map { it.second }
    }
    // 상위 10개의 유사한 레스토랑을 추천하는 함수
    suspend fun getRecommendRestaurant(userPreference: UserPreference): List<Place> {
        // 사용자 선호도 기반으로 지역을 가져오는 메서드
        val region = getRegionByPreference(userPreference)

        // 해당 지역에 있는 레스토랑 정보 가져오기
        val restaurants = TourService.getRestaurantByRegion(region)

        // 각 레스토랑의 벡터와 사용자의 벡터를 비교하여 추천
        val restaurantSimilarities = restaurants.mapNotNull { restaurant ->
            // 레스토랑 정보를 벡터로 변환
            val restaurantVector = placeToVector(restaurant)

            // 레스토랑 벡터가 null이 아닐 경우
            if (userPreference.preferenceVector != null && restaurantVector != null) {
                // 유저의 벡터와 레스토랑의 벡터 간의 유사도 계산
                val userVector = userPreference.preferenceVector // 유저 벡터
                val similarity = cosineSimilarity(userVector, restaurantVector)

                // 유사도와 레스토랑 객체를 쌍으로 묶음
                Pair(similarity, restaurant)
            } else {

                null
            }
        }

        // 유사도 기준으로 내림차순 정렬하고 상위 10개만 추출
        val topRestaurants = restaurantSimilarities
            .sortedByDescending { it.first } // 유사도 높은 순으로 정렬
            .take(10) // 상위 10개 선택
            .map { it.second } // Pair에서 레스토랑 객체만 추출

        // 추천된 레스토랑 반환
        return topRestaurants
    }
    suspend fun placeToVector(place: Place) : List<Float>?{

        if(place.overview != null){
            val keywords = extractKeywords(place.overview)

            return Word2VecModel.getVectorByList(keywords)

        }
        else return null
    }
    suspend fun extractKeywords(text: String): List<String> {
        // 텍스트 정규화
        val normalized = OpenKoreanTextProcessorJava.normalize(text)
        // 토큰화
        val tokens = OpenKoreanTextProcessorJava.tokenize(normalized)
        // 품사 분석 및 명사 필터링
        val tokenList = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens)

        return tokenList
            .filter { it.pos.toString() == "Noun" }
            .map { it.text }.take(5)
    }

    // 코사인 유사도 함수
    private fun cosineSimilarity(vec1: List<Float>, vec2: List<Float>): Float {
        val dotProduct = vec1.zip(vec2).sumOf { (a, b) -> (a * b).toDouble() }.toFloat()
        val magnitude1 = kotlin.math.sqrt(vec1.sumOf { (it * it).toDouble() }).toFloat()
        val magnitude2 = kotlin.math.sqrt(vec2.sumOf { (it * it).toDouble() }).toFloat()

        return if (magnitude1 == 0f || magnitude2 == 0f) 0f
        else dotProduct / (magnitude1 * magnitude2)

    }

}
