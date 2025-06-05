package com.mp.momentrip.data.tourAPI

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
