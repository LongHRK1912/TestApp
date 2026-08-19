package com.hrk.test_app.ui.theme

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.WindowCompat
import com.hrk.test_app.common.language.LocalAppLocale
import com.hrk.test_app.common.theme.AppThemeMode
import java.util.Locale

@Composable
fun TestAppTheme(
    themeMode: AppThemeMode,
    localeState: Locale,
    content: @Composable () -> Unit,
) {
    val target = when (themeMode) {
        AppThemeMode.LIGHT -> LightExtendedColors
        AppThemeMode.DARK -> DarkExtendedColors
    }

    val spec = tween<Color>(durationMillis = 500, easing = LinearEasing)

    @Composable
    fun Color.anim(label: String) = animateColorAsState(this, spec, label).value

    val animated = ExtendedColors(
        background = target.background.anim("bg"),
        onBackground = target.onBackground.anim("onBg"),
        surface = target.surface.anim("surface"),
        onSurface = target.onSurface.anim("onSurface"),
        dialog = target.dialog.anim("dialog"),
        onDialog = target.onDialog.anim("onDialog"),
        card = target.card.anim("card"),
        onCard = target.onCard.anim("onCard"),

        scrim = target.scrim.anim("scrim"),

        accent = target.accent.anim("accent"),
        onAccent = target.onAccent.anim("onAccent"),
        accentVariant = target.accentVariant.anim("accentVar"),
        accentContainer = target.accentContainer.anim("accentCont"),
        onAccentContainer = target.onAccentContainer.anim("onAccentCont"),

        accentSecondary = target.accentSecondary.anim("accentSec"),
        onAccentSecondary = target.onAccentSecondary.anim("onAccentSec"),
        accentSecondaryContainer = target.accentSecondaryContainer.anim("accentSecCont"),
        onAccentSecondaryContainer = target.onAccentSecondaryContainer.anim("onAccentSecCont"),

        success = target.success.anim("success"),
        onSuccess = target.onSuccess.anim("onSuccess"),
        successContainer = target.successContainer.anim("successCont"),
        onSuccessContainer = target.onSuccessContainer.anim("onSuccessCont"),

        warning = target.warning.anim("warning"),
        onWarning = target.onWarning.anim("onWarning"),
        warningContainer = target.warningContainer.anim("warningCont"),
        onWarningContainer = target.onWarningContainer.anim("onWarningCont"),

        info = target.info.anim("info"),
        onInfo = target.onInfo.anim("onInfo"),
        infoContainer = target.infoContainer.anim("infoCont"),
        onInfoContainer = target.onInfoContainer.anim("onInfoCont"),

        error = target.error.anim("error"),
        onError = target.onError.anim("onError"),
        errorContainer = target.errorContainer.anim("errorCont"),
        onErrorContainer = target.onErrorContainer.anim("onErrorCont"),

        heartRate = target.heartRate.anim("heartRate"),
        onHeartRate = target.onHeartRate.anim("onHeartRate"),
        heartRateContainer = target.heartRateContainer.anim("heartRateCont"),

        steps = target.steps.anim("steps"),
        onSteps = target.onSteps.anim("onSteps"),
        stepsContainer = target.stepsContainer.anim("stepsCont"),

        calories = target.calories.anim("calories"),
        onCalories = target.onCalories.anim("onCalories"),
        caloriesContainer = target.caloriesContainer.anim("caloriesCont"),

        medication = target.medication.anim("medication"),
        onMedication = target.onMedication.anim("onMedication"),
        medicationContainer = target.medicationContainer.anim("medicationCont"),

        sleep = target.sleep.anim("sleep"),
        onSleep = target.onSleep.anim("onSleep"),
        sleepContainer = target.sleepContainer.anim("sleepCont"),

        textPrimary = target.textPrimary.anim("textPrimary"),
        textSecondary = target.textSecondary.anim("textSec"),
        textTertiary = target.textTertiary.anim("textTertiary"),
        textDisabled = target.textDisabled.anim("textDisabled"),

        disabled = target.disabled.anim("disabled"),
        onDisabled = target.onDisabled.anim("onDisabled"),
        ripple = target.ripple.anim("ripple"),

        divider = target.divider.anim("divider"),
        outline = target.outline.anim("outline"),
        outlineVariant = target.outlineVariant.anim("outlineVar"),

        shimmer = target.shimmer.anim("shimmer"),
        shimmerHighlight = target.shimmerHighlight.anim("shimmerHL"),

        bottomBar = target.bottomBar.anim("bottomBar"),
        onBottomBar = target.onBottomBar.anim("onBottomBar"),
        bottomBarIndicator = target.bottomBarIndicator.anim("bottomBarInd"),
        topBar = target.topBar.anim("topBar"),
        onTopBar = target.onTopBar.anim("onTopBar"),
    )

    val view = LocalView.current

    LaunchedEffect(animated.background) {
        val activity = view.context as? Activity ?: return@LaunchedEffect
        val window = activity.window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setBackgroundDrawable(animated.background.toArgb().toDrawable())
    }

    CompositionLocalProvider(
        LocalAppLocale provides localeState,
        LocalExtendedColors provides animated,
    ) {
        MaterialTheme(
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}
