package com.hrk.test_app.common.language

import androidx.compose.runtime.compositionLocalOf
import java.util.Locale

val LocalAppLocale = compositionLocalOf {
    Locale.getDefault()
}
