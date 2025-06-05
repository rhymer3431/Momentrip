package com.mp.momentrip.ui.screen.profile


import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mp.momentrip.service.AccountService
import com.mp.momentrip.ui.MainDestinations
import com.mp.momentrip.ui.ScheduleDestinations
import com.mp.momentrip.ui.components.ThemeCard
import com.mp.momentrip.view.UserViewModel


@Composable
fun ProfileScreen(
    mainNavController: NavController,
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

            UserStats(userState)
            // Profile Menu
            ProfileMenu(
                mainNavController,
                navController)
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
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UserStats(
    userState: UserViewModel
) {
    val user = userState.getUser()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (user != null) {
            Text(
                text = user.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = user.email,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }


        Spacer(modifier = Modifier.height(32.dp))

        // Stats Card
        ThemeCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(78.dp),
            content = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    userState.region.value?.let { StatItem(title = "추천 여행지", value = it) }

                    StatItem(title = "다녀온 여행 횟수", value = userState.getScheduleSize().toString())

                }
            }
        )
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
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun ProfileMenu(
    mainNavController: NavController,
    navController: NavController) {

    ThemeCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(344.dp),
        shape = RoundedCornerShape(16.dp),


    ) {
        Column {
            ProfileMenuItem(
                icon = Icons.Default.Place, // 여행
                title = "나의 여행",
                showDivider = true,
                onClick = { navController.navigate(ScheduleDestinations.SCHEDULE_LIST_ROUTE) }
            )

            ProfileMenuItem(
                icon = Icons.Default.Settings, // 설정
                title = "설정",
                showDivider = true,
                onClick = { navController.navigate(MainDestinations.SETTINGS_ROUTE) }
            )

            ProfileMenuItem(
                icon = Icons.Default.DeleteForever, // 계정 탈퇴
                title = "계정 탈퇴",
                showDivider = true,
                onClick = {
                    // 계정 탈퇴 로직 별도 처리 필요
                }
            )

            ProfileMenuItem(
                icon = Icons.Default.Logout, // 로그아웃
                title = "로그아웃",
                showDivider = true,
                onClick = { AccountService.signOut(mainNavController) }
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
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
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
fun ProfileScreen2() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {



        // Avatar
        Box(
            modifier = Modifier
                .size(width = 89.dp, height = 87.dp)
                .offset(x = 164.dp, y = 149.dp)
                .background(Color(0xFFEADDFF), shape = RoundedCornerShape(7.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()

                    .background(Color(0xFF4F378A))
            )
        }

        // Name
        Text(
            text = "Vincent Guillebaud",
            modifier = Modifier.offset(x = 117.dp, y = 258.dp),
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )



        // List Items: 여행, 설정, 로그아웃, 계정 탈퇴
        Column(
            modifier = Modifier.offset(x = 80.dp, y = 403.dp)
        ) {
            Text("여행", fontSize = 18.sp, color = Color(0xFF343A40), )
            Spacer(modifier = Modifier.height(30.dp))
            Text("설정", fontSize = 18.sp, color = Color(0xFF343A40))
            Spacer(modifier = Modifier.height(30.dp))
            Text("로그아웃", fontSize = 18.sp, color = Color(0xFF343A40))
            Spacer(modifier = Modifier.height(30.dp))
            Text("계정 탈퇴", fontSize = 18.sp, color = Color(0xFF343A40),)
        }

        // Decorative horizontal lines (Line 12/13/14), red icons, etc. can be drawn with Divider or custom Canvas.
    }
}


@Preview(showBackground = true)
@Composable
fun ProfilePreview2(){
    ProfileScreen2()
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
