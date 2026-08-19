package com.hrk.test_app.ui

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrk.test_app.BuildConfig
import com.hrk.test_app.common.language.LanguageManager
import com.hrk.test_app.common.theme.AppThemeMode
import com.hrk.test_app.common.theme.ThemeHelper
import com.hrk.test_app.common.update.AppUpdateHelper
import com.hrk.test_app.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    themeHelper: ThemeHelper,
    languageManager: LanguageManager,
    private val updateHelper: AppUpdateHelper,
) : ViewModel() {
    private val _startDestination = MutableStateFlow<AppDestination?>(null)
    val startDestination: StateFlow<AppDestination?> = _startDestination.asStateFlow()

    val currentTheme: StateFlow<AppThemeMode> = themeHelper.currentTheme
    val localeState: StateFlow<Locale> = languageManager.localeFlow

    init {
        determineStartDestination()
    }

    private fun determineStartDestination() {
        viewModelScope.launch {
            _startDestination.value = AppDestination.Player
        }
    }

    fun checkForUpdates(activity: Activity) {
        if (!BuildConfig.DEBUG) {
            viewModelScope.launch {
                updateHelper.checkForImmediateUpdate(activity)
            }
            updateHelper.completeUpdateIfDownloaded()
        }
    }
}


