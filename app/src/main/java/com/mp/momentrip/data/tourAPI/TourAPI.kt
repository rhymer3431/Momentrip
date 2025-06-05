package com.mp.momentrip.data.tourAPI

import com.google.gson.annotations.SerializedName


// 카테고리 코드만 정의하는 Enum 클래스

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


