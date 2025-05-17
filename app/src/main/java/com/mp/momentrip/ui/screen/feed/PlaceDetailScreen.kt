package com.mp.momentrip.ui.screen.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mp.momentrip.ui.components.ImageCard
import com.mp.momentrip.view.UserViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailScreen(
    userState: UserViewModel,
    navController: NavController
) {
    /* 데이터 로드 */
    val place by userState.place.collectAsState()

    Column(Modifier.fillMaxWidth().padding(24.dp)) {
        ImageCard(
            imageUrl = place?.firstImage.toString(),

            )
        /* 헤더: ←  제목  ♡ */
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.ArrowBack, "Back",
                Modifier.size(28.dp).clickable { navController.popBackStack() }
            )
            Text(
                text = place?.title ?: "None",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

        }
        Spacer(Modifier.height(24.dp))
        Text(place?.overview ?: "None", fontSize = 20.sp, lineHeight = 28.sp,
            color = Color(0xFF828282))
    }
}