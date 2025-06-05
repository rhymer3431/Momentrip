package com.mp.momentrip.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mp.momentrip.R
import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.place.Region
import com.mp.momentrip.data.place.dummy_place
import com.mp.momentrip.data.tourAPI.Category
import com.mp.momentrip.view.UserViewModel

@Composable
fun PlaceCard(
    place: Place,
    modifier: Modifier = Modifier,  // ✅ 외부에서 전달 가능하게
    onClick: () -> Unit
) {
    Box(
        modifier = modifier // ✅ 외부 modifier 사용
            .size(width = 161.dp, height = 214.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0x1FB4BCC9),
                spotColor = Color(0x1FB4BCC9)
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(width = 137.dp, height = 124.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                ImageCard(place.firstImage)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .width(137.dp)
                    .height(68.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Region.fromCode(place.areaCode!!.toInt())!!.locationName,
                    fontSize = 12.sp,
                    letterSpacing = 0.3.sp,
                    color = Color(0xFF7D848D),
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = place.title,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp,
                    color = Color(0xFF1B1E28),
                    fontWeight = FontWeight.Medium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "#" + Category.fromCode(place.cat1!!)?.categoryName,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        color = Color(0xFF4F8EDA)
                    )
                    Text(
                        text = "#" + Category.fromCode(place.cat3!!)?.categoryName,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        color = Color(0xFF4F8EDA)
                    )
                }
            }
        }
    }
}
@Composable
fun TallPlaceCard(
    place: Place,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0x1FB4BCC9),
                spotColor = Color(0x1FB4BCC9)
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        // 이미지 상단
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            ImageCard(place.firstImage)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = Region.fromCode(place.areaCode!!.toInt())!!.locationName,
                fontSize = 12.sp,
                color = Color(0xFF7D848D),
                fontWeight = FontWeight.Normal
            )

            Text(
                text = place.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1B1E28),
                maxLines = 2
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                place.cat1?.let {
                    Category.fromCode(it)?.categoryName?.let { tag ->
                        Text(
                            text = "#$tag",
                            fontSize = 12.sp,
                            color = Color(0xFF4F8EDA)
                        )
                    }
                }
                place.cat3?.let {
                    Category.fromCode(it)?.categoryName?.let { tag ->
                        Text(
                            text = "#$tag",
                            fontSize = 12.sp,
                            color = Color(0xFF4F8EDA)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LongPlaceCard(
    place: Place,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(120.dp)
            .fillMaxWidth()           // 기본은 가로로 꽉 차도록, 필요 시 Modifier로 조정
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0x1FB4BCC9),
                spotColor  = Color(0x1FB4BCC9)
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            /* 왼쪽 썸네일 이미지 */
            Box(
                modifier = Modifier
                    .size(width = 96.dp, height = 96.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                ImageCard(place.firstImage)
            }

            /* 오른쪽 텍스트 블록 */
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                /* 상단 : 지역, 장소명 */
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = Region.fromCode(place.areaCode!!.toInt())!!.locationName,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        color = Color(0xFF7D848D)
                    )
                    Text(
                        text = place.title,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                        color = Color(0xFF1B1E28),
                        fontWeight = FontWeight.Medium,
                        maxLines = 2
                    )
                }
                /* 하단 : 태그 2개 */
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "#" + Category.fromCode(place.cat1!!)?.categoryName.orEmpty(),
                        fontSize = 12.sp,
                        color = Color(0xFF4F8EDA)
                    )
                    Text(
                        text = "#" + Category.fromCode(place.cat3!!)?.categoryName.orEmpty(),
                        fontSize = 12.sp,
                        color = Color(0xFF4F8EDA)
                    )
                }
            }
        }
    }
}

@Composable
fun MiniFeedPlaceCard(
    userState: UserViewModel,
    place: Place,
    modifier: Modifier = Modifier,  // ✅ 외부에서 전달 가능하게
    onClick: () -> Unit
) {
    Box(
        modifier = modifier // ✅ 외부 modifier 사용
            .size(width = 161.dp, height = 214.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0x1FB4BCC9),
                spotColor = Color(0x1FB4BCC9)
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(width = 137.dp, height = 124.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                ImageCard(place.firstImage)

                LikeButton(
                    userState = userState,
                    place = place,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(3.dp) // 여백 줘서 딱 붙지 않게
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .width(137.dp)
                    .height(68.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Region.fromCode(place.areaCode!!.toInt())!!.locationName,
                    fontSize = 12.sp,
                    letterSpacing = 0.3.sp,
                    color = Color(0xFF7D848D),
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )


                Text(
                    text = place.title,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp,
                    color = Color(0xFF1B1E28),
                    fontWeight = FontWeight.Medium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "#" + Category.fromCode(place.cat2!!)?.categoryName,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        color = Color(0xFF4F8EDA)
                    )
                    Text(
                        text = "#" + Category.fromCode(place.cat3!!)?.categoryName,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        color = Color(0xFF4F8EDA)
                    )
                }
            }
        }
    }
}

