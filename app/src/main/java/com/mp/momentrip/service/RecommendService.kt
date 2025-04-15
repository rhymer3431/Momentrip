package com.mp.momentrip.service

import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.view.UserViewModel

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

    suspend fun getRegionByPreference(userPreference: UserPreference?): String {
        val regionVectors = regions.mapValues { (_, keywords) ->
            val keywordList = keywords.split(" ")
            Word2VecModel.getVectorByList(keywordList)
        }
        return regionVectors.maxByOrNull { (_, regionVector) ->
            cosineSimilarity(userPreference?.preferenceVector!!, regionVector)
        }?.key ?: "추천할 지역 없음"
    }

    suspend fun getRecommendPlaceList(userState: UserViewModel){

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