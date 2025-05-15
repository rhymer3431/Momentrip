package com.mp.momentrip.data

import com.google.gson.annotations.SerializedName
import kotlin.String
import kotlin.text.toInt


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

enum class Category(val categoryName: String) {
    A01("자연"),
    A02("인문(문화/예술/역사)"),
    A03("레포츠"),
    A04("쇼핑"),
    A05("음식"),
    B02("숙박"),
    C01("추천코스"),

    A01010100("국립공원"),
    A01010200("도립공원"),
    A01010300("군립공원"),
    A01010400("산"),
    A01010500("자연생태관광지"),
    A01010600("자연휴양림"),
    A01010700("수목원"),
    A01010800("폭포"),
    A01010900("계곡"),
    A01011000("약수터"),
    A01011100("해안절경"),
    A01011200("해수욕장"),
    A01011300("섬"),
    A01011400("항구/포구"),
    A01011500("등대"),
    A01011600("호수"),
    A01011700("강"),
    A01011800("동굴"),

    A02010100("고궁"),
    A02010200("성"),
    A02010300("사찰"),
    A02010400("종교성지"),
    A02010500("사적지"),
    A02010600("기념탑/전망대"),
    A02010700("유적지"),
    A02020100("휴양림"),
    A02020200("수련장"),
    A02020300("관광농원"),
    A02020400("이색체험"),
    A02030100("산업관광지"),
    A02030200("발전소"),
    A02030300("광산"),
    A02030400("공장"),
    A02040100("문화원"),
    A02040200("전통공방"),
    A02040300("전통시장"),
    A02050100("박물관"),
    A02050200("미술관"),
    A02060100("종교시설"),
    A02060200("교회"),
    A02060300("성당"),
    A02070100("공연장"),
    A02070200("전시관"),

    A03010100("등산"),
    A03010200("자전거"),
    A03020100("래프팅"),
    A03020200("카약"),
    A03020300("서핑"),
    A03030100("패러글라이딩"),
    A03030200("열기구"),
    A03040100("스키"),
    A03040200("눈썰매"),
    A03040300("스케이트장"),
    A03040400("골프장"),

    A04010100("5일장"),
    A04010200("상설시장"),
    A04010300("백화점"),
    A04010400("면세점"),
    A04010500("아울렛"),
    A04010600("전통공예상점"),
    A04010700("특산물판매점"),

    A05020100("한식"),
    A05020200("양식"),
    A05020300("일식"),
    A05020400("중식"),
    A05020700("이색 음식점"),
    A05020900("카페"),
    A05021000("클럽"),

    B02010100("관광호텔"),
    B02010500("콘도미니엄"),
    B02010600("유스호스텔"),
    B02010700("펜션"),
    B02010900("모텔"),
    B02011000("민박"),
    B02011100("게스트하우스"),
    B02011200("홈스테이"),
    B02011300("서비스드레지던스"),
    B02011600("한옥");


    companion object {
        fun fromCode(code: String): Category? = entries.find { it.name == code }
    }
}

enum class FoodCategory(val code: String, val description: String) {
    KOREAN("A05020100", "한식"),
    WESTERN("A05020200", "양식"),
    JAPANESE("A05020300", "일식"),
    CHINESE("A05020400", "중식"),
    ASIAN("A05020500", "아시아식"),
    BUFFET("A05020600", "뷔페"),
    FAST_FOOD("A05020700", "패스트푸드"),
    CHICKEN("A05020800", "치킨"),
    PIZZA("A05020900", "피자"),
    SNACK("A05021000", "야식"),
    BAKERY("A05021100", "제과·제빵"),
    CAFE("A05021200", "카페"),
    OTHER("A05021300", "기타");

    companion object {
        fun fromCode(code: String): FoodCategory? = entries.find { it.code == code }
    }
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
)

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
)

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
    val areacode: String?,            // 지역코드 (optional)
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


data class DetailInfoItem(
    val contentId: String,           // 콘텐츠 ID
    val contentTypeId: Int,          // 콘텐츠 타입 ID (12: 관광지, 32: 숙박, 39: 음식점)

    // --- 관광지용 필드 (contentTypeId = 12) ---
    val accomCount: String? = null,       // 수용 인원
    val expGuide: String? = null,         // 체험 안내
    val heritage1: String? = null,        // 세계문화유산 여부
    val infoCenter: String? = null,       // 문의 및 안내
    val openDate: String? = null,         // 개장일
    val parking: String? = null,          // 주차 시설
    val restDate: String? = null,         // 쉬는 날
    val useTime: String? = null,          // 이용 시간

    // --- 숙박용 필드 (contentTypeId = 32) ---
    val checkInTime: String? = null,      // 체크인 시간
    val checkOutTime: String? = null,     // 체크아웃 시간
    val chkCooking: String? = null,       // 객실 내 취사 가능 여부
    val infoCenterLodging: String? = null,// 문의 및 안내
    val parkingLodging: String? = null,   // 주차 시설
    val roomCount: String? = null,        // 객실 수
    val roomType: String? = null,         // 객실 유형
    val reservationUrl: String? = null,   // 예약 안내 홈페이지
    val subFacility: String? = null,      // 기타 부대시설
    val refundRegulation: String? = null, // 환불 규정

    // --- 음식점용 필드 (contentTypeId = 39) ---
    val firstMenu: String? = null,        // 대표 메뉴
    val treatMenu: String? = null,        // 취급 메뉴
    val infoCenterFood: String? = null,   // 문의 및 안내
    val openDateFood: String? = null,     // 개업일
    val openTimeFood: String? = null,     // 영업 시간
    val restDateFood: String? = null,     // 쉬는 날
    val parkingFood: String? = null,      // 주차 시설
)
