package com.mp.momentrip.service


import okhttp3.OkHttpClient
import com.mp.momentrip.data.tourAPI.ApiResponse

import com.mp.momentrip.data.tourAPI.AreaCode



import com.mp.momentrip.data.tourAPI.FestivalItem


import com.mp.momentrip.data.tourAPI.AreaBasedItem
import com.mp.momentrip.data.tourAPI.ContentDetailItem
import com.mp.momentrip.data.tourAPI.DetailIntroItem
import com.mp.momentrip.data.tourAPI.KeywordSearchItem
import com.mp.momentrip.data.tourAPI.LocationBasedItem
import com.mp.momentrip.data.tourAPI.StayItem
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


val request_num = 20


interface TourAPIService {
    @GET("areaCode1")
    suspend fun getAreaCode(
        @Query("numOfRows") numOfRows: Int = request_num,
        @Query("pageNo") pageNo: Int = 1,
        @Query("areaCode") areaCode: String? = null,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "MomenTrip",
        @Query("_type") type: String = "json",
        @Query("ServiceKey") serviceKey: String
    ): ApiResponse<AreaCode>

    @GET("areaBasedList1")
    suspend fun getAreaBasedList(
        @Query("numOfRows") numOfRows: Int = request_num,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "MomenTrip",
        @Query("arrange") arrange: String = "Q",
        @Query("_type") type: String = "json",
        @Query("areaCode") areaCode: String? = null,
        @Query("sigunguCode") sigunguCode: String? = null,
        @Query("contentTypeId") contentTypeId: Int,
        @Query("serviceKey") serviceKey: String,
    ): ApiResponse<AreaBasedItem>

    @GET("locationBasedList1")
    suspend fun getLocationBasedList(
        @Query("numOfRows") numOfRows: Int = request_num,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "MomenTrip",
        @Query("ServiceKey") serviceKey: String,
        @Query("_type") type: String = "json",
        @Query("listYN") listYN: String = "Y",
        @Query("arrange") arrange: String = "Q",
        @Query("contentTypeId") contentTypeId: String? = null,
        @Query("mapX") mapX: Double? = null,
        @Query("mapY") mapY: Double? = null,
        @Query("radius") radius: Int? = null,
        @Query("modifiedtime") modifiedTime: String? = null,
    ): ApiResponse<LocationBasedItem>

    @GET("searchKeyword1")
    suspend fun searchKeyword(
        @Query("numOfRows") numOfRows: Int = request_num,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "AppTest",
        @Query("serviceKey") serviceKey: String,
        @Query("_type") type: String = "json",
        @Query("listYN") listYN: String = "Y",
        @Query("arrange") arrange: String = "Q",
        @Query("contentTypeId") contentTypeId: String? = "38",
        @Query("areaCode") areaCode: String? = null,
        @Query("sigunguCode") sigunguCode: String? = null,
        @Query("cat1") cat1: String? = null,
        @Query("cat2") cat2: String? = null,
        @Query("cat3") cat3: String? = null,
        @Query("keyword") keyword: String? = null,
        @Query("mapX") mapX: Double? = null,
        @Query("mapY") mapY: Double? = null,
        @Query("radius") radius: Int? = null,
        @Query("modifiedtime") modifiedTime: String? = null,
    ): ApiResponse<KeywordSearchItem>

    @GET("searchFestival1")
    suspend fun searchFestival(
        @Query("numOfRows") numOfRows: Int = request_num,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "APPTest",
        @Query("serviceKey") serviceKey: String,
        @Query("_type") type: String = "json",
        @Query("listYN") listYN: String = "Y",
        @Query("arrange") arrange: String = "A",
        @Query("areaCode") areaCode: String? = null,
        @Query("sigunguCode") sigunguCode: String? = null,
        @Query("eventStartDate") eventStartDate: String? = null,  // 행사 시작일 (YYYYMMDD 형식)
        @Query("eventEndDate") eventEndDate: String? = null,      // 행사 종료일 (YYYYMMDD 형식)
        @Query("modifiedtime") modifiedTime: String? = null,
    ): ApiResponse<FestivalItem>
    // Add other API endpoints following the same pattern

    @GET("searchStay1")
    suspend fun searchStay(
        @Query("numOfRows") numOfRows: Int = request_num,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "APPTest",
        @Query("serviceKey") serviceKey: String,
        @Query("_type") type: String = "json",
        @Query("listYN") listYN: String = "Y",
        @Query("arrange") arrange: String = "A",
        @Query("areaCode") areaCode: String? = null,
        @Query("sigunguCode") sigunguCode: String? = null,
        @Query("modifiedtime") modifiedTime: String? = null,
    ): ApiResponse<StayItem>

    @GET("detailCommon1")
    suspend fun getContentDetail(
        @Query("numOfRows") numOfRows: Int = request_num,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") mobileOS: String = "ETC",
        @Query("MobileApp") mobileApp: String = "APPTest",
        @Query("serviceKey") serviceKey: String,
        @Query("_type") type: String = "json",
        @Query("contentId") contentId: Int,
        @Query("contentTypeId") contentTypeId: Int,
        @Query("defaultYN") defaultYN: String = "Y",
        @Query("firstImageYN") firstImageYN: String = "Y",
        @Query("areacodeYN") areacodeYN: String = "Y",
        @Query("catcodeYN") catcodeYN: String = "Y",
        @Query("addrinfoYN") addrinfoYN: String = "Y",
        @Query("mapinfoYN") mapinfoYN: String = "Y",
        @Query("overviewYN") overviewYN: String = "Y"
    ): ApiResponse<ContentDetailItem>

    @GET("detailIntro1")
    suspend fun getDetailIntro(
        @Query("numOfRows") numOfRows: Int = request_num,         // 페이지당 결과 수
        @Query("pageNo") pageNo: Int = 1,                         // 페이지 번호
        @Query("MobileOS") mobileOS: String = "ETC",              // OS
        @Query("MobileApp") mobileApp: String = "APPTest",        // 앱 이름
        @Query("serviceKey") serviceKey: String,                  // 인증 키
        @Query("_type") type: String = "json",                    // JSON 응답
        @Query("contentId") contentId: Int,                       // 콘텐츠 ID
        @Query("contentTypeId") contentTypeId: Int                // 콘텐츠 타입 ID
    ): ApiResponse<DetailIntroItem>

}

object TourApiClient {
    private const val BASE_URL = "http://apis.data.go.kr/B551011/KorService1/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY  // 요청/응답 바디 포함 전체 로그 출력
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)        // 🚨 이 줄이 반드시 필요함!
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)                      // 위에서 설정한 client 사용
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: TourAPIService = retrofit.create(TourAPIService::class.java)
}
