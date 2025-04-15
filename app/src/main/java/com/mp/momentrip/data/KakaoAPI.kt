package com.mp.momentrip.data

data class KakaoSearchResponse(
    val documents: List<KakaoPlace>,
    val meta: Meta
)

data class KakaoPlace(
    val place_name: String,
    val category_name: String,
    val address_name: String,
    val phone: String,
    val x: String, // longitude
    val y: String  // latitude
)

data class KakaoImageSearchResponse(
    val documents: List<KakaoImage>,
    val meta: Meta
)

data class KakaoImage(
    val image_url: String,
    val thumbnail_url: String,
    val width: Int,
    val height: Int
)


data class Meta(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean
)

val categoryCode = mapOf(
    "대형마트" to "MT1",
    "편의점" to "CS2",
    "어린이집" to "PS3",
    "유치원" to "PS3",
    "학교" to "SC4",
    "학원" to "AC5",
    "주차장" to "PK6",
    "주유소" to "OL7",
    "충전소" to "OL7",
    "지하철역" to "SW8",
    "은행" to "BK9",
    "문화시설" to "CT1",
    "중개업소" to "AG2",
    "공공기관" to "PO3",
    "관광명소" to "AT4",
    "숙박" to "AD5",
    "음식점" to "FD6",
    "카페" to "CE7",
    "병원" to "HP8",
    "약국" to "PM9"
)