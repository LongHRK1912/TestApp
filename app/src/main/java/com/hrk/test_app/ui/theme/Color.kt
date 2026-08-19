package com.hrk.test_app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ExtendedColors(
    // ── Backgrounds ──────────────────────────────────────────────────────────
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val dialog: Color,
    val onDialog: Color,
    val card: Color,
    val onCard: Color,

    // ── Scrim / Overlay ──────────────────────────────────────────────────────
    val scrim: Color,

    // ── Accent (Primary) ─────────────────────────────────────────────────────
    val accent: Color,
    val onAccent: Color,
    val accentVariant: Color,
    val accentContainer: Color,
    val onAccentContainer: Color,

    // ── Secondary Accent ─────────────────────────────────────────────────────
    val accentSecondary: Color,
    val onAccentSecondary: Color,
    val accentSecondaryContainer: Color,
    val onAccentSecondaryContainer: Color,

    // ── Semantic: Status ──────────────────────────────────────────────────────
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,

    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,

    val info: Color,
    val onInfo: Color,
    val infoContainer: Color,
    val onInfoContainer: Color,

    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,

    // ── Semantic: Health Metrics ──────────────────────────────────────────────
    val heartRate: Color,
    val onHeartRate: Color,
    val heartRateContainer: Color,

    val steps: Color,
    val onSteps: Color,
    val stepsContainer: Color,

    val calories: Color,
    val onCalories: Color,
    val caloriesContainer: Color,

    val medication: Color,
    val onMedication: Color,
    val medicationContainer: Color,

    val sleep: Color,
    val onSleep: Color,
    val sleepContainer: Color,

    // ── Text hierarchy ────────────────────────────────────────────────────────
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,

    // ── Interactive states ────────────────────────────────────────────────────
    val disabled: Color,
    val onDisabled: Color,
    val ripple: Color,

    // ── Borders & separators ──────────────────────────────────────────────────
    val divider: Color,
    val outline: Color,
    val outlineVariant: Color,

    // ── Skeleton / Shimmer ────────────────────────────────────────────────────
    val shimmer: Color,
    val shimmerHighlight: Color,

    // ── Navigation bars ───────────────────────────────────────────────────────
    val bottomBar: Color,
    val onBottomBar: Color,
    val bottomBarIndicator: Color,
    val topBar: Color,
    val onTopBar: Color,
)

val LocalExtendedColors = staticCompositionLocalOf<ExtendedColors> {
    error("No ExtendedColors provided — wrap your composable in AppTheme")
}

// ──────────────────────────────────────────────────────────────────────────────
// LIGHT THEME (Primary: #2A8477 - Teal Aesthetic)
// ──────────────────────────────────────────────────────────────────────────────
val LightExtendedColors = ExtendedColors(
    background = Color(0xFFF5F9F8),
    onBackground = Color(0xFF191C1B),
    surface = Color(0xFFFBFDFB),
    onSurface = Color(0xFF191C1B),
    dialog = Color(0xFFFFFFFF),
    onDialog = Color(0xFF191C1B),
    card = Color(0xFFFFFFFF),
    onCard = Color(0xFF191C1B),

    scrim = Color(0x66000000),

    accent = Color(0xFF2A8477),
    onAccent = Color(0xFFFFFFFF),
    accentVariant = Color(0xFF1E6C60),
    accentContainer = Color(0xFFBBECE4),
    onAccentContainer = Color(0xFF00201C),

    accentSecondary = Color(0xFF4A635E),
    onAccentSecondary = Color(0xFFFFFFFF),
    accentSecondaryContainer = Color(0xFFCCE8E1),
    onAccentSecondaryContainer = Color(0xFF05201C),

    success = Color(0xFF2E7D32),
    onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFC8E6C9),
    onSuccessContainer = Color(0xFF00210B),

    warning = Color(0xFFED6C02),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFFFE0B2),
    onWarningContainer = Color(0xFF421A00),

    info = Color(0xFF0288D1),
    onInfo = Color(0xFFFFFFFF),
    infoContainer = Color(0xFFB3E5FC),
    onInfoContainer = Color(0xFF001F2F),

    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),

    heartRate = Color(0xFFE53935),
    onHeartRate = Color(0xFFFFFFFF),
    heartRateContainer = Color(0xFFFFCDD2),

    steps = Color(0xFF2A8477),
    onSteps = Color(0xFFFFFFFF),
    stepsContainer = Color(0xFFBBECE4),

    calories = Color(0xFFFF9800),
    onCalories = Color(0xFFFFFFFF),
    caloriesContainer = Color(0xFFFFE0B2),

    medication = Color(0xFF9C27B0),
    onMedication = Color(0xFFFFFFFF),
    medicationContainer = Color(0xFFE1BEE7),

    sleep = Color(0xFF3F51B5),
    onSleep = Color(0xFFFFFFFF),
    sleepContainer = Color(0xFFC5CAE9),

    textPrimary = Color(0xFF191C1B),
    textSecondary = Color(0xFF3F4946),
    textTertiary = Color(0xFF56605D),
    textDisabled = Color(0xFF8B9390),

    disabled = Color(0xFFE1E3E2),
    onDisabled = Color(0xFFB0B6B4),
    ripple = Color(0x1F2A8477),

    divider = Color(0xFFC4C8C6),
    outline = Color(0xFF6F7976),
    outlineVariant = Color(0xFFBEC9C5),

    shimmer = Color(0xFFE1E3E2),
    shimmerHighlight = Color(0xFFEFF1F0),

    bottomBar = Color(0xFFFFFFFF),
    onBottomBar = Color(0xFF191C1B),
    bottomBarIndicator = Color(0xFFCCE8E1),
    topBar = Color(0xFFF5F9F8),
    onTopBar = Color(0xFF191C1B),
)

