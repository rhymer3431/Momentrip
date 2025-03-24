package com.mp.momentrip.ui.home


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.R
import com.mp.momentrip.data.QuestionSetList


import com.mp.momentrip.service.PreferenceAnalyzer

import com.mp.momentrip.ui.theme.BlueNice
import com.mp.momentrip.ui.theme.OrangeNice
import com.mp.momentrip.ui.theme.TravelAppTheme
import com.mp.momentrip.util.Recommender
import com.mp.momentrip.util.Scheduler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.material.LinearProgressIndicator
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.view.ScheduleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val analyzer = PreferenceAnalyzer()

@Composable
fun PreferenceScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    scheduleViewModel: ScheduleViewModel
) {
    val recommendedRegion = remember { mutableStateOf("") }
    TravelAppTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (recommendedRegion.value == "") {
                QuestionSet(recommendedRegion)
            } else {
                RecommendResult(
                    resultRegion = recommendedRegion.value,
                    navController = navController,
                    scheduleViewModel = scheduleViewModel
                )
            }
        }
    }
}


@Composable
fun QuestionSet(
    recommendedRegion: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val questionSetList = QuestionSetList()
    val questionIndex = remember { mutableStateOf(0) } // 🔹 현재 질문 인덱스 관리
    val totalQuestions = questionSetList.questionSet.size // 🔹 전체 질문 개수
    val progress = questionIndex.value.toFloat() / (totalQuestions - 1).coerceAtLeast(1) // 🔹 진행도 계산

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // 🔹 진행 상태 표시 바
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Color.Blue,
            backgroundColor = Color.LightGray
        )

        // 🔹 진행 상황 텍스트 (예: "질문 2 / 5")
        Text(
            text = "질문 ${questionIndex.value + 1} / $totalQuestions",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        // 🔹 현재 질문 표시
        Text(
            text = questionSetList.getCurrentQuestionSet(questionIndex.value).question,
            fontSize = 34.sp,
            fontFamily = FontFamily(Font(R.font.haru))
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 한 행에 두 개 배치
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val answers = questionSetList.getCurrentQuestionSet(questionIndex.value).answers
            items(answers.size) { index ->
                val answer = answers[index] // 리스트에서 하나씩 가져오기
                AnswerCard(
                    question = answer.answer,
                    type = answer.type,
                    onClick = {
                        analyzer.addAnswer(answer.type)
                        setNextQuestion(questionIndex, recommendedRegion)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


// 🔹 다음 질문으로 넘어가는 함수 (추천 결과 화면 전환 추가)
fun setNextQuestion(questionIndex: MutableState<Int>, recommendedRegion: MutableState<String>) {
    if (questionIndex.value < 3) {
        questionIndex.value += 1
    } else {
        val preference = analyzer.createUserPreference()
        recommendedRegion.value = Recommender.getRecommendRegion(preference)
    }
}

@Composable
fun RecommendResult(
    resultRegion: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    scheduleViewModel: ScheduleViewModel
) {
    TravelAppTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "당신에게 추천하는 여행지는...",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = resultRegion,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = OrangeNice
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Schedule 생성 버튼 추가
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch{
                        scheduleViewModel.setSchedule(Scheduler.createTravelSchedule(3, resultRegion))
                        navController.navigate("schedule")
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "일정 생성하기")
            }
        }
    }
}



@Composable
fun AnswerCard(
    question: String,
    type: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(16.dp)) // 테두리 추가
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            elevation = 0.dp, // 그림자 제거
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .clickable(onClick = onClick)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Text(
                    text = question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = type,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}



