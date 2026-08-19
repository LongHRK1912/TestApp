package com.hrk.test_app.di

import android.content.Context
import com.hrk.test_app.utils.ConnectivityManagerNetworkMonitor
import com.hrk.test_app.utils.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): NetworkMonitor = ConnectivityManagerNetworkMonitor(context, ioDispatcher)
}
