package com.mp.momentrip.service
import android.util.Log
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.util.vector.textToVector

// 사용자 답변을 받고 벡터로 변환
class PreferenceAnalyzer {
    private val answerText = mapOf(
        "A" to "자연 경관 활동 하이킹 캠핑 산 자연 휴식",
        "B" to "문화 역사 탐방 박물관 유적지 전통 건축물",
        "C" to "도시 편리함 현대적 시설 쇼핑 미술관 나이트라이프",
        "D" to "휴식 웰빙 스파 마사지 요가 힐링 리조트"
    )

    private val answers : ArrayList<String> = arrayListOf()

    suspend fun addAnswer(answer:String){
        answers.add(answer)
    }

    suspend fun createUserPreference(): UserPreference {

        val vectors = answers.mapNotNull { answer ->
            answerText[answer]?.let { text ->
                val words = text.split(" ")
                Word2VecModel.getVectorByList(words)
            }
        }

        if (vectors.isEmpty()) {
            return UserPreference(preferenceVector = null) // 예외 처리
        }

        // 평균 벡터 계산
        val averagedVector = vectors
            .reduce { acc, vector ->
                acc.zip(vector) { a, b -> a + b }
            }
            .map { it / vectors.size }
            .toMutableList()
        return UserPreference(preferenceVector = averagedVector)
    }


}

