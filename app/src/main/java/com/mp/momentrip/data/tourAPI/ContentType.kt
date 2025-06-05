package com.mp.momentrip.data.tourAPI

enum class ContentType(val id: Int, val label: String) {
    TOURIST_SPOT(12, "관광지"),
    CULTURAL_FACILITY(14, "문화시설"),
    FESTIVAL_EVENT(15, "축제/공연/행사"),
    LEISURE_SPORTS(28, "레포츠"),
    ACCOMMODATION(32, "숙박"),
    SHOPPING(38, "쇼핑"),
    RESTAURANT(39, "음식점");

    companion object {
        private val map = ContentType.entries.associateBy(ContentType::id)

        fun fromId(id: Int): ContentType? = map[id]
    }
}
