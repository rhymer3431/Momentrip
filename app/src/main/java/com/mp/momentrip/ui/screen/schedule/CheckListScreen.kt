package com.mp.momentrip.ui.screen.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mp.momentrip.data.CheckItem
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.view.ScheduleViewModel


@Composable
fun ChecklistScreen(viewModel: ScheduleViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(45.dp))

        // Top App Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Checklist",
                fontSize = 22.sp,
                lineHeight = 28.sp,
                color = Color(0xFF1D1B20),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Input Text Field
        var inputText by remember { mutableStateOf("") }
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Add item") },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            viewModel.addItem(inputText)
            inputText = ""
        }) {
            Text("Add")
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(viewModel.checklistItems) { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = item.checked,
                        onCheckedChange = {
                            viewModel.toggleItem(index)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF65558F),
                            checkmarkColor = Color.White
                        )
                    )
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color(0xFF1D1B20)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        viewModel.removeItem(index)
                    }) {
                        Icon(Icons.Default.Home, contentDescription = "Remove")
                    }
                }
            }
        }
    }
}
