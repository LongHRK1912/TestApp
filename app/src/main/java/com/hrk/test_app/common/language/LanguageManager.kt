package com.hrk.test_app.common.language

import com.hrk.test_app.data.local_data_source.preference.SharedPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(
    private val prefs: SharedPrefs
) {
    private val supportedLanguageCodes = AppLanguage.entries.map { it.code }.toSet()

    private val _localeFlow = MutableStateFlow(resolveInitialLocale())
    val localeFlow: StateFlow<Locale> = _localeFlow.asStateFlow()

    fun current(): Locale = _localeFlow.value

    fun save(locale: Locale) {
        if (_localeFlow.value == locale) return
        prefs.currentLanguage = locale.toLanguageTag()
        _localeFlow.value = locale
    }

    private fun resolveInitialLocale(): Locale {
        val savedCode = prefs.currentLanguage
        if (savedCode.isNotBlank()) return Locale.forLanguageTag(savedCode)

        val deviceCode = Locale.getDefault().language
        val resolvedCode = if (deviceCode in supportedLanguageCodes) deviceCode
        else AppLanguage.default().code

        prefs.currentLanguage = resolvedCode
        return Locale.forLanguageTag(resolvedCode)
    }
}
