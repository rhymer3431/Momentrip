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
        return getPlacesByKeyword(keyword, ContentTypeId.RESTAURANT.code)
    }

    suspend fun getAccommodationsByKeyword(keyword: String): List<Place> {
        return getPlacesByKeyword(keyword, ContentTypeId.ACCOMMODATION.code)
    }

    suspend fun getTouristSpotsByKeyword(keyword: String): List<Place> {
        return getPlacesByKeyword(keyword, ContentTypeId.TOURIST_SPOT.code)
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

            val repeat = try {
                service.getDetailInfo(
                    contentId = item.contentid.toString(),
                    contentTypeId = item.contenttypeid,
                    serviceKey = serviceKey
                ).response.body.items?.item?.firstOrNull()
            } catch (e: Exception) {
                Log.w("TourService", "detailInfo API 실패: ${e.message}")
                null
            }

            mergeToPlace(item, detail, repeat)
        }
    }



    suspend fun getRestaurantsByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentTypeId.RESTAURANT.code)
    }

    suspend fun getAccommodationsByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentTypeId.ACCOMMODATION.code)
    }

    suspend fun getTouristSpotsByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentTypeId.TOURIST_SPOT.code)
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

            val repeat = try {
                service.getDetailInfo(
                    contentId = base.contentid.toString(),
                    contentTypeId = base.contenttypeid,
                    serviceKey = serviceKey
                ).response.body.items?.item?.firstOrNull()
            } catch (e: Exception) {
                Log.w("TourService", "detailInfo 실패: ${e.message}")
                null
            }

            mergeToPlaceFromArea(base, detail, repeat)
        }
    }

    private fun mergeToPlace(
        base: KeywordSearchItem,
        detail: ContentDetailItem?,
        repeat: DetailInfoItem?
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
            useTime = repeat?.useTime,
            infoCenter = repeat?.infoCenter ?: repeat?.infoCenterLodging ?: repeat?.infoCenterFood,

            // 음식점
            openTime = repeat?.openTimeFood,
            firstMenu = repeat?.firstMenu,
            treatMenu = repeat?.treatMenu,

            // 숙소
            checkInTime = repeat?.checkInTime,
            checkOutTime = repeat?.checkOutTime,
            roomType = repeat?.roomType,
            reservation = null,
            reservationUrl = repeat?.reservationUrl,
            parking = repeat?.parking ?: repeat?.parkingLodging ?: repeat?.parkingFood,
            foodPlace = null,
            pickup = null,

            eventStartDate = null,
            eventEndDate = null
        )
    }
    private fun mergeToPlaceFromArea(
        base: AreaBasedItem,
        detail: ContentDetailItem?,
        repeat: DetailInfoItem?
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

            useTime = repeat?.useTime,
            infoCenter = repeat?.infoCenter ?: repeat?.infoCenterLodging ?: repeat?.infoCenterFood,
            openTime = repeat?.openTimeFood,
            firstMenu = repeat?.firstMenu,
            treatMenu = repeat?.treatMenu,
            checkInTime = repeat?.checkInTime,
            checkOutTime = repeat?.checkOutTime,
            roomType = repeat?.roomType,
            reservationUrl = repeat?.reservationUrl,
            parking = repeat?.parking ?: repeat?.parkingLodging ?: repeat?.parkingFood
        )
    }

}
