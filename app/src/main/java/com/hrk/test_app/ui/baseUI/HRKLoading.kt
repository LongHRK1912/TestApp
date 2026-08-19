package com.hrk.test_app.ui.baseUI

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hrk.test_app.R
import com.hrk.test_app.common.language.localizedString
import com.hrk.test_app.ui.theme.LocalExtendedColors
import kotlinx.coroutines.launch

sealed interface ShowMode {
    data object FullScreen : ShowMode
    data class Component(
        val modifier: Modifier
    ) : ShowMode
}

@Composable
fun HRKLoading(
    mode: ShowMode = ShowMode.FullScreen,
    contentDesc: String = localizedString(R.string.please_wait),
) {
    val colors = LocalExtendedColors.current
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val startValue = if (LocalInspectionMode.current) 0F else 1F
    val floatAnimValues = (0 until NUM_OF_LINES).map { remember { Animatable(startValue) } }
    LaunchedEffect(floatAnimValues) {
        (0 until NUM_OF_LINES).map { index ->
            launch {
                floatAnimValues[index].animateTo(
                    targetValue = 0F,
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = FastOutSlowInEasing,
                        delayMillis = 40 * index
                    )
                )
            }
        }
    }

    val rotationAnim by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ROTATION_TIME, easing = LinearEasing)
        ),
        label = ""
    )

    val baseLineColor = colors.onDisabled
    val progressLineColor = colors.accent

    val colorAnimValues = (0 until NUM_OF_LINES).map { index ->
        infiniteTransition.animateColor(
            initialValue = baseLineColor,
            targetValue = baseLineColor,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = ROTATION_TIME / 2
                    progressLineColor at ROTATION_TIME / NUM_OF_LINES / 2 using LinearEasing
                    baseLineColor at ROTATION_TIME / NUM_OF_LINES using LinearEasing
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(ROTATION_TIME / NUM_OF_LINES / 2 * index)
            ),
            label = ""
        )
    }

    when (mode) {
        is ShowMode.Component -> {
            Box(modifier = mode.modifier, contentAlignment = Alignment.Center) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .graphicsLayer { rotationZ = rotationAnim }
                        .semantics { contentDescription = contentDesc }
                        .testTag("loadingWheel")
                ) {
                    repeat(NUM_OF_LINES) { index ->
                        rotate(degrees = index * 30f) {
                            drawLine(
                                color = colorAnimValues[index].value,
                                alpha = if (floatAnimValues[index].value < 1f) 1f else 0f,
                                strokeWidth = 4F,
                                cap = StrokeCap.Round,
                                start = Offset(size.width / 2, size.height / 4),
                                end = Offset(
                                    size.width / 2,
                                    floatAnimValues[index].value * size.height / 4
                                )
                            )
                        }
                    }
                }
            }
        }

        ShowMode.FullScreen -> {
            Dialog(
                onDismissRequest = { },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false,
                    decorFitsSystemWindows = false,
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(8.dp)
                            .graphicsLayer { rotationZ = rotationAnim }
                            .semantics { contentDescription = contentDesc }
                            .testTag("loadingWheel")
                    ) {
                        repeat(NUM_OF_LINES) { index ->
                            rotate(degrees = index * 30f) {
                                drawLine(
                                    color = colorAnimValues[index].value,
                                    alpha = if (floatAnimValues[index].value < 1f) 1f else 0f,
                                    strokeWidth = 4F,
                                    cap = StrokeCap.Round,
                                    start = Offset(size.width / 2, size.height / 4),
                                    end = Offset(
                                        size.width / 2,
                                        floatAnimValues[index].value * size.height / 4
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val ROTATION_TIME = 15000
private const val NUM_OF_LINES = 15
