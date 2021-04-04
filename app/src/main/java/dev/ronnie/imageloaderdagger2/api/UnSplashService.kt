package dev.ronnie.imageloaderdagger2.api

import dev.ronnie.imageloaderdagger2.data.model.ImagesResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * created by Ronnie Otieno on 31-Mar-21.
 */
interface UnSplashService {
    @GET("photos")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("per_page") limit: Int

    ): List<ImagesResponse>
}