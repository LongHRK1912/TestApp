package com.hrk.test_app.ui.screen.player.view.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

/**
 * Custom Seamless Seekbar & Timestamps (UI/UX Pro Max Edition)
 * - Continuous, seamless pill track with zero gaps or stop artifacts
 * - Smooth tap-to-seek and drag gestures with reactive thumb scaling
 * - Left elapsed time and right remaining time
 */
@Composable
fun PlayerProgressBar(
    currentPositionMs: Long,
    durationMs: Long,
    onSeekTo: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDragging by remember { mutableStateOf(false) }
    var dragFraction by remember { mutableFloatStateOf(0f) }

    val actualProgress = if (durationMs > 0L) {
        (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val displayProgress = if (isDragging) dragFraction else actualProgress

    val currentDisplayMs = if (isDragging) {
        (dragFraction * durationMs).toLong()
    } else currentPositionMs

    val remainingMs = (durationMs - currentDisplayMs).coerceAtLeast(0L)

    val animatedThumbRadius by animateDpAsState(
        targetValue = if (isDragging) 7.dp else 5.5.dp,
        animationSpec = tween(150),
        label = "ThumbSize"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Custom Seamless Gesture Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .pointerInput(durationMs) {
                    detectTapGestures { offset ->
                        val fraction = (offset.x / size.width.toFloat()).coerceIn(0f, 1f)
                        val targetMs = (fraction * durationMs).toLong()
                        onSeekTo(targetMs)
                    }
                }
                .pointerInput(durationMs) {
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            isDragging = true
                            dragFraction = (offset.x / size.width.toFloat()).coerceIn(0f, 1f)
                        },
                        onDragEnd = {
                            val targetMs = (dragFraction * durationMs).toLong()
                            onSeekTo(targetMs)
                            isDragging = false
                        },
                        onDragCancel = {
                            isDragging = false
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            val deltaFraction = dragAmount / size.width.toFloat()
                            dragFraction = (dragFraction + deltaFraction).coerceIn(0f, 1f)
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            ) {
                val trackHeight = 4.dp.toPx()
                val centerY = size.height / 2f
                val totalWidth = size.width
                val progressX = (totalWidth * displayProgress).coerceIn(0f, totalWidth)

                // 1. Inactive Background Track (Full width soft translucent white line)
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.40f),
                    topLeft = Offset(0f, centerY - trackHeight / 2f),
                    size = Size(totalWidth, trackHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(trackHeight / 2f, trackHeight / 2f)
                )

                // 2. Active Played Track (Seamless solid black line)
                if (progressX > 0f) {
                    drawRoundRect(
                        color = Color(0xFF16161A),
                        topLeft = Offset(0f, centerY - trackHeight / 2f),
                        size = Size(progressX, trackHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(trackHeight / 2f, trackHeight / 2f)
                    )
                }

                // 3. Thumb Knob (Smooth solid black circle with crisp finish)
                val thumbRadiusPx = animatedThumbRadius.toPx()
                drawCircle(
                    color = Color(0xFF16161A),
                    radius = thumbRadiusPx,
                    center = Offset(progressX, centerY)
                )
            }
        }

        // Timestamps (Current Position & Remaining Duration)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Elapsed time
            Text(
                text = formatTime(currentDisplayMs),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            )

            // Remaining time (-m:ss)
            Text(
                text = "-${formatTime(remainingMs)}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            )
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000L).coerceAtLeast(0L)
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    return String.format(Locale.US, "%d:%02d", minutes, seconds)
}
