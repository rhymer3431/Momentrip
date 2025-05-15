
package com.mp.momentrip.ui.components


import androidx.compose.foundation.ExperimentalFoundationApi

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mp.momentrip.data.Activity

import com.mp.momentrip.data.Day


import com.mp.momentrip.data.Place
import com.mp.momentrip.data.Schedule



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleCard(schedule: Schedule, modifier: Modifier = Modifier) {
    // Jetpack Compose의 공식 rememberPagerState 사용
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = {schedule.days.size})

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // ... 기타 UI 요소들 ...

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
            ) { page ->
                Column {
                    Text(text = "🗓️ Day ${page + 1}", style = MaterialTheme.typography.titleSmall)
                    DayCard(day = schedule.days[page])
                }
            }

            // 점 인디케이터 (직접 구현)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                repeat(schedule.days.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .padding(2.dp)
                            .background(
                                color = if (index == pagerState.currentPage)
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.LightGray,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}


@Composable
fun DayCard(day: Day, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            day.timeTable.forEach { timeSlot ->
                ActivityItem(timeSlot)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
@Composable
fun ActivityItem(timeSlot: Activity, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(text = "🕒 ${timeSlot.startTime}", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(4.dp))
            PlaceCard(timeSlot.place, onClick =  {})
        }
    }
}

