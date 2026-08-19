package com.hrk.test_app.common.theme

import androidx.compose.ui.graphics.Color
import com.hrk.test_app.R

enum class AppThemeMode(
    val themeCode: Int,
    val themeName: Int,
    val color: Color,
) {
    LIGHT(
        themeCode = 20010,
        themeName = R.string.theme_light,
        color = Color(0xFFFCFDFD),
    ),
    DARK(
        themeCode = 20011,
        themeName = R.string.theme_dark,
        color = Color(0xFF010206),
    );

    companion object {
        fun fromThemeCode(code: Int): AppThemeMode =
            entries.find { it.themeCode == code } ?: LIGHT
    }
}
