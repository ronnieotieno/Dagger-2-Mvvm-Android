package dev.ronnie.imageloaderdagger2.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import dev.ronnie.imageloaderdagger2.api.UnSplashService
import dev.ronnie.imageloaderdagger2.data.datasource.ImagesDataSource
import dev.ronnie.imageloaderdagger2.data.datasource.SearchDataSource
import dev.ronnie.imageloaderdagger2.utils.LOAD_SIZE
import javax.inject.Inject

/**
 *created by Ronnie Otieno on 03-Apr-21.
 **/
class Repository @Inject constructor(private val unSplashService: UnSplashService) {

    fun getImages(orderBy:String) = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = LOAD_SIZE),
        pagingSourceFactory = {
            ImagesDataSource(unSplashService, orderBy)
        }
    ).flow

    fun searchImage(query: String,orderBy:String) = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = LOAD_SIZE),
        pagingSourceFactory = {
            SearchDataSource(unSplashService, query, orderBy)
        }
    ).flow

}