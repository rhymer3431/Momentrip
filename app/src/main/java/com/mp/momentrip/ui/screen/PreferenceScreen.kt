package com.mp.momentrip.ui.screen


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.R
import com.mp.momentrip.data.QuestionSetList


import com.mp.momentrip.service.PreferenceAnalyzer

import com.mp.momentrip.ui.theme.OrangeNice


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.os.bundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mp.momentrip.data.Answer
import com.mp.momentrip.ui.components.DotsIndicator
import com.mp.momentrip.ui.theme.MomenTripTheme
import com.mp.momentrip.util.MainDestinations
import com.mp.momentrip.util.UserDestinations

import com.mp.momentrip.view.QuestionViewModel
import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun QuestionScreen(
    modifier: Modifier,
    navController: NavController,
    userState: UserViewModel
) {
    val questionViewModel: QuestionViewModel = viewModel() // ✅ 변경
    val isLoading by questionViewModel.isLoading.collectAsState()
    val currentIndex by questionViewModel.currentIndex.collectAsState()

    if (isLoading) {
        LoadingScreen()
    }
    else{
        Column(
            modifier = modifier.fillMaxSize().padding(36.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = questionViewModel.getQuestion(currentIndex),
                fontSize = 34.sp,
                fontFamily = FontFamily(Font(R.font.omyu))
            )
            Spacer(modifier = Modifier.height(100.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val answers = questionViewModel.getAnswers(currentIndex)
                items(answers.size) { index ->
                    val answer = answers[index]
                    AnswerCard(
                        answer = answer,
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                questionViewModel.getAnalyzer().addAnswer(answer.type)
                                questionViewModel.setNextQuestion(navController, userState)
                            }

                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
            DotsIndicator(
                currentIndex,
                questionViewModel.questionSetSize
            )
        }
    }

}


@Composable
fun ImageOption(imageRes: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(124.dp)
    ) {
        Box(
            modifier = Modifier
                .size(124.dp, 126.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFD9D9D9))
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            lineHeight = 25.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}




@Composable
fun AnswerCard(
    answer: Answer,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),

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

                Image(
                    painter = painterResource(id = answer.image_id),
                    contentDescription = "",
                    modifier = Modifier.clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = answer.answer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF// ARGB 형식 (노란색 배경)
)
@Composable
fun QuestionScreenPreview(){
        MomenTripTheme {
            QuestionScreen(
                modifier = Modifier,
                navController = rememberNavController(),
                userState = UserViewModel()
            )
        }

}

@Preview
@Composable
fun AnswerCardPreview(){

        AnswerCard(
            Answer(
                answer = "응답",
                image_id = R.drawable.city,
                type = "A"
            ),
            onClick = {}
        )

}

