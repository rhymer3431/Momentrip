package com.mp.momentrip.service

import android.util.Log
import com.mp.momentrip.data.*

object TourService {
    private lateinit var serviceKey: String
    private val service = TourApiClient.apiService

    fun init(serviceKey: String) {
        TourService.serviceKey = serviceKey
    }
    suspend fun getRestaurantsByKeyword(keyword: String): List<Place> {
        return getPlacesByKeyword(keyword, ContentType.RESTAURANT.id.toString())
    }


    suspend fun getAccommodationsByKeyword(keyword: String): List<Place> {
        return getPlacesByKeyword(keyword, ContentType.ACCOMMODATION.id.toString())
    }

    suspend fun getTouristSpotsByKeyword(keyword: String): List<Place> {
        return getPlacesByKeyword(keyword, ContentType.TOURIST_SPOT.id.toString())
    }

    private suspend fun getPlacesByKeyword(
        keyword: String,
        contentTypeId: String
    ): List<Place> {
        val searchItems = try {
            service.searchKeyword(
                keyword = keyword,
                contentTypeId = contentTypeId,
                serviceKey = serviceKey
            ).response.body.items?.item
        } catch (e: Exception) {
            Log.e("TourService", "searchKeyword API 실패: ${e.message}")
            null
        } ?: return emptyList()

        return searchItems.map { item ->
            val detail = try {
                service.getContentDetail(
                    contentId = item.contentid,
                    contentTypeId = item.contenttypeid.toString(),
                    serviceKey = serviceKey
                ).response.body.items?.item?.firstOrNull()
            } catch (e: Exception) {
                Log.w("TourService", "detailCommon API 실패: ${e.message}")
                null
            }

            val intro = try {
                service.getDetailIntro(
                    contentId = item.contentid,
                    contentTypeId = item.contenttypeid,
                    serviceKey = serviceKey
                ).response.body.items?.item?.firstOrNull()
            } catch (e: Exception) {
                Log.w("TourService", "detailInfo API 실패: ${e.message}")
                null
            }

            mergeToPlace(item, detail, intro)
        }
    }



    suspend fun getRestaurantsByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentType.RESTAURANT.id.toString())
    }

    suspend fun getAccommodationsByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentType.ACCOMMODATION.id.toString())
    }

    suspend fun getTouristSpotsByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentType.TOURIST_SPOT.id.toString())
    }

    suspend fun getPlacesByRegion(region: String, contentTypeId: String): List<Place> {
        val areaCode = Region.fromName(region)?.code?.toString()
            ?: return emptyList()

        val items = try {
            service.getAreaBasedList(
                areaCode = areaCode,
                contentTypeId = contentTypeId,
                serviceKey = serviceKey
            ).response.body.items?.item
        } catch (e: Exception) {
            Log.e("TourService", "지역기반 API 실패: ${e.message}")
            null
        } ?: return emptyList()

        return items.mapNotNull { base ->
            val detail = try {
                service.getContentDetail(
                    contentId = base.contentid,
                    contentTypeId = base.contenttypeid.toString(),
                    serviceKey = serviceKey
                ).response.body.items?.item?.firstOrNull()
            } catch (e: Exception) {
                Log.w("TourService", "detailCommon 실패: ${e.message}")
                null
            }

            val intro = try {
                service.getDetailIntro(
                    contentId = base.contentid,
                    contentTypeId = base.contenttypeid,
                    serviceKey = serviceKey
                ).response.body.items?.item?.firstOrNull()
            } catch (e: Exception) {
                Log.w("TourService", "detailInfo 실패: ${e.message}")
                null
            }
            Log.d("test",intro.toString())
            mergeToPlaceFromArea(base, detail, intro)
        }
    }

    private fun mergeToPlace(
        base: KeywordSearchItem,
        detail: ContentDetailItem?,
        intro: DetailIntroItem?
    ): Place {
        return Place(
            contentId = base.contentid,
            contentTypeId = base.contenttypeid,
            title = detail?.title ?: base.title,
            addr1 = detail?.addr1 ?: base.addr1,
            addr2 = detail?.addr2 ?: base.addr2,
            areaCode = detail?.areacode ?: base.areacode,
            sigunguCode = base.sigungucode,
            cat1 = detail?.cat1 ?: base.cat1,
            cat2 = detail?.cat2 ?: base.cat2,
            cat3 = detail?.cat3 ?: base.cat3,
            firstImage = detail?.firstimage ?: base.firstimage,
            firstImage2 = detail?.firstimage2 ?: base.firstimage2,
            x = detail?.mapx ?: base.mapx ?: 0.0,
            y = detail?.mapy ?: base.mapy ?: 0.0,
            tel = detail?.tel ?: base.tel,
            hmpg = detail?.hmpg,
            overview = detail?.overview,

            // 관광지
            useTime = intro?.usetime,
            infoCenter = intro?.infocenter ?: intro?.infocenterlodging ?: intro?.infocenterfood,

            openTime = intro?.opentimefood,
            firstMenu = intro?.firstmenu,
            treatMenu = intro?.treatmenu,


            checkInTime = intro?.checkintime,
            checkOutTime = intro?.checkouttime,
            roomType = intro?.roomtype,
            reservation = null,
            reservationUrl = intro?.reservationurl,
            parking = intro?.parking ?: intro?.parkinglodging ?: intro?.parkingfood,

            foodPlace = null,
            pickup = null,

            eventStartDate = null,
            eventEndDate = null
        )
    }
    private fun mergeToPlaceFromArea(
        base: AreaBasedItem,
        detail: ContentDetailItem?,
        intro: DetailIntroItem?
    ): Place {
        return Place(
            contentId = base.contentid,
            contentTypeId = base.contenttypeid,
            title = detail?.title ?: base.title,
            addr1 = detail?.addr1 ?: base.addr1,
            addr2 = detail?.addr2 ?: base.addr2,
            areaCode = detail?.areacode ?: base.areacode,
            sigunguCode = base.sigungucode,
            cat1 = detail?.cat1 ?: base.cat1,
            cat2 = detail?.cat2 ?: base.cat2,
            cat3 = detail?.cat3 ?: base.cat3,
            firstImage = detail?.firstimage ?: base.firstimage,
            firstImage2 = detail?.firstimage2 ?: base.firstimage2,
            x = detail?.mapx ?: base.mapx ?: 0.0,
            y = detail?.mapy ?: base.mapy ?: 0.0,
            tel = detail?.tel ?: base.tel,
            hmpg = detail?.hmpg,
            overview = detail?.overview,

            useTime = intro?.usetime,
            infoCenter = intro?.infocenter ?: intro?.infocenterlodging ?: intro?.infocenterfood,
            openTime = intro?.opentimefood,
            firstMenu = intro?.firstmenu,
            treatMenu = intro?.treatmenu,
            checkInTime = intro?.checkintime,
            checkOutTime = intro?.checkouttime,
            roomType = intro?.roomtype,
            reservationUrl = intro?.reservationurl,
            parking = intro?.parking ?: intro?.parkinglodging ?: intro?.parkingfood

        )
    }

}
