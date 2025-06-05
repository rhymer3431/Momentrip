package com.mp.momentrip.ui.screen.user

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mp.momentrip.R
import com.mp.momentrip.data.user.UserRegisterForm
import com.mp.momentrip.service.AccountService
import com.mp.momentrip.ui.MainDestinations
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
    val genders = listOf("남성", "여성")
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
        OutlinedTextField(
            value = password,
            onValueChange = { password = it }, // ✅ 값이 변경되도록 수정!
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = {
                Text(
                    text = "비밀번호를 입력하세요",
                    color = Color(0xFF1B1E28),
                    fontSize = 16.sp
                )
            },

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
// Gender Dropdown
        Text(
            text = "성별",
            fontSize = 16.sp,
            color = Color(0xFF1B1E28),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            // 드롭다운 트리거 (클릭 가능한 TextField)
            OutlinedTextField(
                value = selectedGender,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { expanded = true },
                shape = RoundedCornerShape(14.dp),
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "성별 선택",
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                }
            )

            // 드롭다운 메뉴
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                genders.forEach { gender ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = gender,
                                fontSize = 16.sp,
                                color = Color(0xFF1B1E28)
                            )
                        },
                        onClick = {
                            selectedGender = gender
                            expanded = false
                        }
                    )
                }
            }
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
                when (selectedGender) {
                    "남성" -> 0
                    "여성" -> 1
                    else -> 2 // 기타 경우
                },
                age.toIntOrNull() ?: 0 // 안전한 변환을 위해 toIntOrNull 사용
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
                        navController.navigate(MainDestinations.PREFERENCE_ANALYZE) {
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
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
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
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable { navController.navigate(MainDestinations.SIGN_IN_ROUTE) }
        )
    }
}