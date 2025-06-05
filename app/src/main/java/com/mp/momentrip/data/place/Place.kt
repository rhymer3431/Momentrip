package com.mp.momentrip.data.place

import androidx.annotation.DrawableRes
import com.mp.momentrip.R

data class Place(
    val contentId: Int = 0,                      // 필수 (기본값 0)
    val contentTypeId: Int = 0,                  // 필수 (기본값 0)
    val title: String = "",                      // 필수 (기본값 빈 문자열)
    val addr1: String? = null,                   // 주소 (Optional)
    val addr2: String? = null,                   // 상세주소 (Optional)
    val areaCode: String? = null,                // 지역 코드 (Optional)
    val sigunguCode: Int? = null,                // 시군구 코드 (Optional)
    val cat1: String? = null,                    // 대분류 코드 (Optional)
    val cat2: String? = null,                    // 중분류 코드 (Optional)
    val cat3: String? = null,                    // 소분류 코드 (Optional)
    val firstImage: String? = null,              // 대표 이미지 (원본) (Optional)
    val firstImage2: String? = null,             // 대표 이미지 (썸네일) (Optional)
    val x: Double = 0.0,                    // GPS X좌표 (Optional)
    val y: Double = 0.0,                    // GPS Y좌표 (Optional)
    val tel: String? = null,                     // 전화번호 (Optional)
    val hmpg: String? = null,                    // 홈페이지주소 (Optional)
    val overview: String? = null,                 // 개요 (Optional)

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
    val placeinfo: String? = null,               // 행사장 위치 안내
    val playtime: String? = null,                // 공연 시간
    val program: String? = null,                 // 행사 프로그램
    val spendtimefestival: String? = null,       // 관람 소요 시간
    val sponsor1: String? = null,                // 주최자 정보
    val sponsor1tel: String? = null,             // 주최자 연락처
    val sponsor2: String? = null,                // 주관사 정보
    val sponsor2tel: String? = null,             // 주관사 연락처
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
    val chkcooking: String? = null,              // 객실 내 취사 가능 여부
    val infocenterlodging: String? = null,       // 문의 및 안내
    val parkinglodging: String? = null,          // 주차 시설
    val roomcount: String? = null,               // 객실 수
    val reservationlodging: String? = null,      // 예약 안내
    val reservationurl: String? = null,          // 예약 안내 홈페이지
    val roomtype: String? = null,                // 객실 유형
    val scalelodging: String? = null,            // 규모
    val subfacility: String? = null,             // 기타 부대시설
    val campfire: String? = null,                // 캠프파이어 여부
    val fitness: String? = null,                 // 피트니스 센터 여부
    val karaoke: String? = null,                 // 노래방 여부
    val publicbath: String? = null,              // 공용 샤워실 여부
    val publicpc: String? = null,                // 공용 PC실 여부
    val sauna: String? = null,                   // 사우나 여부
    val seminar: String? = null,                 // 세미나실 여부
    val sports: String? = null,                  // 스포츠 시설 여부

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

data class KakaoPlaceDTO(
    val name: String = "",
    val address: String = "",
    val category: String = "",
    val phone: String = "",
    val x: Double = 0.0,         // GPS X좌표 (경도)
    val y: Double = 0.0,         // GPS Y좌표 (위도)
)

enum class Region(val code: Int, val locationName: String) {
    SEOUL(1, "서울"),
    INCHEON(2, "인천"),
    DAEJEON(3, "대전"),
    DAEGU(4, "대구"),
    GWANGJU(5, "광주"),
    BUSAN(6, "부산"),
    ULSAN(7, "울산"),
    SEJONG(8, "세종"),
    GYEONGGI(31, "경기"),
    GANGWON(32, "강원"),
    CHUNGBUK(33, "충북"),
    CHUNGNAM(34, "충남"),
    JEONBUK(35, "전북"),
    JEONNAM(36, "전남"),
    GYEONGBUK(37, "경북"),
    GYEONGNAM(38, "경남"),
    JEJU(39, "제주도");

    companion object {
        private val codeMap = entries.associateBy { it.code }
        private val nameMap = entries.associateBy { it.locationName }

        fun fromCode(code: Int) = codeMap[code]
        fun fromName(name: String) = nameMap[name]
        /**
         * 지역(locationName)에 대응하는 로컬 사진 리소스 ID 반환
         *
         * - 사진 파일은 res/drawable-* 에 아래와 같은 이름으로 저장돼 있다고 가정한다.
         *   ex) seoul.jpg  →  R.drawable.seoul
         *       incheon.jpg→  R.drawable.incheon
         *
         * - 매칭되지 않으면 null 반환 → 호출 측에서 플레이스홀더 처리
         */
        @DrawableRes
        fun getRegionImage(locationName: String): Int? =
            when (fromName(locationName)) {
                SEOUL     -> R.drawable.seoul
                INCHEON   ->  R.drawable.incheon
                DAEJEON   ->  R.drawable.daejeon
                DAEGU     ->  R.drawable.daegu
                GWANGJU   ->    R.drawable.gwangju
                BUSAN     ->  R.drawable.busan
                ULSAN     ->  R.drawable.ulsan
                SEJONG    ->  R.drawable.sejong
                GYEONGGI  ->  R.drawable.gyeonggi
                GANGWON   ->  R.drawable.ganrueng
                CHUNGBUK  ->  R.drawable.chungbook
                CHUNGNAM  ->  R.drawable.chungnam
                JEONBUK   ->  R.drawable.jeonbook
                JEONNAM   ->  R.drawable.jeonnam
                GYEONGBUK ->  R.drawable.gyeongbook
                GYEONGNAM ->  R.drawable.gyeongnam
                JEJU      ->  R.drawable.jeju
                null             -> null          // 알 수 없는 지역
            }

    }
}

val dummy_place = Place(
    contentId = 1,
    contentTypeId = 12,
    title = "한라산 국립공원",
    addr1 = "제주특별자치도 제주시 1100로",
    areaCode = "39",
    sigunguCode = 1,
    firstImage = "https://example.com/images/hallasan.jpg",
    firstImage2 = "https://example.com/images/hallasan_thumb.jpg",
    x = 126.532,
    y = 33.361,
    overview = "한라산은 제주도의 중심에 있는 한국에서 가장 높은 산으로, 사계절 다양한 자연 풍경을 자랑합니다.",
)
