package com.hrk.test_app.common.language

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.annotation.ArrayRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

private fun Context.getLocalizedContext(locale: Locale): Context {
    val configuration = Configuration(resources.configuration).apply {
        setLocale(locale)
    }
    return createConfigurationContext(configuration)
}

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun localizedString(
    @StringRes id: Int,
    vararg formatArgs: Any
): String {
    val localizedContext = LocalContext.current.getLocalizedContext(LocalAppLocale.current)
    return if (formatArgs.isNotEmpty()) localizedContext.getString(id, *formatArgs)
    else localizedContext.getString(id)
}

fun Context.localizedString(
    locale: Locale,
    @StringRes id: Int,
    vararg formatArgs: Any
): String {
    val localizedContext = getLocalizedContext(locale)
    return if (formatArgs.isNotEmpty()) localizedContext.getString(id, *formatArgs)
    else localizedContext.getString(id)
}

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun localizedStringArray(@ArrayRes id: Int): Array<String> {
    return LocalContext.current.getLocalizedContext(LocalAppLocale.current).resources.getStringArray(
        id
    )
}

fun Context.localizedStringArray(
    locale: Locale,
    @ArrayRes id: Int
): Array<String> = getLocalizedContext(locale).resources.getStringArray(id)

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun localizedQuantityString(
    @PluralsRes id: Int,
    quantity: Int,
    vararg formatArgs: Any
): String {
    val localizedContext = LocalContext.current.getLocalizedContext(LocalAppLocale.current)
    return if (formatArgs.isNotEmpty()) localizedContext.resources.getQuantityString(
        id,
        quantity,
        *formatArgs
    )
    else localizedContext.resources.getQuantityString(id, quantity)
}

fun Context.localizedQuantityString(
    locale: Locale,
    @PluralsRes id: Int,
    quantity: Int,
    vararg formatArgs: Any
): String {
    val localizedContext = getLocalizedContext(locale)
    return if (formatArgs.isNotEmpty()) localizedContext.resources.getQuantityString(
        id,
        quantity,
        *formatArgs
    )
    else localizedContext.resources.getQuantityString(id, quantity)
}
