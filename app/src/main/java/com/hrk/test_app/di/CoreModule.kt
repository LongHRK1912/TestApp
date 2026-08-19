package com.hrk.test_app.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.hrk.test_app.common.update.AppUpdateHelper
import com.hrk.test_app.data.local_data_source.preference.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideSharedPrefs(
        @ApplicationContext context: Context
    ): SharedPrefs = SharedPrefs(context)

    @Provides
    @Singleton
    fun provideAppUpdateManager(
        @ApplicationContext context: Context
    ): AppUpdateManager = AppUpdateManagerFactory.create(context)

    @Provides
    @Singleton
    fun provideAppUpdateHelper(
        appUpdateManager: AppUpdateManager
    ): AppUpdateHelper = AppUpdateHelper(appUpdateManager)
}
