package com.mp.momentrip.data.tourAPI


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
