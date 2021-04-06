package dev.ronnie.imageloaderdagger2.api

import dev.ronnie.imageloaderdagger2.data.model.ImagesResponse
import dev.ronnie.imageloaderdagger2.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * created by Ronnie Otieno on 31-Mar-21.
 */
interface UnSplashService {
    @GET("photos")
    suspend fun getImages(
        @Query("order_by") orderBy: String,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): List<ImagesResponse>

    @GET("search/photos")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("order_by") orderBy: String,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): SearchResponse
}