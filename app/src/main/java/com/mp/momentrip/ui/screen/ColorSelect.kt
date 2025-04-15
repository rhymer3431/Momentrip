package com.mp.momentrip.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mp.momentrip.view.MomenTripTheme
import com.mp.momentrip.view.PresetThemes
import com.mp.momentrip.view.ThemePreset
import com.mp.momentrip.view.ThemeViewModel
import com.mp.momentrip.view.UserViewModel
import kotlin.random.Random


@Composable
fun ColorPickerDialog(
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedColor by remember { mutableStateOf(currentColor) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "색상 선택",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 간단한 색상 그리드
                val colors = listOf(
                    Color.Red, Color.Green, Color.Blue,
                    Color.Yellow, Color.Magenta, Color.Cyan,
                    Color(0xFFFFA000), // Orange
                    Color(0xFF795548), // Brown
                    Color(0xFF607D8B), // Blue Grey
                    Color(0xFF9E9E9E)  // Grey
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier.height(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(colors) { color ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(color)
                                .border(
                                    width = 2.dp,
                                    color = if (selectedColor == color) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 선택된 색상 미리보기
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(selectedColor)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "#${selectedColor.toArgb().toUInt().toString(16).takeLast(6)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("취소")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = { onColorSelected(selectedColor) }) {
                        Text("적용")
                    }
                }
            }
        }
    }
}
@Composable
fun ThemeChangerScreen(viewModel: UserViewModel) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 현재 테마 미리보기
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Primary Color",
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // 색상 변경 버튼
        Button(
            onClick = {
                viewModel.updatePrimary(
                    Color(
                        red = Random.nextFloat(),
                        green = Random.nextFloat(),
                        blue = Random.nextFloat()
                    )
                )
            }
        ) {
            Text("Change Primary Color")
        }

    }
}
