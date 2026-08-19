package com.hrk.test_app.ui.screen.player.view.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive

/**
 * Vinyl Turntable with:
 * - Continuous smooth rotation when playing (retaining angle when paused)
 * - Clear rotating vinyl grooves, specular arc reflections, and center label graphics
 * - Animated tonearm stylus needle that rests on the disc when playing
 */
@Composable
fun VinylTurntable(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    turntableSize: Dp = 255.dp,
) {
    // Rotation state accumulator: keeps continuous rotation without resetting on pause/resume
    val rotationAnimatable = remember { Animatable(0f) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isActive) {
                val current = rotationAnimatable.value
                rotationAnimatable.animateTo(
                    targetValue = current + 360f,
                    animationSpec = tween(durationMillis = 6000, easing = LinearEasing)
                )
            }
        }
    }

    // Tonearm needle angle animation: moves onto record when playing, resets when paused
    val tonearmAngle by animateFloatAsState(
        targetValue = if (isPlaying) 28f else 0f,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        label = "TonearmAngle"
    )

    Box(
        modifier = modifier
            .size(turntableSize)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            val center = Offset(size.width * 0.46f, size.height * 0.50f)
            val radius = minOf(size.width, size.height) * 0.44f

            // Rotating Vinyl Disc
            rotate(rotationAnimatable.value % 360f, pivot = center) {
                drawVinylDisc(center, radius)
            }

            // Stationary Tonearm Stylus on Top
            drawTonearm(
                pivot = Offset(size.width * 0.88f, size.height * 0.16f),
                angle = tonearmAngle
            )
        }
    }
}

private fun DrawScope.drawVinylDisc(
    center: Offset,
    radius: Float,
) {
    // 1. Outer Dark Vinyl Body
    drawCircle(
        color = Color(0xFF16161A),
        radius = radius,
        center = center,
        style = Fill
    )

    // 2. Concentric Grooves
    val grooveCount = 8
    for (i in 1..grooveCount) {
        val r = radius * (0.38f + (i.toFloat() / grooveCount) * 0.56f)
        drawCircle(
            color = Color(0x22FFFFFF),
            radius = r,
            center = center,
            style = Stroke(width = 1.2f)
        )
    }

    // 3. Rotating Light Reflection Arcs (Makes rotation clearly visible!)
    val arcRect = androidx.compose.ui.geometry.Rect(
        center.x - radius * 0.85f,
        center.y - radius * 0.85f,
        center.x + radius * 0.85f,
        center.y + radius * 0.85f
    )
    drawArc(
        color = Color(0x38FFFFFF),
        startAngle = 25f,
        sweepAngle = 45f,
        useCenter = false,
        topLeft = arcRect.topLeft,
        size = arcRect.size,
        style = Stroke(width = 18f, cap = StrokeCap.Round)
    )
    drawArc(
        color = Color(0x38FFFFFF),
        startAngle = 205f,
        sweepAngle = 45f,
        useCenter = false,
        topLeft = arcRect.topLeft,
        size = arcRect.size,
        style = Stroke(width = 18f, cap = StrokeCap.Round)
    )

    // Secondary smaller highlight arcs
    val innerArcRect = androidx.compose.ui.geometry.Rect(
        center.x - radius * 0.60f,
        center.y - radius * 0.60f,
        center.x + radius * 0.60f,
        center.y + radius * 0.60f
    )
    drawArc(
        color = Color(0x28FFFFFF),
        startAngle = 70f,
        sweepAngle = 35f,
        useCenter = false,
        topLeft = innerArcRect.topLeft,
        size = innerArcRect.size,
        style = Stroke(width = 12f, cap = StrokeCap.Round)
    )
    drawArc(
        color = Color(0x28FFFFFF),
        startAngle = 250f,
        sweepAngle = 35f,
        useCenter = false,
        topLeft = innerArcRect.topLeft,
        size = innerArcRect.size,
        style = Stroke(width = 12f, cap = StrokeCap.Round)
    )

    // 4. Center Label Circle (Pastel purple-blue gradient)
    val labelRadius = radius * 0.36f
    val labelBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFFB5A4E8),
            Color(0xFF8E9BEB),
            Color(0xFF6B8CEF)
        ),
        center = center,
        radius = labelRadius
    )
    drawCircle(
        brush = labelBrush,
        radius = labelRadius,
        center = center,
        style = Fill
    )

    // 5. Center Label Rotating Notches / Ring
    drawCircle(
        color = Color(0x40FFFFFF),
        radius = labelRadius * 0.68f,
        center = center,
        style = Stroke(width = 2f)
    )

    // Rotating decorative dots on the label
    val dotRadius = labelRadius * 0.50f
    for (deg in listOf(0f, 90f, 180f, 270f)) {
        val rad = Math.toRadians(deg.toDouble())
        val dotX = center.x + (dotRadius * Math.cos(rad)).toFloat()
        val dotY = center.y + (dotRadius * Math.sin(rad)).toFloat()
        drawCircle(
            color = Color(0x80FFFFFF),
            radius = 2.5f,
            center = Offset(dotX, dotY)
        )
    }

    // 6. Center Spindle Hole
    drawCircle(
        color = Color(0xFF16161A),
        radius = radius * 0.075f,
        center = center,
        style = Fill
    )
    drawCircle(
        color = Color(0x90FFFFFF),
        radius = radius * 0.075f,
        center = center,
        style = Stroke(width = 1.5f)
    )
}

private fun DrawScope.drawTonearm(
    pivot: Offset,
    angle: Float
) {
    rotate(angle, pivot = pivot) {
        // Base Turntable Pivot Base
        drawCircle(
            color = Color.White,
            radius = 8.dp.toPx(),
            center = pivot
        )
        drawCircle(
            color = Color(0x40000000),
            radius = 4.dp.toPx(),
            center = pivot
        )

        // Tonearm Arm Line with elbow
        val elbow = Offset(pivot.x - 22.dp.toPx(), pivot.y + 36.dp.toPx())
        val head = Offset(elbow.x - 30.dp.toPx(), elbow.y + 32.dp.toPx())

        val armPath = Path().apply {
            moveTo(pivot.x, pivot.y)
            lineTo(elbow.x, elbow.y)
            lineTo(head.x, head.y)
        }

        // Arm Shadow
        drawPath(
            path = armPath,
            color = Color(0x30000000),
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )

        // Arm Body
        drawPath(
            path = armPath,
            color = Color.White,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Stylus Cartridge Head
        drawCircle(
            color = Color.White,
            radius = 5.dp.toPx(),
            center = head
        )
        drawCircle(
            color = Color(0xFF16161A),
            radius = 2.5.dp.toPx(),
            center = head
        )
    }
}
