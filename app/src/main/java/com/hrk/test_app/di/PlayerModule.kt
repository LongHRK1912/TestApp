package com.hrk.test_app.di

import com.hrk.test_app.data.repository.PlayerRepository
import com.hrk.test_app.data.repository.PlayerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlayerModule {

    @Binds
    @Singleton
    abstract fun bindPlayerRepository(
        impl: PlayerRepositoryImpl
    ): PlayerRepository
}
