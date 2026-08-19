package com.hrk.test_app.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainImmediateDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultDispatcher
