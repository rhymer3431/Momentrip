package com.mp.momentrip.service

class PreferenceAnalyzer {

    private val answerText = mapOf(
        "A" to "자연 경관 활동 하이킹 캠핑 산 자연 휴식",
        "B" to "문화 역사 탐방 박물관 유적지 전통 건축물",
        "C" to "도시 편리함 현대적 시설 쇼핑 미술관 나이트라이프",
        "D" to "휴식 웰빙 스파 마사지 요가 힐링 리조트"
    )

    private val answers = mutableListOf<String>()

    fun addAnswer(answer: String) {
        answers.add(answer)
    }

    suspend fun createUserVector(): MutableList<Float> {
        val vectors = answers.mapNotNull { answer ->
            answerText[answer]?.let { text ->
                val words = text.split(" ")
                Word2VecModel.getVectorByList(words)
            }
        }

        if (vectors.isEmpty()) return mutableListOf()

        val averagedVector = vectors
            .reduce { acc, vec -> acc.zip(vec) { a, b -> a + b } }
            .map { it / vectors.size }
            .toMutableList()

        return averagedVector
    }
}
