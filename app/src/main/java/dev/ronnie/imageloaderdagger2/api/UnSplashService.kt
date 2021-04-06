package dev.ronnie.imageloaderdagger2.api

import dev.ronnie.imageloaderdagger2.data.model.ImagesResponse
import dev.ronnie.imageloaderdagger2.data.model.SearchResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
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

    //Dont worry about response we just tell the unsplash that user downlaoded the image
    @GET("photos/{id}/download")
    suspend fun downloadImage(
        @Path("id") photoId: String
    ): ResponseBody
}