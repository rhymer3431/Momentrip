import com.mp.momentrip.BuildConfig.KAKAO_API_KEY
import com.mp.momentrip.service.RetrofitClient

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

object Searcher {
    suspend fun getRepresentativeImage(restaurantName: String): String? {
        return try {
            println(restaurantName)
            val response = RetrofitClient.kakaoApiService.searchImages(
                "KakaoAK ${KAKAO_API_KEY}",
                restaurantName,
                size = 5,
                page = 1
            ).awaitResponse()

            if (response.isSuccessful) {
                response.body()?.documents
                    ?.sortedByDescending { it.width * it.height }
                    ?.firstOrNull()
                    ?.image_url
            } else {
                println("API call failed: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            println("Request failed: ${e.message}")
            null
        }
    }

    private suspend fun <T> Call<T>.awaitResponse(): Response<T> {
        return try {
            val response = execute()
            if (!response.isSuccessful) {
                throw IOException("Request failed: ${response.code()}")
            }
            response
        } catch (e: IOException) {
            throw e
        }

    }
}


fun main() = runBlocking {
    // 실제 사용시에는 보안을 위해 환경변수나 설정 파일에서 API 키를 가져오세요

    val placeName = "보문민속식당"


    if (placeName.isNotEmpty()) {
        println("'$placeName'의 이미지 검색 중...")

        val imageUrl = Searcher.getRepresentativeImage(placeName)

        if (imageUrl != null) {
            println("이미지 URL: $imageUrl")
            // 실제 앱에서는 이 URL을 사용해 이미지를 다운로드하여 표시
        } else {
            println("이미지를 찾을 수 없습니다.")
        }
    } else {
        println("장소 이름을 입력하지 않았습니다.")
    }
}