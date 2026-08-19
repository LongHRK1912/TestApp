package com.hrk.test_app.ui.screen.player.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hrk.test_app.data.model.LyricDisplayMode
import com.hrk.test_app.data.model.LyricLine
import com.hrk.test_app.data.model.LyricSong
import com.hrk.test_app.data.model.LyricWord

// Apple Fluid Motion Easing Curve
private val FluidEaseOut = CubicBezierEasing(0.22f, 1.0f, 0.36f, 1.0f)
private val FluidEaseIn = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)

/**
 * 2-Line Karaoke Lyrics View (Zero-Layout-Shift Edition)
 * - Rock-solid fixed slot heights: 1-line and multi-line lyrics transition seamlessly with ZERO jump/jitter
 * - Pure fluid vertical slide-up & scale transitions
 * - Line 1: Active Karaoke highlight (Smooth / Word / Line)
 * - Line 2: Upcoming preview line
 */
@Composable
fun KaraokeLyricsView(
    lyricSong: LyricSong?,
    currentPositionMs: Long,
    currentLineIndex: Int,
    nextLineIndex: Int,
    lyricMode: LyricDisplayMode,
    onLineClick: ((Long) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val lines = lyricSong?.lines.orEmpty()
    val currentLine = if (currentLineIndex in lines.indices) lines[currentLineIndex] else null
    val nextLine = if (nextLineIndex in lines.indices) lines[nextLineIndex] else null

    // Fluid motion transition with zero layout shift
    val transitionSpec = (
        slideInVertically(
            animationSpec = tween(durationMillis = 420, easing = FluidEaseOut)
        ) { fullHeight -> (fullHeight * 0.60f).toInt() } +
        scaleIn(
            initialScale = 0.96f,
            animationSpec = tween(durationMillis = 420, easing = FluidEaseOut)
        ) +
        fadeIn(
            animationSpec = tween(durationMillis = 340, easing = LinearOutSlowInEasing)
        )
    ) togetherWith (
        slideOutVertically(
            animationSpec = tween(durationMillis = 300, easing = FluidEaseIn)
        ) { fullHeight -> -(fullHeight * 0.50f).toInt() } +
        scaleOut(
            targetScale = 0.97f,
            animationSpec = tween(durationMillis = 300, easing = FluidEaseIn)
        ) +
        fadeOut(
            animationSpec = tween(durationMillis = 240, easing = FastOutLinearInEasing)
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(112.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ── Line 1: Current Playing Line (Stable 56dp height slot) ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = currentLine,
                transitionSpec = { transitionSpec },
                contentAlignment = Alignment.Center,
                label = "CurrentLineScrollUpAnimation"
            ) { activeLine ->
                if (activeLine != null) {
                    ActiveLyricLineRow(
                        line = activeLine,
                        currentPositionMs = currentPositionMs,
                        lyricMode = lyricMode,
                        fontSize = 17.sp,
                        lineHeight = 23.sp,
                        highlightColor = Color(0xFF16151E),
                        unplayedColor = Color.White.copy(alpha = 0.95f),
                        onLineClick = onLineClick
                    )
                } else {
                    Text(
                        text = "…",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }

        // ── Line 2: Upcoming Next Line (Stable 46dp height slot) ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = nextLine,
                transitionSpec = { transitionSpec },
                contentAlignment = Alignment.Center,
                label = "NextLineScrollUpAnimation"
            ) { upcomingLine ->
                if (upcomingLine != null) {
                    Text(
                        text = upcomingLine.fullText.trim(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.80f),
                            textAlign = TextAlign.Center
                        ),
                        maxLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onLineClick?.invoke(upcomingLine.startTimeMs)
                            }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ActiveLyricLineRow(
    line: LyricLine,
    currentPositionMs: Long,
    lyricMode: LyricDisplayMode,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    highlightColor: Color,
    unplayedColor: Color,
    onLineClick: ((Long) -> Unit)?,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onLineClick?.invoke(line.startTimeMs)
            },
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center
    ) {
        when (lyricMode) {
            LyricDisplayMode.Smooth -> {
                // Yêu cầu 4: Tô màu mượt mà trong từng ký tự theo thời gian thực (60fps sub-character sweep)
                line.words.forEach { word ->
                    SmoothKaraokeWord(
                        word = word,
                        currentPositionMs = currentPositionMs,
                        fontSize = fontSize,
                        lineHeight = lineHeight,
                        highlightColor = highlightColor,
                        unplayedColor = unplayedColor
                    )
                }
            }

            LyricDisplayMode.WordByWord -> {
                // Yêu cầu 3: Tô màu theo từng từ / ký tự tức thì
                line.words.forEach { word ->
                    val isPastOrActive = currentPositionMs >= word.startTimeMs
                    Text(
                        text = word.text,
                        style = TextStyle(
                            fontSize = fontSize,
                            lineHeight = lineHeight,
                            fontWeight = if (isPastOrActive) FontWeight.ExtraBold else FontWeight.Bold,
                            color = if (isPastOrActive) highlightColor else unplayedColor,
                            letterSpacing = 0.sp
                        )
                    )
                }
            }

            LyricDisplayMode.LineByLine -> {
                // Yêu cầu 2: Tô màu theo từng dòng (Khi bài hát phát tới dòng nào thì tô màu dòng đó)
                val isLineActive = currentPositionMs >= line.startTimeMs
                Text(
                    text = line.fullText,
                    style = TextStyle(
                        fontSize = fontSize,
                        lineHeight = lineHeight,
                        fontWeight = if (isLineActive) FontWeight.ExtraBold else FontWeight.Bold,
                        color = if (isLineActive) highlightColor else unplayedColor,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.sp
                    )
                )
            }
        }
    }
}

/**
 * Renders a single word with dual-layer text and smooth clipRect fill based on millisecond progress.
 */
@Composable
private fun SmoothKaraokeWord(
    word: LyricWord,
    currentPositionMs: Long,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    highlightColor: Color,
    unplayedColor: Color,
    modifier: Modifier = Modifier
) {
    val progress = word.getProgress(currentPositionMs)

    val baseStyle = remember(fontSize, lineHeight, unplayedColor) {
        TextStyle(
            fontSize = fontSize,
            lineHeight = lineHeight,
            fontWeight = FontWeight.Bold,
            color = unplayedColor,
            letterSpacing = 0.sp
        )
    }

    val highlightStyle = remember(fontSize, lineHeight, highlightColor) {
        TextStyle(
            fontSize = fontSize,
            lineHeight = lineHeight,
            fontWeight = FontWeight.Bold,
            color = highlightColor,
            letterSpacing = 0.sp
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        // Base Layer (Unplayed text)
        Text(
            text = word.text,
            style = baseStyle
        )

        // Highlight Layer with Progressive Clip Rect (Played text)
        if (progress > 0f) {
            Text(
                text = word.text,
                style = highlightStyle,
                modifier = Modifier.drawWithContent {
                    clipRect(
                        left = 0f,
                        top = 0f,
                        right = size.width * progress,
                        bottom = size.height
                    ) {
                        this@drawWithContent.drawContent()
                    }
                }
            )
        }
    }
}
