package dev.ronnie.imageloaderdagger2.di.modules

import dagger.Module
import dagger.Provides
import dev.ronnie.imageloaderdagger2.api.UnSplashService
import dev.ronnie.imageloaderdagger2.data.repository.Repository
import javax.inject.Singleton


/**
 *created by Ronnie Otieno on 03-Apr-21.
 **/
@Module
object DataModule {

    @Singleton
    @Provides
    fun providesRepository(unSplashService: UnSplashService): Repository =
        Repository(unSplashService)
}