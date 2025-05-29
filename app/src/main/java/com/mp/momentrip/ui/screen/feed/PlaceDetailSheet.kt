package com.mp.momentrip.ui.screen.feed

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.R
import com.mp.momentrip.data.Place
import com.mp.momentrip.ui.components.ImageCard
import com.mp.momentrip.ui.components.LikeButton
import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.launch

@Suppress("UnusedModifier")
@Composable
fun PlaceDetailBottomSheet(
    userState: UserViewModel,
    place: Place,
    cardSize: Dp,
    imageHeight: Dp,    // renamed from baseImageHeight
    imageAlpha: Float,
    textAlpha: Float,
    onClose: () -> Unit = {}
) {
    val density = LocalDensity.current
    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val baseImageHeightPx = with(density) { imageHeight.toPx() }

    // Offset for drag and close animation
    val offsetY = remember { Animatable(0f) }
    var isClosing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Closing animation
    LaunchedEffect(isClosing) {
        if (isClosing) {
            offsetY.animateTo(screenHeightPx, tween(300))
            onClose()
        }
    }

    // Calculate dynamic image height (base + drag)
    val currentImgPx = (baseImageHeightPx + offsetY.value).coerceAtLeast(baseImageHeightPx)
    val dynamicImageHeight = with(density) { currentImgPx.toDp() }

    Box(Modifier.fillMaxSize()) {
        // 상단 이미지 (확대 적용)
        ImageCard(
            imageUrl = place.firstImage ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .height(dynamicImageHeight)
                .graphicsLayer { alpha = imageAlpha }
        )

        // Bottom Sheet (항상 이미지 바로 아래)
        Column(
            modifier = Modifier
                // Drag gesture: offsetY 값만 변경
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { _, dragAmt ->
                            scope.launch {
                                val newY = (offsetY.value + dragAmt).coerceAtLeast(0f)
                                offsetY.snapTo(newY)
                            }
                        },
                        onDragEnd = {
                            scope.launch {
                                offsetY.animateTo(0f, tween(300))
                            }
                        }
                    )
                }
                // 이미지 바로 아래
                .offset(y = dynamicImageHeight)
                .fillMaxWidth()
                .height(cardSize)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp)
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .graphicsLayer { alpha = textAlpha }
        ) {
            var selectedTab by remember { mutableStateOf(DetailTab.Description) }

            // 닫기 버튼
            Box(
                Modifier
                    .size(27.dp)
                    .align(Alignment.End)
                    .clickable {
                        scope.launch { isClosing = true }
                    }
            )

            LikeButton(userState, place)

            Text(
                text = place.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF121212)
            )
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = place.addr1 ?: "주소 정보 없음",
                    fontSize = 12.sp,
                    color = Color(0xFF6F7789)
                )
            }
            Spacer(Modifier.height(8.dp))

            Text(
                text = "# ${place.cat1 ?: "카테고리 없음"}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6F7789)
            )
            Spacer(Modifier.height(20.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabItem("상세정보", selectedTab == DetailTab.Detail) { selectedTab = DetailTab.Detail }
                TabItem("설명",     selectedTab == DetailTab.Description) { selectedTab = DetailTab.Description }
            }
            Spacer(Modifier.height(16.dp))

            when (selectedTab) {
                DetailTab.Detail -> PlaceDetailContent(place, selectedTab)
                DetailTab.Description -> {
                    Text(text = "소개글", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = place.overview ?: "설명 정보 없음",
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceDetailContent(place: Place, selectedTab: DetailTab) {
    Column(modifier = Modifier.fillMaxWidth()) {
        when (selectedTab) {
            DetailTab.Description -> {
                Text(
                    text = "소개글",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF121212)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = place.overview ?: "정보 없음",
                    fontSize = 12.sp,
                    color = Color(0xFF6F7789),
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Justify
                )
            }

            DetailTab.Detail -> {
                when (place.contentTypeId) {
                    14 -> CultureContent(place)
                    15 -> FestivalContent(place)
                    28 -> LeportsContent(place)
                    32 -> LodgingContent(place)
                    38 -> ShoppingContent(place)
                    39 -> FoodContent(place)
                    else -> DefaultDetailContent(place)
                }
            }
        }
    }
}

@Composable
fun CultureContent(place: Place) {
    DetailItem("문의 및 안내", place.infocenterculture)
    DetailItem("주차 시설", place.parkingculture)
    DetailItem("이용 요금", place.usefee)
    DetailItem("쉬는 날", place.restdateculture)
    DetailItem("이용 시간", place.usetimeculture)
    DetailItem("관람 소요 시간", place.spendtime)
}

@Composable
fun FestivalContent(place: Place) {
    DetailItem("행사 장소", place.eventplace)
    DetailItem("행사 기간", "${place.eventstartdate ?: ""} ~ ${place.eventenddate ?: ""}")
    DetailItem("예매처", place.bookingplace)
    DetailItem("공연 시간", place.playtime)
    DetailItem("프로그램", place.program)
    DetailItem("이용 요금", place.usetimefestival)
}

@Composable
fun LeportsContent(place: Place) {
    DetailItem("문의 및 안내", place.infocenterleports)
    DetailItem("주차 시설", place.parkingleports)
    DetailItem("입장료", place.usefeeleports)
    DetailItem("이용 시간", place.usetimeleports)
    DetailItem("개장 기간", place.openperiod)
    DetailItem("예약 안내", place.reservation)
}

@Composable
fun LodgingContent(place: Place) {
    DetailItem("입실/퇴실 시간", "${place.checkintime ?: ""} ~ ${place.checkouttime ?: ""}")
    DetailItem("객실 수", place.roomcount)
    DetailItem("객실 유형", place.roomtype)
    DetailItem("규모", place.scalelodging)
    DetailItem("예약 안내", place.reservationlodging)
    DetailItem("기타 부대시설", place.subfacility)
}

@Composable
fun ShoppingContent(place: Place) {
    DetailItem("문의 및 안내", place.infocentershopping)
    DetailItem("영업 시간", place.opentime)
    DetailItem("판매 품목", place.saleitem)
    DetailItem("판매 품목 가격", place.saleitemcost)
    DetailItem("규모", place.scaleshopping)
    DetailItem("매장 안내", place.shopguide)
}

@Composable
fun FoodContent(place: Place) {
    DetailItem("대표 메뉴", place.firstmenu)
    DetailItem("취급 메뉴", place.treatmenu)
    DetailItem("영업 시간", place.opentimefood)
    DetailItem("쉬는 날", place.restdatefood)
    DetailItem("할인 정보", place.discountinfofood)
}

@Composable
fun DefaultDetailContent(place: Place) {
    DetailItem("문의 및 안내", place.infocenter)
    DetailItem("주차 시설", place.parking)
    DetailItem("이용 시간", place.usetime)
    DetailItem("이용 시기", place.useseason)
    DetailItem("쉬는 날", place.restdate)
    DetailItem("체험 안내", place.expguide)
}

@Composable
fun DetailItem(title: String, value: String?) {
    if (!value.isNullOrBlank()) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF121212)
            )
            Text(
                text = value,
                fontSize = 12.sp,
                color = Color(0xFF6F7789),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun TabItem(title: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color(0xFF6F7789)
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(70.dp)
                    .height(2.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(3.dp))
            )
        } else {
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

enum class DetailTab {
    Detail, Description
}
