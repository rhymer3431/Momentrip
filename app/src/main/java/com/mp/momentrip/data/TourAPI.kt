package com.mp.momentrip.data

import com.google.gson.annotations.SerializedName


// 카테고리 코드만 정의하는 Enum 클래스

enum class ContentType(val id: Int, val label: String) {
    TOURIST_SPOT(12, "관광지"),
    CULTURAL_FACILITY(14, "문화시설"),
    FESTIVAL_EVENT(15, "축제/공연/행사"),
    TRAVEL_COURSE(25, "여행코스"),
    LEISURE_SPORTS(28, "레포츠"),
    ACCOMMODATION(32, "숙박"),
    SHOPPING(38, "쇼핑"),
    RESTAURANT(39, "음식점");

    companion object {
        private val map = ContentType.entries.associateBy(ContentType::id)

        fun fromId(id: Int): ContentType? = map[id]
    }
}

enum class Category(val categoryName: String) {
    A01("자연"),
    A0101("자연관광지"),
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
    A0102("관광자원"),
    A01020100("희귀동.식물"),
    A01020200("기암괴석"),

    A02("인문(문화/예술/역사)"),
    A0201("역사관광지"),
    A02010100("고궁"),
    A02010200("성"),
    A02010300("문"),
    A02010400("고택"),
    A02010500("생가"),
    A02010600("민속마을"),
    A02010700("유적지/사적지"),
    A02010800("사찰"),
    A02010900("종교성지"),
    A02011000("안보관광"),

    A0202("휴양관광지"),
    A02020200("관광단지"),
    A02020300("온천/욕장/스파"),
    A02020400("이색찜질방"),
    A02020500("헬스투어"),
    A02020600("테마공원"),
    A02020700("공원"),
    A02020800("유람선/잠수함관광"),

    A0203("체험관광지"),
    A02030100("농.산.어촌 체험"),
    A02030200("전통체험"),
    A02030300("산사체험"),
    A02030400("이색체험"),
    A02030600("이색거리"),

    A0204("산업관광지"),
    A02040400("발전소"),
    A02040600("식음료"),
    A02040800("기타"),
    A02040900("전자-반도체"),
    A02041000("자동차"),

    A0205("건축/조형물"),
    A02050100("다리/대교"),
    A02050200("기념탑/기념비/전망대"),
    A02050300("분수"),
    A02050400("동상"),
    A02050500("터널"),
    A02050600("유명건물"),

    A0206("문화시설"),
    A02060100("박물관"),
    A02060200("기념관"),
    A02060300("전시관"),
    A02060400("컨벤션센터"),
    A02060500("미술관/화랑"),
    A02060600("공연장"),
    A02060700("문화원"),
    A02060800("외국문화원"),
    A02060900("도서관"),
    A02061000("대형서점"),

    A0207("축제"),
    A02070100("문화관광축제"),
    A02070200("일반축제"),

    A0208("공연/행사"),
    A02080100("전통공연"),
    A02080200("연극"),
    A02080300("뮤지컬"),
    A02080400("오페라"),
    A02080500("전시회"),
    A02080600("박람회"),
    A02080800("무용"),
    A02080900("클래식음악회"),
    A02081000("대중콘서트"),
    A02081100("영화"),

    A03("레포츠"),
    A0301("레포츠소개"),
    A03010200("수상레포츠"),
    A03010300("항공레포츠"),

    A0302("육상 레포츠"),
    A03020200("수련시설"),
    A03020300("경기장"),
    A03020400("인라인(실내 인라인 포함)"),
    A03020500("자전거하이킹"),
    A03020600("카트"),
    A03020700("골프"),
    A03020800("경마"),
    A03020900("경륜"),
    A03021000("카지노"),
    A03021100("승마"),

    A0303("수상 레포츠"),
    A03030100("윈드서핑/제트스키"),
    A03030200("카약/카누"),
    A03030300("요트"),
    A03030400("스노쿨링/스킨스쿠버다이빙"),
    A03030500("민물낚시"),
    A03030600("바다낚시"),
    A03030700("수영"),
    A03030800("래프팅"),

    A0304("항공 레포츠"),
    A03040100("스카이다이빙"),
    A03040200("초경량비행"),
    A03040300("헹글라이딩/패러글라이딩"),
    A03040400("열기구"),

    A0305("복합 레포츠"),
    A03050100("복합 레포츠"),

    A04("쇼핑"),
    A0401("쇼핑"),
    A04010100("5일장"),
    A04010200("상설 시장"),
    A04010300("백화점"),
    A04010400("면세점"),
    A04010500("대형마트"),
    A04010600("전문매장/상가"),
    A04010700("공예/공방"),
    A04010900("특산물 판매점"),
    A04011000("사후면세점"),
    A04011200("스키 렌탈샵"),

