package com.hrk.test_app.common.theme

import android.content.Context
import android.content.res.Configuration
import com.hrk.test_app.data.local_data_source.preference.SharedPrefs
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeHelper @Inject constructor(
    private val prefs: SharedPrefs,
    @ApplicationContext context: Context,
) {
    private val _currentTheme = MutableStateFlow(AppThemeMode.DARK)
    val currentTheme: StateFlow<AppThemeMode> = _currentTheme.asStateFlow()

    init {
        val savedThemeCode = prefs.currentTheme
        if (savedThemeCode == -1) {
            val uiMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val initialTheme = if (uiMode == Configuration.UI_MODE_NIGHT_YES) AppThemeMode.DARK
            else AppThemeMode.LIGHT

            prefs.currentTheme = initialTheme.themeCode
            _currentTheme.value = initialTheme
        } else {
            _currentTheme.value = AppThemeMode.fromThemeCode(savedThemeCode)
        }
    }

    fun updateTheme(theme: AppThemeMode) {
        _currentTheme.value = theme
        prefs.currentTheme = theme.themeCode
    }
}
