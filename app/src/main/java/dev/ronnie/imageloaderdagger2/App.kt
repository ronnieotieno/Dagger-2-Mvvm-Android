package dev.ronnie.imageloaderdagger2

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dev.ronnie.imageloaderdagger2.di.DaggerAppComponent

class App : DaggerApplication() {
    private val applicationInjector = DaggerAppComponent.builder().application(this).build()
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = applicationInjector
}
