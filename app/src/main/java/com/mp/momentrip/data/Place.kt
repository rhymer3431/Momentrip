package com.mp.momentrip.data

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

    // 관광지 추가 정보
    val useTime: String? = null, // 이용시간
    val infoCenter: String? = null, // 문의 전화번호

    // 식당 추가 정보
    val openTime: String? = null, // 영업시간
    val firstMenu: String? = null, // 대표 메뉴
    val treatMenu: String? = null, // 취급 음식

    // 숙소 추가 정보
    val checkInTime: String? = null,
    val checkOutTime: String? = null,
    val roomType: String? = null,
    val reservation: String? = null, // 예약 방법
    val reservationUrl: String? = null, // 예약 url
    val parking: String? = null,
    val foodPlace: String? = null,
    val pickup: String? = null,

    val eventStartDate: String? = null,          // 행사시작일 (Optional)
    val eventEndDate: String? = null,            // 행사종료일 (Optional)

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
    SEJONG(8, "세종시"),
    GYEONGGI(31, "경기도"),
    GANGWON(32, "강원도"),
    CHUNGBUK(33, "충청북도"),
    CHUNGNAM(34, "충청남도"),
    JEONBUK(35, "전라북도"),
    JEONNAM(36, "전라남도"),
    GYEONGBUK(37, "경상북도"),
    GYEONGNAM(38, "경상남도"),
    JEJU(39, "제주도");

    companion object {
        private val codeMap = entries.associateBy { it.code }
        private val nameMap = entries.associateBy { it.locationName }

        fun fromCode(code: Int) = codeMap[code]
        fun fromName(name: String) = nameMap[name]
    }
}