// ──────────────────────────────────────────────────────────────────────────────
// DARK THEME (Primary: #86CDC1 - Vibrant Teal for WCAG AA)
// ──────────────────────────────────────────────────────────────────────────────
val DarkExtendedColors = ExtendedColors(
    background = Color(0xFF040606), // Deeper charcoal background
    onBackground = Color(0xFFE2E6E5),
    surface = Color(0xFF0D1211), // Elevated surface
    onSurface = Color(0xFFE2E6E5),
    dialog = Color(0xFF141A19), // Dialog container
    onDialog = Color(0xFFE2E6E5),
    card = Color(0xFF111716),
    onCard = Color(0xFFE2E6E5),

    scrim = Color(0xE6000000),

    accent = Color(0xFF00CFB5), // More vibrant 'Electric Teal'
    onAccent = Color(0xFF003730),
    accentVariant = Color(0xFF00A28E),
    accentContainer = Color(0xFF004D44),
    onAccentContainer = Color(0xFFBBECE4),

    accentSecondary = Color(0xFFB0CCC5),
    onAccentSecondary = Color(0xFF1B3530),
    accentSecondaryContainer = Color(0xFF324B46),
    onAccentSecondaryContainer = Color(0xFFCCE8E1),

    success = Color(0xFF6EDC8B), // Clearer green
    onSuccess = Color(0xFF003916),
    successContainer = Color(0xFF005221),
    onSuccessContainer = Color(0xFFC0FAD3),

    warning = Color(0xFFFFB74D),
    onWarning = Color(0xFF452700),
    warningContainer = Color(0xFF663C00),
    onWarningContainer = Color(0xFFFFE0B2),

    info = Color(0xFF4FC3F7),
    onInfo = Color(0xFF00325B),
    infoContainer = Color(0xFF004C81),
    onInfoContainer = Color(0xFFB2EBF2),

    error = Color(0xFFFFB4AB), // Material 3 Error Dark
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    heartRate = Color(0xFFFF897D),
    onHeartRate = Color(0xFF690005),
    heartRateContainer = Color(0xFF93000A),

    steps = Color(0xFF00CFB5),
    onSteps = Color(0xFF003730),
    stepsContainer = Color(0xFF004D44),

    calories = Color(0xFFFFB74D),
    onCalories = Color(0xFF452700),
    caloriesContainer = Color(0xFF663C00),

    medication = Color(0xFFD0BCFF),
    onMedication = Color(0xFF381E72),
    medicationContainer = Color(0xFF4F378B),

    sleep = Color(0xFFB4C5FF),
    onSleep = Color(0xFF1B2D61),
    sleepContainer = Color(0xFF324578),

    textPrimary = Color(0xFFE2E6E5),
    textSecondary = Color(0xFF9EA4A2),
    textTertiary = Color(0xFF707977),
    textDisabled = Color(0xFF56605D),

    disabled = Color(0xFF1A1F1E),
    onDisabled = Color(0xFF454D4C), // Slightly higher contrast but still "disabled"
    ripple = Color(0x3300CFB5),

    divider = Color(0xFF2E3634),
    outline = Color(0xFF899391),
    outlineVariant = Color(0xFF3F4947),

    shimmer = Color(0xFF141A19),
    shimmerHighlight = Color(0xFF1F2625),

    bottomBar = Color(0xFF0D1211),
    onBottomBar = Color(0xFFE2E6E5),
    bottomBarIndicator = Color(0xFF004D44),
    topBar = Color(0xFF040606),
    onTopBar = Color(0xFFE2E6E5),
)
