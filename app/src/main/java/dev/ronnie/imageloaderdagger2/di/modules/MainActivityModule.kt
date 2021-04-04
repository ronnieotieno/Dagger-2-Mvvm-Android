package dev.ronnie.imageloaderdagger2.di.modules


import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.ronnie.imageloaderdagger2.presentation.MainActivity

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(
        modules = [
            FragmentBuildersModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity
}
