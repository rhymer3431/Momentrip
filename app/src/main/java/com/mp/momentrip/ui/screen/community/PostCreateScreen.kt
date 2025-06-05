package com.mp.momentrip.ui.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.mp.momentrip.data.community.Post
import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.user.User
import com.mp.momentrip.ui.components.LongPlaceCard
import com.mp.momentrip.ui.components.PostCreatePlaceCard
import com.mp.momentrip.view.PlaceViewModel
import com.mp.momentrip.view.UserViewModel

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun PostCreateScreen(
    userState:UserViewModel,
    placeViewModel: PlaceViewModel,
    onPlaceSelectClick: () -> Unit,
    onPostSubmit: (Post) -> Unit,
    onCancel: () -> Unit
) {
    var sheetVisible by remember { mutableStateOf(false) }
    val selectedPlace by placeViewModel.selectedPlace.collectAsState()
    var description by remember { mutableStateOf("") }
    var tagInput    by remember { mutableStateOf("") }
    var tags        by remember { mutableStateOf<List<String>>(emptyList()) }

    val primary = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface
    val corner  = RoundedCornerShape(12.dp)

    /* ─── 상단 앱바 (X · 공유) ─── */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("새 게시물") },
                navigationIcon = {
                    IconButton(onCancel) { Icon(Icons.Default.Close, contentDescription = null) }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@TextButton
                            val post = Post(
                                id = "",
                                author = User(id = uid),
                                place = selectedPlace!!,
                                description = description,
                                tags = tags
                            )
                            onPostSubmit(post)

                        },
                        enabled = selectedPlace != null && description.isNotBlank()
                    ) { Text("공유") }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            /* ── 1. 장소 카드 (Instagram의 이미지 영역 대체) ── */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.3f)
                    .clip(corner)
                    .background(Color.LightGray.copy(alpha = 0.3f))
                    .clickable { sheetVisible = true},
                contentAlignment = Alignment.Center
            ) {
                if (selectedPlace == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, null, tint = primary, modifier = Modifier.size(48.dp))
                        Text("장소 추가", color = primary)
                    }
                } else {
                    /* 실제 앱에서는 장소 대표 사진을 AsyncImage 로 불러오면 된다 */

                    PostCreatePlaceCard(
                        place = selectedPlace!!,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.4f),
                        onClick = { sheetVisible = true }
                    )

                }
            }

            Spacer(Modifier.height(24.dp))

            /* ── 2. 설명 (테두리 없는 멀티라인 입력) ── */
            BasicTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
            ) { inner ->
                if (description.isEmpty())
                    Text("문구 입력...", color = Color.Gray)
                inner()
            }

            Spacer(Modifier.height(24.dp))

            /* ── 3. 태그 Chips + 추가 입력 ── */
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement   = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    SuggestionChip(
                        onClick = { tags = tags - tag },
                        label = { Text("#$tag ✕") }
                    )
                }
                AssistChip(
                    onClick = { /* no-op: 포커스 이동용 */ },
                    label = {
                        Row {
                            BasicTextField(
                                value = tagInput,
                                onValueChange = { tagInput = it },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodySmall
                            )
                            if (tagInput.isEmpty())
                                Text("태그 추가", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (tagInput.isNotBlank()) {
                                    tags = tags + tagInput.trim()
                                    tagInput = ""
                                }
                            }
                        ) { Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp)) }
                    }
                )
            }

            Spacer(Modifier.height(60.dp))   // 하단 여유
        }
    }
    PlaceSelectBottomSheet(
        userState = userState,
        visible = sheetVisible,
        onPlaceSelected = {placeViewModel.setPlace(it)},
        onDismiss = {
            sheetVisible = false               // X 버튼 등에서 닫기
        }
    )
}
/* ----------------------------------------------------------------
   PlaceSelectBottomSheet.kt
---------------------------------------------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceSelectBottomSheet(
    userState: UserViewModel,
    visible: Boolean,
    onPlaceSelected: (Place) -> Unit,
    onDismiss: () -> Unit
) {
    if (!visible) return                         // 열려있어야만 그린다

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val user by userState.user.collectAsState()
    var query by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            /* ───── 제목 & 닫기 ───── */
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "장소 선택",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }

            Spacer(Modifier.height(12.dp))

            /* ───── 검색 입력 ───── */
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder   = { Text("검색") },
                trailingIcon  = {
                    if (query.isNotEmpty()) {
                        IconButton({ query = "" }) {
                            Icon(Icons.Default.Clear, null)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            /* ───── 장소 목록 ───── */
            val filtered = remember(query, user) {
                user?.liked.orEmpty()
                    .filter { place ->
                        query.isBlank() ||
                                place?.title?.contains(query, ignoreCase = true) == true
                    }
            }

            if (filtered.isEmpty()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("표시할 장소가 없습니다.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 40.dp)
                ) {
                    items(
                        filtered,
                        key = { it!!.contentId}          // 성능 최적화
                    ) { place ->
                        LongPlaceCard(
                            place = place!!,
                            onClick = {
                                onPlaceSelected(place)
                                onDismiss()      // 선택 후 바로 닫기
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}


