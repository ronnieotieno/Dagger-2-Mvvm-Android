package dev.ronnie.imageloaderdagger2.di.modules

import dagger.Module


@Module(
    includes = [
        ViewModelModule::class,
        NetworkModule::class,
        DataModule::class
    ]
)
class AppModule