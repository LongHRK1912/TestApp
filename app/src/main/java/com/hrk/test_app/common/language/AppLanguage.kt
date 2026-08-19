package com.hrk.test_app.common.language

enum class AppLanguage(
    val code: String,
    val displayName: String
) {
    Arabic("ar", "Arabic"),
    German("de", "Deutsch"),
    English("en", "English"),
    Spanish("es", "Español"),
    French("fr", "Français"),
    Hindi("hi", "हिन्दी"),
    Indonesian("id", "Bahasa Indonesia"),
    Portuguese("pt", "Português"),
    Russian("ru", "Русский"),
    Vietnamese("vi", "Tiếng Việt"),
    Chinese("zh", "中文");

    companion object {
        fun default(): AppLanguage = English

        fun fromCode(code: String): AppLanguage =
            entries.find { it.code == code } ?: default()
    }
}
