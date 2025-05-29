package com.mp.momentrip.ui.screen.schedule

import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.ReorderableItem

import sh.calvin.reorderable.rememberReorderableLazyListState

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import kotlinx.coroutines.launch

import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*

import androidx.compose.material3.Text
import androidx.compose.runtime.*

import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import kotlinx.coroutines.launch

@Composable
fun ReorderableList17() {
    // 1) 초기값으로 타입을 명시하거나 초기값에 의해 추론되도록 반드시 처리
    var items by remember {
        mutableStateOf<List<String>>(List(20) { "Item ${it + 1}" })
    }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // 2) 드래그 중인 항목 인덱스 & y 오프셋
    var draggingIndex by remember { mutableStateOf<Int?>(null) }
    var offsetY by remember { mutableStateOf(0f) }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(items, key = { index, _ -> index }) { index, item ->
            val isDragging = index == draggingIndex

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isDragging)
                            Modifier.offset { IntOffset(0, offsetY.toInt()) }
                        else
                            Modifier.animateItem(        // 1.7.0부터는 animateItem()
                                placementSpec = spring(stiffness = Spring.StiffnessMedium)
                            )
                    )
                    .background(
                        if (isDragging)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else
                            MaterialTheme.colorScheme.surface
                    )
                    .padding(16.dp)
                    .pointerInput(index) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                draggingIndex = index
                            },
                            onDragEnd = {
                                draggingIndex = null
                                offsetY = 0f
                            },
                            onDragCancel = {
                                draggingIndex = null
                                offsetY = 0f
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                offsetY += dragAmount.y

                                val layoutInfo = listState.layoutInfo
                                val visible = layoutInfo.visibleItemsInfo

                                // 3) 드래그 중인 아이템 정보
                                val current = visible.firstOrNull { it.index == draggingIndex }
                                    ?: return@detectDragGesturesAfterLongPress
                                val centerY = current.offset + offsetY + current.size / 2

                                // 4) 교체 대상 탐색
                                val target = visible
                                    .firstOrNull { it.index != draggingIndex
                                            && centerY in it.offset..(it.offset + it.size) }

                                // 5) 리스트 순서 교체
                                if (target != null && draggingIndex != null) {
                                    val updated = items.toMutableList()
                                    val moved = updated.removeAt(draggingIndex!!)
                                    updated.add(target.index, moved)
                                    items = updated
                                    draggingIndex = target.index

                                    // 6) 스크롤 자동화 (선택)
                                    scope.launch {
                                        // 화면 끝 가까이에서 자동 스크롤
                                        val viewport = layoutInfo.viewportSize
                                        if (centerY < 100) listState.scrollBy(-100f)
                                        else if (centerY > viewport - 100) listState.scrollBy(100f)
                                    }
                                }
                            }
                        )
                    }
            ) {
                Text(text = item)
            }
        }
    }
}
