package com.mp.momentrip.ui.screen.community

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.mp.momentrip.data.community.Post
import com.mp.momentrip.ui.components.PostCard
import com.mp.momentrip.view.CommunityViewModel
import com.mp.momentrip.view.PostSortOption
import com.mp.momentrip.view.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    userViewModel: UserViewModel,
    viewModel: CommunityViewModel,
    onAddPostClick: () -> Unit,
    onPostClick: (Post) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.refreshPosts()
    }

    val postList by viewModel.postList.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val likedIds by viewModel.likedPostIds.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()

    var filterLikedOnly by remember { mutableStateOf(false) }
    val filteredPosts = remember(postList, likedIds, filterLikedOnly) {
        if (filterLikedOnly) postList.filter { likedIds.contains(it.id) } else postList
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("글쓰기") },
                onClick = onAddPostClick,
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            )

        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refreshPosts() },
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                CommunityHeader(
                    sortOption = sortOption,
                    onSortSelected = viewModel::setSortOption,
                    filterLikedOnly = filterLikedOnly,
                    onFilterLikedOnlyChanged = { filterLikedOnly = it }
                )

                if (filteredPosts.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(top = 100.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("표시할 게시글이 없습니다", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        TextButton(onClick = onAddPostClick) {
                            Text("첫 게시글 작성하기", style = MaterialTheme.typography.labelLarge)
                        }
                    }

                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp)
                    ) {
                        items(filteredPosts) { post ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.background)
                                    .fillMaxWidth()
                                    .clickable { onPostClick(post) }
                            ){
                                PostCard(
                                    userViewModel = userViewModel,
                                    communityViewModel = viewModel,
                                    post = post,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .clickable { onPostClick(post) },
                                    onCardClick = { onPostClick(it) },
                                    onLikeClick = {
                                        viewModel.toggleLike(it.id, FirebaseAuth.getInstance().currentUser?.uid!!)
                                    },
                                    onDeletePost = {
                                        viewModel.deletePost(it.id)
                                        viewModel.refreshPosts()
                                    }
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SortDropdown(
    sortOption: PostSortOption,
    onOptionSelected: (PostSortOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val label = when (sortOption) {
        PostSortOption.LATEST -> "최신순"
        PostSortOption.MOST_LIKED -> "인기순"
    }

    Box {
        AnimatedContent(
            targetState = label,
            transitionSpec = {
                fadeIn(tween(150)) with fadeOut(tween(150))
            },
            modifier = Modifier
                .clickable { expanded = true }
                .background(Color(0xFFF6F6F6), RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)

        ) { text ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text, fontSize = 14.sp, color = Color.Black)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Black)
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text("최신순") },
                onClick = {
                    onOptionSelected(PostSortOption.LATEST)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("인기순") },
                onClick = {
                    onOptionSelected(PostSortOption.MOST_LIKED)
                    expanded = false
                }
            )
        }
    }
}
@Composable
fun CommunityHeader(
    sortOption: PostSortOption,
    onSortSelected: (PostSortOption) -> Unit,
    filterLikedOnly: Boolean,
    onFilterLikedOnlyChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
            )
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 1) 왼쪽: 커뮤니티 제목
        Text(
            text = "커뮤니티",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        // 2) 오른쪽: 정렬 드롭다운 + 좋아요한 글 토글
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 정렬 드롭다운
            SortDropdown(
                sortOption = sortOption,
                onOptionSelected = onSortSelected
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 좋아요한 글만 토글
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "좋아요한 글만",
                    fontSize = 14.sp,
                    color = Color(0xFF555555)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Switch(
                    checked = filterLikedOnly,
                    onCheckedChange = onFilterLikedOnlyChanged,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF4A90E2),
                        uncheckedThumbColor = Color(0xFFBDBDBD)
                    )
                )
            }
        }
    }
}
