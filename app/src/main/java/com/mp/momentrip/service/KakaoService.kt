package com.mp.momentrip.service


import com.mp.momentrip.data.KakaoPlace
import com.mp.momentrip.data.Place
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface KakaoApiService {
    @GET("v2/local/search/keyword.json")
    fun searchPlaces(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
        @Query("category_group_code") categoryGroupCode: String,
        @Query("size") size: Int,
        @Query("page") page: Int,
        @Query("sort") sort: String
    ): Call<com.mp.momentrip.data.KakaoSearchResponse>
}

fun convertPlaces(kakaoPlaces : List<KakaoPlace>) : List<Place> {
    val places: MutableList<Place> = mutableListOf()


    for (kp in kakaoPlaces){
        places.add(Place(
            name=kp.place_name,
            category=kp.category_name,
            address = kp.address_name,
            phone = kp.phone,
            x = kp.x.toDouble(),
            y = kp.y.toDouble()
        ))
    }
    return places
}

object RetrofitClient {
    private const val BASE_URL = "https://dapi.kakao.com/"

    val kakaoApiService: KakaoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoApiService::class.java)
    }
}