package com.mp.momentrip.data.tourAPI

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