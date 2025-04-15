package com.mp.momentrip.ui.screen
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import com.mp.momentrip.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mp.momentrip.service.AccountService
import com.mp.momentrip.util.MainDestinations
import com.mp.momentrip.view.UserViewModel

@Composable
fun SignInScreen(
    navController: NavController,
    modifier: Modifier,
    userState: UserViewModel
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Main content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            item {
                // Title and subtitle
                Text(
                    text = "로그인",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1B1E28),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )



                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "MomenTrip에 오신 것을 환영합니다!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF7D848D),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Email field
                TextField(
                    value = email,
                    onValueChange = { email = it }, // ✅ 값이 변경되도록 수정!
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Password field
                val passwordVisibility = remember { mutableStateOf(false) }
                // Password field
                TextField(
                    value = password,
                    onValueChange = { password = it }, // ✅ 값이 변경되도록 수정!
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    shape = RoundedCornerShape(14.dp),
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),

                    )

                Spacer(modifier = Modifier.height(12.dp))

                // Forgot password
                Text(
                    text = "비밀번호를 잊으셨나요?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFF7029),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Handle forgot password */ },
                    textAlign = TextAlign.End
                )

                Spacer(modifier = Modifier.height(45.dp))



                Button(
                    onClick = {
                        userState.logIn(
                            email, password
                        )
                        navController.navigate(MainDestinations.HOME_ROUTE)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.dp),

                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(
                        text = "로그인",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Sign up text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "계정이 없으신가요?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF707B81)
                    )
                    Text(
                        text = "Sign up",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFF7029),
                        modifier = Modifier.clickable { navController.navigate(MainDestinations.SIGN_UP_ROUTE) }
                    )
                }
            }
        }
    }
}

fun onSuccess(){

}
fun onFailure(){

}

