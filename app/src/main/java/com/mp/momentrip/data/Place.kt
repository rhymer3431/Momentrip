package com.mp.momentrip.data

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
            when (Region.fromName(locationName)) {
                Region.SEOUL     -> R.drawable.q4a1
                Region.INCHEON   ->  R.drawable.q4a1
                Region.DAEJEON   ->  R.drawable.q4a1
                Region.DAEGU     ->  R.drawable.q4a1
                Region.GWANGJU   ->    R.drawable.q4a1
                Region.BUSAN     ->  R.drawable.q4a1
                Region.ULSAN     ->  R.drawable.q4a1
                Region.SEJONG    ->  R.drawable.q4a1
                Region.GYEONGGI  ->  R.drawable.q4a1
                Region.GANGWON   ->  R.drawable.q4a1
                Region.CHUNGBUK  ->  R.drawable.q4a1
                Region.CHUNGNAM  ->  R.drawable.q4a1
                Region.JEONBUK   ->  R.drawable.q4a1
                Region.JEONNAM   ->  R.drawable.q4a1
                Region.GYEONGBUK ->  R.drawable.q4a1
                Region.GYEONGNAM ->  R.drawable.q4a1
                Region.JEJU      ->  R.drawable.q4a1
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
    useTime = "09:00 ~ 17:00",
    infoCenter = "064-123-4567"
)
