package com.mp.momentrip.service
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.ui.home.analyzer
import com.mp.momentrip.util.Recommender
import com.mp.momentrip.util.Scheduler
import com.mp.momentrip.util.vector.textToVector
import java.io.PrintStream

// 사용자 답변을 받고 벡터로 변환
class PreferenceAnalyzer {
    private val answerText = mapOf(
        "A" to "자연 경관 활동 하이킹 캠핑 산 자연 휴식",
        "B" to "문화 역사 탐방 박물관 유적지 전통 건축물",
        "C" to "도시 편리함 현대적 시설 쇼핑 미술관 나이트라이프",
        "D" to "휴식 웰빙 스파 마사지 요가 힐링 리조트"
    )

    private val answers : ArrayList<String> = arrayListOf()

    fun addAnswer(answer:String){
        answers.add(answer)
    }
    fun removeAnswer(answer:String){
        answers.remove(answer)
    }
    fun createUserPreference(): UserPreference {
        val answers = getAnswers()
        val newUserPreference =
            UserPreference(preferenceVector = textToVector(answers))
        return newUserPreference
    }

    private fun getAnswers(): String{
        val userInput = answers.joinToString(" ") { answerText[it] ?: "" }
        return userInput;
    }
}

