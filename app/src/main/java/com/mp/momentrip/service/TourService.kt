package com.mp.momentrip.service

import android.util.Log
import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.place.Region
import com.mp.momentrip.data.tourAPI.AreaBasedItem
import com.mp.momentrip.data.tourAPI.ContentDetailItem
import com.mp.momentrip.data.tourAPI.ContentType
import com.mp.momentrip.data.tourAPI.DetailIntroItem
import com.mp.momentrip.data.tourAPI.KeywordSearchItem
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

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
                    contentTypeId = item.contenttypeid,
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



    suspend fun getCulturalFacilitiesByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentType.CULTURAL_FACILITY.id)
    }

    suspend fun getFestivalsByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentType.FESTIVAL_EVENT.id)
    }


    suspend fun getLeisureSportsByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentType.LEISURE_SPORTS.id)
    }

    suspend fun getAccommodationsByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentType.ACCOMMODATION.id)
    }

    suspend fun getShoppingPlacesByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentType.SHOPPING.id)
    }

    suspend fun getRestaurantsByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentType.RESTAURANT.id)
    }

    suspend fun getTouristSpotsByRegion(region: String): List<Place> {
        return getPlacesByRegion(region, ContentType.TOURIST_SPOT.id)
    }
    suspend fun getAllPlacesByRegion(region: String): List<Place> = coroutineScope {
        ContentType.entries.map { type ->
            async {
                getPlacesByRegion(region, type.id)
            }
        }.awaitAll().flatten()
    }

    suspend fun getPlacesByRegion(region: String, contentTypeId: Int): List<Place> {
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

        return items.map { base ->
            val detail = try {
                service.getContentDetail(
                    contentId = base.contentid,
                    contentTypeId = base.contenttypeid,
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

    /* ────────────────────────────────
       ① KeywordSearch → Place 변환
       ──────────────────────────────── */
    private fun mergeToPlace(
        base: KeywordSearchItem,
        detail: ContentDetailItem?,
        intro: DetailIntroItem?
    ): Place = Place(
        /* ─ 기본 공통 정보 ─ */
        contentId      = base.contentid,
        contentTypeId  = base.contenttypeid,
        title          = detail?.title ?: base.title,
        addr1          = detail?.addr1 ?: base.addr1,
        addr2          = detail?.addr2 ?: base.addr2,
        areaCode       = detail?.areacode ?: base.areacode,
        sigunguCode    = base.sigungucode,
        cat1           = detail?.cat1 ?: base.cat1,
        cat2           = detail?.cat2 ?: base.cat2,
        cat3           = detail?.cat3 ?: base.cat3,
        firstImage     = detail?.firstimage  ?: base.firstimage,
        firstImage2    = detail?.firstimage2 ?: base.firstimage2,
        x              = detail?.mapx ?: base.mapx ?: 0.0,
        y              = detail?.mapy ?: base.mapy ?: 0.0,
        tel            = detail?.tel ?: base.tel,
        hmpg           = detail?.hmpg,
        overview       = detail?.overview,

        /* ─ 관광지 (12) ─ */
        expguide       = intro?.expguide,
        infocenter     = intro?.infocenter,
        opendate       = intro?.opendate,
        parking        = intro?.parking,
        restdate       = intro?.restdate,
        useseason      = intro?.useseason,
        usetime        = intro?.usetime,

        /* ─ 문화시설 (14) ─ */
        infocenterculture = intro?.infocenterculture,
        parkingculture    = intro?.parkingculture,
        parkingfee        = intro?.parkingfee,
        restdateculture   = intro?.restdateculture,
        usefee            = intro?.usefee,
        usetimeculture    = intro?.usetimeculture,
        spendtime         = intro?.spendtime,

        /* ─ 축제/공연/행사 (15) ─ */
        bookingplace      = intro?.bookingplace,
        eventenddate      = intro?.eventenddate,
        eventhomepage     = intro?.eventhomepage,
        eventplace        = intro?.eventplace,
        eventstartdate    = intro?.eventstartdate,
        playtime          = intro?.playtime,
        program           = intro?.program,
        spendtimefestival = intro?.spendtimefestival,
        usetimefestival   = intro?.usetimefestival,

        /* ─ 레포츠 (28) ─ */
        infocenterleports = intro?.infocenterleports,
        openperiod        = intro?.openperiod,
        parkingfeeleports = intro?.parkingfeeleports,
        parkingleports    = intro?.parkingleports,
        reservation       = intro?.reservation,
        restdateleports   = intro?.restdateleports,
        usefeeleports     = intro?.usefeeleports,
        usetimeleports    = intro?.usetimeleports,

        /* ─ 숙박 (32) ─ */
        checkintime       = intro?.checkintime,
        checkouttime      = intro?.checkouttime,
        infocenterlodging = intro?.infocenterlodging,
        parkinglodging    = intro?.parkinglodging,
        roomcount         = intro?.roomcount,
        reservationlodging= intro?.reservationlodging,
        reservationurl    = intro?.reservationurl,
        roomtype          = intro?.roomtype,

        /* ─ 쇼핑 (38) ─ */
        infocentershopping= intro?.infocentershopping,
        opentime          = intro?.opentime,
        parkingshopping   = intro?.parkingshopping,
        saleitem          = intro?.saleitem,
        saleitemcost      = intro?.saleitemcost,
        scaleshopping     = intro?.scaleshopping,
        shopguide         = intro?.shopguide,

        /* ─ 음식점 (39) ─ */
        discountinfofood  = intro?.discountinfofood,
        firstmenu         = intro?.firstmenu,
        infocenterfood    = intro?.infocenterfood,
        opentimefood      = intro?.opentimefood,
        restdatefood      = intro?.restdatefood,
        treatmenu         = intro?.treatmenu
    )


    /* ───────────────────────────────────────────────
       ② AreaBasedList → Place 변환 (구조 동일)
       ─────────────────────────────────────────────── */
    private fun mergeToPlaceFromArea(
        base: AreaBasedItem,
        detail: ContentDetailItem?,
        intro: DetailIntroItem?
    ): Place = Place(
        /* ─ 기본 공통 정보 ─ */
        contentId      = base.contentid,
        contentTypeId  = base.contenttypeid,
        title          = detail?.title ?: base.title,
        addr1          = detail?.addr1 ?: base.addr1,
        addr2          = detail?.addr2 ?: base.addr2,
        areaCode       = detail?.areacode ?: base.areacode,
        sigunguCode    = base.sigungucode,
        cat1           = detail?.cat1 ?: base.cat1,
        cat2           = detail?.cat2 ?: base.cat2,
        cat3           = detail?.cat3 ?: base.cat3,
        firstImage     = detail?.firstimage  ?: base.firstimage,
        firstImage2    = detail?.firstimage2 ?: base.firstimage2,
        x              = detail?.mapx ?: base.mapx ?: 0.0,
        y              = detail?.mapy ?: base.mapy ?: 0.0,
        tel            = detail?.tel ?: base.tel,
        hmpg           = detail?.hmpg,
        overview       = detail?.overview,

        /* 이하 섹션별 매핑은 위 mergeToPlace 와 동일 */
        expguide           = intro?.expguide,
        infocenter         = intro?.infocenter,
        opendate           = intro?.opendate,
        parking            = intro?.parking,
        restdate           = intro?.restdate,
        useseason          = intro?.useseason,
        usetime            = intro?.usetime,

        infocenterculture  = intro?.infocenterculture,
        parkingculture     = intro?.parkingculture,
        parkingfee         = intro?.parkingfee,
        restdateculture    = intro?.restdateculture,
        usefee             = intro?.usefee,
        usetimeculture     = intro?.usetimeculture,
        spendtime          = intro?.spendtime,

        bookingplace       = intro?.bookingplace,
        eventenddate       = intro?.eventenddate,
        eventhomepage      = intro?.eventhomepage,
        eventplace         = intro?.eventplace,
        eventstartdate     = intro?.eventstartdate,
        playtime           = intro?.playtime,
        program            = intro?.program,
        spendtimefestival  = intro?.spendtimefestival,
        usetimefestival    = intro?.usetimefestival,

        infocenterleports  = intro?.infocenterleports,
        openperiod         = intro?.openperiod,
        parkingfeeleports  = intro?.parkingfeeleports,
        parkingleports     = intro?.parkingleports,
        reservation        = intro?.reservation,
        restdateleports    = intro?.restdateleports,
        usefeeleports      = intro?.usefeeleports,
        usetimeleports     = intro?.usetimeleports,

        checkintime        = intro?.checkintime,
        checkouttime       = intro?.checkouttime,
        infocenterlodging  = intro?.infocenterlodging,
        parkinglodging     = intro?.parkinglodging,
        roomcount          = intro?.roomcount,
        reservationlodging = intro?.reservationlodging,
        reservationurl     = intro?.reservationurl,
        roomtype           = intro?.roomtype,

        infocentershopping = intro?.infocentershopping,
        opentime           = intro?.opentime,
        parkingshopping    = intro?.parkingshopping,
        saleitem           = intro?.saleitem,
        saleitemcost       = intro?.saleitemcost,
        scaleshopping      = intro?.scaleshopping,
        shopguide          = intro?.shopguide,

        discountinfofood   = intro?.discountinfofood,
        firstmenu          = intro?.firstmenu,
        infocenterfood     = intro?.infocenterfood,
        opentimefood       = intro?.opentimefood,
        restdatefood       = intro?.restdatefood,
        treatmenu          = intro?.treatmenu
    )



}