    A05("음식"),
    A0502("음식점"),
    A05020100("한식"),
    A05020200("양식"),
    A05020300("일식"),
    A05020400("중식"),
    A05020700("이색 음식점"),
    A05020900("카페 & 찻집"),
    A05021000("클럽"),

    B02("숙박"),
    B0201("숙박시설"),
    B02010100("관광호텔"),
    B02010500("콘도"),
    B02010600("유스호스텔"),
    B02010700("펜션"),
    B02010900("모텔"),
    B02011000("민박"),
    B02011100("게스트하우스"),
    B02011200("홈스테이"),
    B02011300("서비스드레지던스"),
    B02011600("한옥"),

    C01("추천코스"),
    C0112("가족코스"),
    C01120001("가족코스"),
    C0113("나홀로코스"),
    C01130001("나홀로코스"),
    C0114("힐링코스"),
    C01140001("힐링코스"),
    C0115("도보코스"),
    C01150001("도보코스"),
    C0116("캠핑코스"),
    C01160001("캠핑코스"),
    C0117("맛코스"),
    C01170001("맛코스");

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


data class DetailIntroItem(
    val contentid: Int,              // 콘텐츠 ID
    val contenttypeid: Int,             // 콘텐츠 타입 ID

    // --- 관광지 (12) ---
    val expguide: String? = null,       // 체험 안내
    val infocenter: String? = null,     // 문의 및 안내
    val opendate: String? = null,       // 개장일
    val parking: String? = null,        // 주차 시설
    val restdate: String? = null,       // 쉬는 날
    val useseason: String? = null,      // 이용 시기
    val usetime: String? = null,        // 이용 시간

    // --- 문화시설 (14) ---
    val infocenterculture: String? = null,       // 문의 및 안내
    val parkingculture: String? = null,          // 주차 시설
    val parkingfee: String? = null,              // 주차 요금
    val restdateculture: String? = null,         // 쉬는 날
    val usefee: String? = null,                  // 이용 요금
    val usetimeculture: String? = null,          // 이용 시간
    val spendtime: String? = null,               // 관람 소요 시간

    // --- 축제/공연/행사 (15) ---
    val bookingplace: String? = null,            // 예매처
    val eventenddate: String? = null,            // 행사 종료일
    val eventhomepage: String? = null,           // 행사 홈페이지
    val eventplace: String? = null,              // 행사 장소
    val eventstartdate: String? = null,          // 행사 시작일
    val playtime: String? = null,                // 공연 시간
    val program: String? = null,                 // 행사 프로그램
    val spendtimefestival: String? = null,       // 관람 소요 시간
    val usetimefestival: String? = null,         // 이용 요금

    // --- 레포츠 (28) ---
    val infocenterleports: String? = null,       // 문의 및 안내
    val openperiod: String? = null,              // 개장 기간
    val parkingfeeleports: String? = null,       // 주차 요금
    val parkingleports: String? = null,          // 주차 시설
    val reservation: String? = null,             // 예약 안내
    val restdateleports: String? = null,         // 쉬는 날
    val usefeeleports: String? = null,           // 입장료
    val usetimeleports: String? = null,          // 이용 시간

    // --- 숙박 (32) ---
    val checkintime: String? = null,             // 입실 시간
    val checkouttime: String? = null,            // 퇴실 시간
    val infocenterlodging: String? = null,       // 문의 및 안내
    val parkinglodging: String? = null,          // 주차 시설
    val roomcount: String? = null,               // 객실 수
    val reservationlodging: String? = null,      // 예약 안내
    val reservationurl: String? = null,          // 예약 안내 홈페이지
    val roomtype: String? = null,                // 객실 유형

    // --- 쇼핑 (38) ---
    val infocentershopping: String? = null,      // 문의 및 안내
    val opentime: String? = null,                // 영업 시간
    val parkingshopping: String? = null,         // 주차 시설
    val saleitem: String? = null,                // 판매 품목
    val saleitemcost: String? = null,            // 판매 품목별 가격
    val scaleshopping: String? = null,           // 규모
    val shopguide: String? = null,               // 매장 안내

    // --- 음식점 (39) ---
    val discountinfofood: String? = null,        // 할인 정보
    val firstmenu: String? = null,               // 대표 메뉴
    val infocenterfood: String? = null,          // 문의 및 안내
    val opentimefood: String? = null,            // 영업 시간
    val restdatefood: String? = null,            // 쉬는 날
    val treatmenu: String? = null,               // 취급 메뉴
)
