package com.mp.momentrip.data

import com.google.gson.annotations.SerializedName


// 카테고리 코드만 정의하는 Enum 클래스

enum class ContentTypeId(val code: String) {
    TOURIST_SPOT("12"),       // 관광지
    CULTURAL_FACILITY("14"), // 문화시설
    EVENT("15"),             // 행사/공연/축제
    TRAVEL_COURSE("25"),     // 여행코스
    LEISURE_SPORTS("28"),    // 레포츠
    ACCOMMODATION("32"),     // 숙박
    SHOPPING("38"),          // 쇼핑
    RESTAURANT("39");        // 음식점
}

enum class Category(val categoryName: String, val rnum: Int) {
    A01("자연", 1),
    A02("인문(문화/예술/역사)", 2),
    A03("레포츠", 3),
    A04("쇼핑", 4),
    A05("음식", 5),
    B02("숙박", 6),
    C01("추천코스", 7);

    // You can still access the default name property if needed
    // by using it directly: this.name
}



data class ApiResponse<T>(
    @SerializedName("response") val response: Response<T>
)

data class Response<T>(
    @SerializedName("header") val header: Header,
    @SerializedName("body") val body: Body<T>
)

data class Header(
    val resultCode: String,
    val resultMsg: String
)

data class Body<T>(
    val items: Items<T>?,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

data class Items<T>(
    val item: List<T>?
)

data class AreaCode(
    val code: String,
    val name: String,
    val rnum: Int
)

data class CategoryCode(
    val code: String,
    val name: String,
    val rnum: Int
)

data class CommonInfo(
    val contentId: String,
    val contentTypeId: String,
    val title: String,
    val address: String,
    val roadAddress: String,
    val tel: String,
    val firstImage: String,
    val firstImage2: String,
    val areaCode: String,
    val sigunguCode: String,
    val cat1: String,
    val cat2: String,
    val cat3: String,
    val mapX: Double,
    val mapY: Double,
    val mlevel: String,
    val overview: String
)

data class IntroInfo(
    val contentId: String,
    val contentTypeId: String,
    // 관광지(12) 관련 필드
    val accomodationCount: String? = null,
    val parking: String? = null,
    // 문화시설(14) 관련 필드
    val scale: String? = null,
    // 행사/공연/축제(15) 관련 필드
    val eventStartDate: String? = null,
    val eventEndDate: String? = null,
    // 여행코스(25) 관련 필드
    val distance: String? = null,
    // 레포츠(28) 관련 필드
    val openPeriod: String? = null,
    // 숙박(32) 관련 필드
    val checkinTime: String? = null,
    // 쇼핑(38) 관련 필드
    val saleItem: String? = null,
    // 음식점(39) 관련 필드
    val openTime: String? = null
)

data class DetailInfo(
    val contentId: String,
    val contentTypeId: String,
    val infoname: String,
    val infotext: String,
    val serialnum: Int
)

data class ImageInfo(
    val contentId: String,
    val originImgurl: String,
    val smallImgurl: String,
    val imgname: String,
    val serialnum: Int
)

data class AreaBasedItem(
    val addr1: String?, // 주소
    val addr2: String?,           // 상세주소
    val areacode: String?, // 지역 코드
    val cat1: String?, // 대분류 코드
    val cat2: String?, // 중분류 코드
    val cat3: String?, // 소분류 코드
    val contentid: Int, // 콘텐츠 ID
    val contenttypeid: Int, // 콘텐츠 타입 ID
    val firstimage: String?, // 대표 이미지 (원본)
    val firstimage2: String?, // 대표 이미지 (썸네일)
    val mapx: Double?, // GPS X좌표
    val mapy: Double?, // GPS Y좌표
    val sigungucode: Int?, // 시군구 코드
    val tel: String?, // 전화번호
    val title: String, // 콘텐츠 제목
){
    fun toPlace(): Place=Place(
        title = title,
        contentTypeId = contenttypeid,
        addr1 = addr1,
        addr2 = addr2,
        areaCode = areacode,
        cat1 = cat1,
        cat2 = cat2,
        cat3 = cat3,
        firstImage = firstimage,
        firstImage2 = firstimage2,
        x = mapx!!,
        y = mapy!!,
        tel = tel
    )
}

data class LocationBasedItem(
    val addr1: String?,            // 주소
    val addr2: String?,            // 상세주소
    val areacode: Int?,            // 지역코드
    val cat1: String?,             // 대분류
    val cat2: String?,             // 중분류
    val cat3: String?,             // 소분류
    val contentid: Int,            // 콘텐츠ID
    val contenttypeid: Int,        // 콘텐츠타입ID
    val createdtime: String,       // 콘텐츠최초등록일
    val dist: Double?,             // 중심좌표로부터거리
    val firstimage: String?,       // 대표이미지(원본)
    val firstimage2: String?,      // 대표이미지(썸네일)
    val mapx: Double?,             // GPS X좌표
    val mapy: Double?,             // GPS Y좌표
    val sigungucode: Int?,         // 시군구코드
    val tel: String?,              // 전화번호
    val title: String              // 콘텐츠제목
)

data class KeywordSearchItem(
    val title: String,              // 콘텐츠제목
    val addr1: String?,            // 주소
    val addr2: String?,            // 상세주소
    val areacode: String?,            // 지역코드
    val cat1: String?,             // 대분류
    val cat2: String?,             // 중분류
    val cat3: String?,             // 소분류
    val contentid: Int,            // 콘텐츠ID
    val contenttypeid: Int,        // 콘텐츠타입ID
    val firstimage: String?,       // 대표이미지(원본)
    val firstimage2: String?,      // 대표이미지(썸네일)
    val mapx: Double?,             // GPS X좌표
    val mapy: Double?,             // GPS Y좌표
    val sigungucode: Int?,         // 시군구코드
    val tel: String?              // 전화번호

){
    fun toPlace(): Place=Place(
        title = title,
        contentTypeId = contenttypeid,
        addr1 = addr1,
        addr2 = addr2,
        areaCode = areacode,
        cat1 = cat1,
        cat2 = cat2,
        cat3 = cat3,
        firstImage = firstimage,
        firstImage2 = firstimage2,
        x = mapx!!,
        y = mapy!!,
        tel = tel
    )
}

data class FestivalItem(
    val addr1: String?,            // 주소
    val addr2: String?,            // 상세주소
    val areacode: Int?,            // 지역코드
    val cat1: String?,             // 대분류
    val cat2: String?,             // 중분류
    val cat3: String?,             // 소분류
    val contentid: Int,            // 콘텐츠ID
    val contenttypeid: Int,        // 콘텐츠타입ID
    val eventstartdate: String?,   // 행사시작일
    val eventenddate: String?,     // 행사종료일
    val firstimage: String?,       // 대표이미지(원본)
    val firstimage2: String?,      // 대표이미지(썸네일)
    val mapx: Double?,             // GPS X좌표
    val mapy: Double?,             // GPS Y좌표
    val sigungucode: Int?,         // 시군구코드
    val tel: String?,              // 전화번호
    val title: String              // 콘텐츠제목
)

data class StayItem(
    val addr1: String?,            // 주소
    val addr2: String?,            // 상세주소
    val areacode: Int?,            // 지역코드
    val cat1: String?,             // 대분류
    val cat2: String?,             // 중분류
    val cat3: String?,             // 소분류
    val contentid: Int,            // 콘텐츠ID
    val contenttypeid: Int,        // 콘텐츠타입ID
    val createdtime: String,       // 등록일
    val firstimage: String?,       // 대표이미지(원본)
    val firstimage2: String?,      // 대표이미지(썸네일)
    val goodstay: Int?,            // 굿스테이여부
    val hanok: Int?,               // 한옥여부
    val mapx: Double?,             // GPS X좌표
    val mapy: Double?,             // GPS Y좌표
    val sigungucode: Int?,         // 시군구코드
    val tel: String?,              // 전화번호
    val title: String              // 콘텐츠제목
)

data class ContentDetailItem(
    val contentid: Int,            // 콘텐츠ID
    val contenttypeid: Int,        // 콘텐츠타입ID
    val createdtime: String,       // 등록일
    val hmpg: String?,             // 홈페이지주소 (optional)
    val tel: String?,              // 전화번호 (optional)
    val title: String,             // 콘텐츠명 (제목)
    val firstimage: String?,       // 대표이미지(원본) (optional)
    val firstimage2: String?,      // 대표이미지(썸네일) (optional)
    val areacode: Int?,            // 지역코드 (optional)
    val sigungucode: String?,      // 시군구코드 (optional)
    val cat1: String?,             // 대분류 (optional)
    val cat2: String?,             // 중분류 (optional)
    val cat3: String?,             // 소분류 (optional)
    val addr1: String?,            // 주소 (optional)
    val addr2: String?,            // 상세주소 (optional)
    val zipcode: String?,          // 우편번호 (optional)
    val mapx: Double?,             // GPS X좌표 (optional)
    val mapy: Double?,             // GPS Y좌표 (optional)
    val overview: String?          // 개요 (optional)
)
