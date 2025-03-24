package com.mp.momentrip.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Answer(
    val answer : String,
    val type : String
)
data class QuestionSet(
    val question : String,
    val answers : List<Answer>
)

class QuestionSetList{
    val questionSet: List<QuestionSet> = listOf(
        QuestionSet(
            question = "여행에서 가장 중요한 요소는 무엇인가요?",
            answers = listOf(
                Answer(
                    answer = "자연 경관과 활동",
                    type = "A"
                ),
                Answer(
                    answer = "문화와 역사 탐방",
                    type = "B"
                ),
                Answer(
                    answer = "도시의 편리함과 현대적 시설",
                    type = "C"
                ),
                Answer(
                    answer = "휴식과 웰빙",
                    type = "D"
                )
            )
        ),
        QuestionSet(
            question = "여행지에서 주로 어떤 활동을 즐기고 싶나요?",
            answers = listOf(
                Answer(
                    answer = "하이킹, 캠핑, 자연 탐방",
                    type = "A"
                ),
                Answer(
                    answer = "박물관, 유적지 방문, 역사적인 장소 탐방",
                    type = "B"
                ),
                Answer(
                    answer = "쇼핑, 미술관, 나이트라이프",
                    type = "C"
                ),
                Answer(
                    answer = "스파, 마사지, 요가 등 힐링 활동",
                    type = "D"
                )
            )
        ),
        QuestionSet(
            question = "휴식과 여유를 느낄 수 있는 여행지로 어떤 곳을 선호하시나요?",
            answers = listOf(
                Answer(
                    answer = "산과 자연이 어우러진 곳",
                    type = "A"
                ),
                Answer(
                    answer = "전통적인 문화와 건축물이 있는 도시",
                    type = "B"
                ),
                Answer(
                    answer = "활기찬 도시와 현대적인 인프라",
                    type = "C"
                ),
                Answer(
                    answer = "편안한 리조트나 휴양지",
                    type = "D"
                )
            )
        ),
        QuestionSet(
            question = "여행 중 가장 중요하게 생각하는 요소는 무엇인가요?",
            answers = listOf(
                Answer(
                    answer = "액티브한 스포츠와 모험적인 활동",
                    type = "A"
                ),
                Answer(
                    answer = "지역 음식과 미식 경험",
                    type = "B"
                ),
                Answer(
                    answer = "고요하고 평화로운 자연 환경",
                    type = "C"
                ),
                Answer(
                    answer = "문화적 경험과 역사 탐방",
                    type = "D"
                )
            )
        )
    )

    public fun getCurrentQuestionSet(index : Int) : QuestionSet{
        return questionSet[index]
    }


}