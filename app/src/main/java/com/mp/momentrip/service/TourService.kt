package com.mp.momentrip.service

import android.util.Log
import com.google.gson.GsonBuilder
import com.mp.momentrip.data.ContentTypeId
import com.mp.momentrip.data.Place

object TourService {
    private lateinit var serviceKey: String
    private const val baseUrl = "http://apis.data.go.kr/B551011/KorService1"
    private val service = TourApiClient.apiService
    private val gson = GsonBuilder().setDateFormat("yyyyMMdd").create()

    fun init(serviceKey: String) {
        TourService.serviceKey = serviceKey
    }

    suspend fun getRestaurantByRegion(region: String): List<Place> {
        val items = service.getAreaBasedList(
            areaCode = "1",
            contentTypeId = ContentTypeId.RESTAURANT.code,
            serviceKey = serviceKey
        ).response.body.items?.item

        val result = mutableListOf<Place>()
        items?.forEach {
            // getDetail을 호출하여 상세 정보를 가져옴
            val place = it.toPlace()
            val detailedPlace = getDetail(place)
            detailedPlace?.let { result.add(it) }
        }
        return result.toList()
    }

    suspend fun getDormitoryByRegion(region: String): List<Place> {
        val items = service.getAreaBasedList(
            areaCode = "1",
            contentTypeId = ContentTypeId.ACCOMMODATION.code,
            serviceKey = serviceKey
        ).response.body.items?.item

        val result = mutableListOf<Place>()
        items?.forEach {
            // getDetail을 호출하여 상세 정보를 가져옴
            val place = it.toPlace()
            val detailedPlace = getDetail(place)
            detailedPlace?.let { result.add(it) }
        }
        return result.toList()
    }

    suspend fun getTourSpotByRegion(region: String): List<Place> {
        val items = service.getAreaBasedList(
            areaCode = "1",
            contentTypeId = ContentTypeId.TOURIST_SPOT.code,
            serviceKey = serviceKey
        ).response.body.items?.item

        val result = mutableListOf<Place>()
        items?.forEach {
            // getDetail을 호출하여 상세 정보를 가져옴
            val place = it.toPlace()
            val detailedPlace = getDetail(place)
            detailedPlace?.let { result.add(it) }
        }
        return result.toList()
    }

    suspend fun getDetail(place: Place?): Place?{

        try{
            val item = service.getContentDetail(
                contentId = place?.contentId.toString(),
                serviceKey = serviceKey
            ).response.body.items?.item

            return item?.get(0)?.toPlace()
        }
        catch(e: Exception){
            return place
        }
    }



}