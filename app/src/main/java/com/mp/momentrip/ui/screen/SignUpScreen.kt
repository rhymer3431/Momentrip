package com.mp.momentrip.ui.screen

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.google.firebase.auth.FirebaseAuth
import com.mp.momentrip.R
import com.mp.momentrip.data.UserRegisterForm
import com.mp.momentrip.service.AccountService
import com.mp.momentrip.util.MainDestinations
import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavController,
    userState: UserViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "회원가입",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1B1E28),
                modifier = Modifier.padding(top = 32.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f) // 남은 공간 차지
                    .verticalScroll(rememberScrollState())
            ) {
                SignUpForm(navController, userState)
            }
            // Already have an account
            AlreadyHaveAccount(navController)
        }
    }
}

@Composable
fun SignUpForm(
    navController: NavController,
    userState: UserViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    // Gender Dropdown State
    var expanded by remember { mutableStateOf(false) }
    val genders = listOf("남성", "여성", "기타")
    var selectedGender by remember { mutableStateOf(genders[0]) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
    ) {
        // Email Field
        Text(
            text = "이메일",
            fontSize = 16.sp,
            color = Color(0xFF1B1E28),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),

            placeholder = {
                Text(
                    text = "이메일을 입력하세요",
                    color = Color(0xFF1B1E28),
                    fontSize = 16.sp
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Password Field
        Text(
            text = "비밀번호",
            fontSize = 16.sp,
            color = Color(0xFF1B1E28),
            modifier = Modifier.padding(bottom = 8.dp)
        )
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
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.eye),
                    contentDescription = "Toggle password visibility",
                    tint = Color(0xFF7D848D),
                    modifier = Modifier.clickable {
                        passwordVisibility.value = !passwordVisibility.value
                    }
                )
            }
        )

        // Password Hint
        Text(
            text = "비밀번호는 8자리 이상이어야 합니다",
            fontSize = 14.sp,
            color = Color(0xFF7D848D),
            modifier = Modifier.padding(top = 8.dp, start = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
        // Name Field
        Text(
            text = "이름",
            fontSize = 16.sp,
            color = Color(0xFF1B1E28),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),

            placeholder = {
                Text(
                    text = "이름을 입력하세요",
                    color = Color(0xFF1B1E28),
                    fontSize = 16.sp
                )
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        // Gender Dropdown
        Text(
            text = "성별",
            fontSize = 16.sp,
            color = Color(0xFF1B1E28),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            OutlinedTextField(
                value = selectedGender,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { expanded = true },
                shape = RoundedCornerShape(14.dp),

                readOnly = true
            )


        }

        Spacer(modifier = Modifier.height(24.dp))

        // Age Field
        Text(
            text = "나이",
            fontSize = 16.sp,
            color = Color(0xFF1B1E28),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),

            placeholder = {
                Text(
                    text = "나이를 입력하세요",
                    color = Color(0xFF1B1E28),
                    fontSize = 16.sp
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        SignUpButton(
            UserRegisterForm(
                email,
                password,
                name,
                1,
                24
            ),
            navController = navController,
            userState = userState
        )

    }
}

@Composable
fun SignUpButton(
    userRegisterForm: UserRegisterForm,
    navController: NavController,
    userState: UserViewModel
) {
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Button(
        onClick = {
            coroutineScope.launch {
                isLoading = true
                try {
                    val result = AccountService.signUp(userRegisterForm)
                    if (result.isSuccess) {
                        // 회원가입 성공 시 사용자 정보 로드 및 홈 화면으로 이동
                        userState.loadUser(FirebaseAuth.getInstance().currentUser!!)
                        navController.navigate(MainDestinations.HOME_ROUTE) {
                            popUpTo(0)
                        }
                    } else {
                        Toast.makeText(
                            context,
                            result.exceptionOrNull()?.message ?: "회원가입 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } finally {
                    isLoading = false
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 48.dp),
        shape = RoundedCornerShape(16.dp),

        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = "가입",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun AlreadyHaveAccount(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "이미 계정이 있으신가요?",
            fontSize = 14.sp,
            color = Color(0xFF707B81)
        )
        Text(
            text = "로그인",
            fontSize = 14.sp,
            color = Color(0xFFFF7029),
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable { navController.navigate(MainDestinations.SIGN_IN_ROUTE) }
        )
    }
}

