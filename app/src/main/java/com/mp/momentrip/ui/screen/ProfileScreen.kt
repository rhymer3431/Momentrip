package com.mp.momentrip.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mp.momentrip.data.User
import com.mp.momentrip.service.AccountService

import com.mp.momentrip.util.UserDestinations
import com.mp.momentrip.view.UserViewModel
import java.io.File


@Composable
fun ProfileScreen(
    navController: NavController,
    userState: UserViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // Header
            ProfileHeader()

            // User Stats
            UserStats(userState.getUser())

            // Profile Menu
            ProfileMenu(navController)
        }
    }
}

@Composable
fun ProfileHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        Text(
            text = "Profile",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1B1E28)
        )

        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color(0xFFF7F7F9), CircleShape)
                .clickable { /* Handle edit click */ },
            contentAlignment = Alignment.Center
        ) {

        }
    }
}

@Composable
fun UserStats(
    user: User?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(Color(0xFFFFEADF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // This would be replaced with your actual avatar image
            // For now using a placeholder with initials
            Text(
                text = "L",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B1E28)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (user != null) {
            Text(
                text = user.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1B1E28)
            )
            Text(
                text = user.email,
                fontSize = 14.sp,
                color = Color(0xFF7D848D)
            )
        }


        Spacer(modifier = Modifier.height(32.dp))

        // Stats Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(78.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(title = "Reward Points", value = "360")
                HorizontalDivider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.5.dp),
                    color = Color(0xFFF7F7F9)
                )
                StatItem(title = "Travel Trips", value = "238")
                HorizontalDivider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.5.dp),
                    color = Color(0xFFF7F7F9)
                )
                StatItem(title = "Bucket List", value = "473")
            }
        }
    }
}

@Composable
fun StatItem(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1B1E28),
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFFF7029),
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun ProfileMenu(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(344.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(16.dp),


    ) {
        Column {
            ProfileMenuItem(
                icon = Icons.Default.Place,
                title = "나의 여행",
                showDivider = true,
                onClick = {navController.navigate(UserDestinations.SCHEDULE_LIST_ROUTE)}
            )
            ProfileMenuItem(
                icon = Icons.Default.Edit,
                title = "색깔 변경",
                showDivider = true,
                onClick = {navController.navigate(UserDestinations.USER_COLOR_SELECTION)}
            )
            ProfileMenuItem(
                icon = Icons.Filled.AccountBox,
                title = "로그아웃",
                showDivider = true,
                onClick = {
                    AccountService.signOut(navController)}
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    showDivider: Boolean,
    onClick: ()->Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ){
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.weight(1f)
                )

            }
            if (showDivider) {
                Divider(
                    modifier = Modifier.padding(start = 56.dp),
                    color = Color(0xFFF7F7F9),
                    thickness = 1.5.dp
                )
            }
        }
    }

}

@Composable
fun HomeIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .width(134.dp)
                .height(5.dp)
                .background(Color(0xFF1B1E28), RoundedCornerShape(100.dp))
        )
    }
}

@Composable
fun ProfileImage(userId: String) {
    var imageUrl by remember { mutableStateOf<String?>(null) }


    // 이미지 표시
    if (imageUrl != null) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "User Profile",
            modifier = Modifier.size(100.dp)
        )
    } else {
        CircularProgressIndicator() // 로딩 중 UI
    }
}
