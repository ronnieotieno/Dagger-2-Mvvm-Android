package dev.ronnie.imageloaderdagger2.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import dev.ronnie.imageloaderdagger2.api.UnSplashService
import dev.ronnie.imageloaderdagger2.data.datasource.DataSource
import dev.ronnie.imageloaderdagger2.utils.LOAD_SIZE
import javax.inject.Inject

/**
 *created by Ronnie Otieno on 03-Apr-21.
 **/
class Repository @Inject constructor(private val unSplashService: UnSplashService) {

    fun getImages() = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = LOAD_SIZE),
        pagingSourceFactory = {
            DataSource(unSplashService)
        }
    ).flow

}