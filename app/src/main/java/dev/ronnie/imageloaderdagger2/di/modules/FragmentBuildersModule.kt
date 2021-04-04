package dev.ronnie.imageloaderdagger2.di.modules


import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.ronnie.imageloaderdagger2.presentation.fragments.ImagesListFragment
import dev.ronnie.imageloaderdagger2.presentation.fragments.SingleImageFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeImagesListFragment(): ImagesListFragment

    @ContributesAndroidInjector
    abstract fun contributeSingleFragment(): SingleImageFragment
}
