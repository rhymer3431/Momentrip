package com.mp.momentrip.data.tourAPI

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
