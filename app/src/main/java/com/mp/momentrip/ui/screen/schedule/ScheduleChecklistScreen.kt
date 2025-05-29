package com.mp.momentrip.ui.schedule // Or your actual package

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.data.CheckItem // Assuming: data class CheckItem(val id: String, val name: String, var checked: Boolean)
import com.mp.momentrip.view.ScheduleViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import sh.calvin.reorderable.* // ReorderableItem, draggableHandle, rememberReorderableLazyListState
import java.util.UUID // For generating unique IDs for new CheckItems

/* ----------- 리스트 UI ----------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleChecklistScreen(
    scheduleViewModel: ScheduleViewModel,
    onChecklistChange: (List<CheckItem>) -> Unit
) {
    val schedule by scheduleViewModel.schedule.collectAsState()
    var checklist by remember { mutableStateOf(schedule?.checklist ?: emptyList()) }
    val context = LocalContext.current

    // Effect to update local checklist if it changes in the ViewModel
    LaunchedEffect(schedule?.checklist) {
        schedule?.checklist?.let { updatedScheduleChecklist ->
            checklist = updatedScheduleChecklist
        }
    }

    var showAddItemDialog by remember { mutableStateOf(false) }
    var newItemText by remember { mutableStateOf("") }
    var isInEditMode by remember { mutableStateOf(false) }

    if (isInEditMode) {
        ChecklistEditScreen(
            initialChecklist = checklist,
            onDoneEditing = { updatedList ->
                checklist = updatedList
                onChecklistChange(updatedList)
                isInEditMode = false
            },
            onCancelEditing = {
                isInEditMode = false
            }
        )
    } else {
        // Display mode for checklist
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "체크리스트",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    Button(
                        onClick = { isInEditMode = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
                    ) {
                        Text("편집", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onChecklistChange(checklist)
                            Toast.makeText(context, "저장되었습니다", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34C759)),
                    ) {
                        Text("저장", color = Color.White)
                    }
                }
            }

            if (checklist.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "체크리스트 항목이 없습니다. 항목을 추가해주세요.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(checklist, key = { it.id }) { item ->
                        val cardShape = RoundedCornerShape(14.dp)
                        Surface(
                            shape = cardShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shadowElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val updatedItem = item.copy(checked = !item.checked)
                                        checklist = checklist.map {
                                            if (it.id == item.id) updatedItem else it
                                        }
                                        onChecklistChange(checklist)
                                    }
                            ) {
                                Checkbox(
                                    checked = item.checked,
                                    onCheckedChange = { isChecked ->
                                        val updatedItem = item.copy(checked = isChecked)
                                        checklist = checklist.map {
                                            if (it.id == item.id) updatedItem else it
                                        }
                                        onChecklistChange(checklist)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF007AFF),
                                        uncheckedColor = Color(0xFF8E8E93)
                                    )
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 16.sp,
                                        color = if (item.checked) Color(0xFF8E8E93) else MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.alpha(if (item.checked) 0.6f else 1f)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showAddItemDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("항목 추가", color = Color.White, fontSize = 16.sp)
            }
        }

        if (showAddItemDialog) {
            AlertDialog(
                onDismissRequest = {
                    showAddItemDialog = false
                    newItemText = ""
                },
                title = { Text("새 체크 항목 추가") },
                text = {
                    OutlinedTextField(
                        value = newItemText,
                        onValueChange = { newItemText = it },
                        singleLine = true,
                        placeholder = { Text("예: 여권 챙기기") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newItemText.isNotBlank()) {
                                val newItem = CheckItem(
                                    id = UUID.randomUUID().toString(),
                                    name = newItemText.trim(),
                                    checked = false
                                )
                                checklist = checklist + newItem
                                onChecklistChange(checklist)
                            }
                            showAddItemDialog = false
                            newItemText = ""
                        }
                    ) {
                        Text("추가")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showAddItemDialog = false
                            newItemText = ""
                        }
                    ) {
                        Text("취소")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistEditScreen(
    initialChecklist: List<CheckItem>,
    onDoneEditing: (List<CheckItem>) -> Unit,
    onCancelEditing: () -> Unit
) {
    var editableItems by remember { mutableStateOf(initialChecklist) }
    var editTarget by remember { mutableStateOf<CheckItem?>(null) }
    var editText by remember { mutableStateOf("") }

    val listState = rememberLazyListState()
    val reorderState = rememberReorderableLazyListState(listState) { from, to ->
        editableItems = editableItems.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF2F2F7))) {
        Surface(shadowElevation = 3.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onCancelEditing) {
                    Text("취소", color = Color(0xFF007AFF), fontSize = 16.sp)
                }
                Text(
                    "항목 편집",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 17.sp
                )
                TextButton(onClick = { onDoneEditing(editableItems) }) {
                    Text("저장", color = Color(0xFF007AFF), fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                }
            }
        }

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(editableItems, key = { it.id }) { item ->
                ReorderableItem(reorderState, key = item.id) { isDragging ->
                    val cardShape = RoundedCornerShape(14.dp)

                    val editAction = SwipeAction(
                        icon = { Icon(Icons.Default.Edit, contentDescription = "수정", tint = Color.White) },
                        background = Color(0xFF007AFF),
                        onSwipe = {
                            editTarget = item
                            editText = item.name
                        }
                    )

                    val deleteAction = SwipeAction(
                        icon = { Icon(Icons.Default.Delete, contentDescription = "삭제", tint = Color.White) },
                        background = Color(0xFFFF3B30),
                        onSwipe = {
                            editableItems = editableItems.filterNot { it.id == item.id }
                        }
                    )

                    SwipeableActionsBox(
                        startActions = listOf(editAction),
                        endActions = listOf(deleteAction),
                        swipeThreshold = 80.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Surface(
                            shape = cardShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shadowElevation = if (isDragging) 8.dp else 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DragHandle,
                                    contentDescription = "드래그 핸들",
                                    tint = Color(0xFFC7C7CC),
                                    modifier = Modifier
                                        .size(24.dp)
                                        .draggableHandle()
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (editTarget != null) {
        AlertDialog(
            onDismissRequest = { editTarget = null },
            title = { Text("항목 이름 수정") },
            text = {
                OutlinedTextField(
                    value = editText,
                    onValueChange = { editText = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    editTarget?.let { target ->
                        editableItems = editableItems.map {
                            if (it.id == target.id) it.copy(name = editText.trim()) else it
                        }
                    }
                    editTarget = null
                }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { editTarget = null }) {
                    Text("취소")
                }
            }
        )
    }
}
