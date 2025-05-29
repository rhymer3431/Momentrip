package com.mp.momentrip.ui.screen.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.view.RecommendInitData
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.ScheduleViewModel


@Composable
fun ActivitySelectScreen(
    scheduleViewModel: ScheduleViewModel,
    recommendViewModel: RecommendViewModel,
    onPlaceSelected: (Place) -> Unit,
    onCancel: () -> Unit,
) {
    val schedule by scheduleViewModel.schedule.collectAsState()
    val region = schedule?.region
    val categories = listOf("관광명소", "음식점", "숙소")
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    val isLoading by recommendViewModel.isLoading.collectAsState()
    val tourList by recommendViewModel.recommendTourSpot.collectAsState()
    val foodList by recommendViewModel.recommendRestaurant.collectAsState()
    val dormList by recommendViewModel.recommendDormitory.collectAsState()

    val places = when (selectedCategory) {
        "관광명소" -> tourList
        "음식점" -> foodList
        "숙소" -> dormList
        else -> null
    } ?: emptyList()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            categories.forEach { category ->
                Button(
                    onClick = { selectedCategory = category },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategory == category) Color(0xFF4F8EDA) else Color.LightGray
                    )
                ) {
                    Text(category)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(places) { place ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPlaceSelected(place) },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = place.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text(text = place.addr1 ?: "", fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onCancel, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("취소")
        }
    }
}