@Composable
fun LargePlaceCard(
    userState: UserViewModel,
    place: Place,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x1FB4BCC9),
                spotColor = Color(0x1FB4BCC9)
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        // 배경 이미지
        ImageCard(
            imageUrl = place.firstImage,
            modifier = Modifier.fillMaxSize()
        )

        // 오버레이 및 콘텐츠
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.4f to Color(0x91000000),
                        1f to Color(0xD6000000)
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 상단 좋아요 버튼
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    LikeButton(
                        userState = userState,
                        place = place,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(36.dp)
                    )
                }

                // 하단 정보 영역
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "지역",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = Region.fromCode(place.areaCode!!.toInt())?.locationName ?: "지역 미상",
                            fontSize = 14.sp,
                            color = Color.White,
                            letterSpacing = 0.3.sp
                        )
                    }

                    Text(
                        text = place.title,
                        fontSize = 26.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        place.cat2?.let {
                            Text(
                                text = "#" + Category.fromCode(it)?.categoryName,
                                fontSize = 14.sp,
                                color = Color(0xFFB8DAFF),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        place.cat3?.let {
                            Text(
                                text = "#" + Category.fromCode(it)?.categoryName,
                                fontSize = 14.sp,
                                color = Color(0xFFB8DAFF),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostCreatePlaceCard(
    place: Place,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x1FB4BCC9),
                spotColor = Color(0x1FB4BCC9)
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        // 대표 이미지
        ImageCard(
            imageUrl = place.firstImage,
            modifier = Modifier.fillMaxSize()
        )

        // 오버레이 정보
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.4f to Color(0x91000000),
                        1f to Color(0xD6000000)
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                // 지역 정보
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "지역",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = place.areaCode?.toInt()?.let { Region.fromCode(it)?.locationName } ?: "지역 미상",
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // 장소명
                Text(
                    text = place.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 카테고리
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    place.cat2?.let {
                        Text(
                            text = "#" + Category.fromCode(it)?.categoryName.orEmpty(),
                            fontSize = 13.sp,
                            color = Color(0xFFB8DAFF),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    place.cat3?.let {
                        Text(
                            text = "#" + Category.fromCode(it)?.categoryName.orEmpty(),
                            fontSize = 13.sp,
                            color = Color(0xFFB8DAFF),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun TallFeedPlaceCard(
    userState: UserViewModel,
    place: Place,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth(0.48f) // 🔹 부모의 약 48% 너비 (2열 카드 뷰 기준)
            .aspectRatio(0.6f)   // 🔹 세로형 카드 비율 (0.6 = width:height → 10:16.6)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color(0x1FB4BCC9),
                    spotColor = Color(0x1FB4BCC9)
                )
                .background(Color.White, RoundedCornerShape(16.dp))
                .clickable { onClick() }
                .padding(14.dp)
        ) {
            // 🔹 이미지 + 좋아요 버튼
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f) // 🔹 이미지 영역: 전체 높이의 약 65%
                    .clip(RoundedCornerShape(12.dp))
            ) {
                ImageCard(place.firstImage)

                LikeButton(
                    userState = userState,
                    place = place,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f) // 🔹 텍스트 영역: 나머지 35%
            ) {
                Text(
                    text = Region.fromCode(place.areaCode!!.toInt())!!.locationName,
                    fontSize = 12.sp,
                    color = Color(0xFF7D848D),
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = place.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1B1E28),
                    maxLines = 2,
                    lineHeight = 20.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    place.cat2?.let {
                        Category.fromCode(it)?.categoryName?.let { tag ->
                            Text(
                                text = "#$tag",
                                fontSize = 12.sp,
                                color = Color(0xFF4F8EDA)
                            )
                        }
                    }
                    place.cat3?.let {
                        Category.fromCode(it)?.categoryName?.let { tag ->
                            Text(
                                text = "#$tag",
                                fontSize = 12.sp,
                                color = Color(0xFF4F8EDA)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LargePlaceCardPreview(){
    LargePlaceCard(
        userState = UserViewModel(),
        place = dummy_place,
        onClick = {}
    )
}
@Preview(showBackground = true)
@Composable
fun TallPlaceCardPreview() {
    val fakePlace = Place(
        contentId = 1,
        title = "서울숲 감성 카페",
        firstImage = "",  // 실제 이미지 URL 또는 빈 문자열
        areaCode = "1",
        cat1 = "A01",
        cat2 = "A0101",
        cat3 = "A01010400"
        // 필요한 필드는 적절히 채우세요
    )

    val fakeUserState = UserViewModel().apply {
        // 테스트용 초기화 필요 시 여기에
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(4) { // 예시 카드 4개
            TallFeedPlaceCard(
                userState = fakeUserState,
                place = fakePlace,
                onClick = {}
            )
        }
    }
}


@Composable
fun FeedPlaceCard(
    userState: UserViewModel,
    place: Place,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .width(200.dp)
            .height(230.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // 🖼 이미지 + 북마크
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)) // ✅ 상단만 둥글게
                    .background(Color.LightGray),
                contentAlignment = Alignment.TopEnd
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(place.firstImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.restuarant) // 기본 이미지 리소스
                )

                LikeButton(
                    userState = userState,
                    place = place,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // 📝 텍스트 정보
            Text(
                text = place.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B1E28),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Text(
                text = Region.fromCode(place.areaCode!!.toInt())?.locationName ?: "",
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )

            // 🏷️ 카테고리 해시태그
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            ) {
                listOfNotNull(
                    Category.fromCode(place.cat2!!)?.categoryName,
                    Category.fromCode(place.cat3!!)?.categoryName
                ).forEach {
                    Text(
                        text = "#$it",
                        fontSize = 12.sp,
                        color = Color(0xFF4F8EDA),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedPlaceCardPreview() {
    val fakePlace = Place(
        contentId = 123,
        title = "서울숲 감성 카페",
        firstImage = "", // 또는 "https://example.com/image.jpg"
        areaCode = "1",
        cat1 = "A01",
        cat2 = "A0101",
        cat3 = "A01010400"
    )

    val fakeUserState = UserViewModel() // ViewModel이 아닌 경우엔 Preview 용 dummy로 조정 필요

    FeedPlaceCard(
        userState = fakeUserState,
        place = fakePlace,
        onClick = {}
    )
}
