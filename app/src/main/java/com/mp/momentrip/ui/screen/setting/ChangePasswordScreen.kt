package com.mp.momentrip.ui.screen.setting

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ChangePasswordScreen(
    onBackClick: () -> Unit = {}
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        // 상단 앱바
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로가기"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "비밀번호 변경",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 기존 비밀번호
        TextFieldLabel("기존 비밀번호")
        PasswordField(value = oldPassword, onValueChange = { oldPassword = it })

        Spacer(modifier = Modifier.height(24.dp))

        // 새로운 비밀번호
        TextFieldLabel("새로운 비밀번호")
        PasswordField(value = newPassword, onValueChange = { newPassword = it })

        Spacer(modifier = Modifier.height(24.dp))

        // 비밀번호 재입력
        TextFieldLabel("비밀번호 재입력")
        PasswordField(value = confirmPassword, onValueChange = { confirmPassword = it })

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                if (newPassword != confirmPassword) {
                    resultMessage = "새 비밀번호가 일치하지 않습니다."
                    return@Button
                }

                scope.launch {
                    val result = changePassword(oldPassword, newPassword)
                    resultMessage = if (result.isSuccess) {
                        "비밀번호 변경 성공"
                    } else {
                        "실패: ${result.exceptionOrNull()?.message}"
                    }
                }
            },
            modifier = Modifier
                .height(46.dp)
                .width(125.dp),
            shape = RoundedCornerShape(9.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BAEC))
        ) {
            Text(
                text = "변경하기",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // 로그 또는 메시지 출력 (임시)
        resultMessage?.let {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = it, color = Color.Gray, fontSize = 14.sp)
            Log.d("ChangePassword", it)
        }
    }
}

@Composable
private fun TextFieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF090914),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = PasswordVisualTransformation()
    )
}

suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
    return try {
        val user = FirebaseAuth.getInstance().currentUser
            ?: return Result.failure(Exception("로그인된 사용자가 없습니다."))
        val email = user.email
            ?: return Result.failure(Exception("이메일을 확인할 수 없습니다."))

        val credential = EmailAuthProvider.getCredential(email, currentPassword)
        user.reauthenticate(credential).await()

        user.updatePassword(newPassword).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
fun ChangePasswordScreenPreview() {
    ChangePasswordScreen()
}
