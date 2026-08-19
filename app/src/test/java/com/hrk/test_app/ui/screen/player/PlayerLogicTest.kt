package com.hrk.test_app.ui.screen.player

import com.hrk.test_app.data.model.LyricLine
import com.hrk.test_app.data.model.LyricWord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

class PlayerLogicTest {

    private fun formatTime(ms: Long): String {
        val totalSeconds = (ms / 1000L).coerceAtLeast(0L)
        val minutes = totalSeconds / 60L
        val seconds = totalSeconds % 60L
        return String.format(Locale.US, "%d:%02d", minutes, seconds)
    }

    private fun computeLineIndices(currentPosMs: Long, lines: List<LyricLine>): Pair<Int, Int> {
        if (lines.isEmpty()) return -1 to -1

        if (currentPosMs < lines.first().startTimeMs) {
            return 0 to (if (lines.size > 1) 1 else -1)
        }

        for (i in lines.indices) {
            val line = lines[i]
            if (currentPosMs in line.startTimeMs..line.endTimeMs) {
                val nextIdx = if (i + 1 < lines.size) i + 1 else -1
                return i to nextIdx
            }
        }

        for (i in 0 until lines.lastIndex) {
            val currentLine = lines[i]
            val nextLine = lines[i + 1]
            if (currentPosMs > currentLine.endTimeMs && currentPosMs < nextLine.startTimeMs) {
                val gap = nextLine.startTimeMs - currentLine.endTimeMs
                val switchThreshold = if (gap <= 1500L) {
                    currentLine.endTimeMs + 350L
                } else {
                    maxOf(currentLine.endTimeMs + 700L, nextLine.startTimeMs - 1200L)
                }
                return if (currentPosMs >= switchThreshold) {
                    val afterNext = if (i + 2 < lines.size) i + 2 else -1
                    (i + 1) to afterNext
                } else {
                    val nextIdx = if (i + 1 < lines.size) i + 1 else -1
                    i to nextIdx
                }
            }
        }

        return lines.lastIndex to -1
    }

    @Test
    fun testTimeFormatting() {
        assertEquals("0:00", formatTime(0L))
        assertEquals("1:17", formatTime(77000L))
        assertEquals("2:18", formatTime(138000L))
        assertEquals("4:18", formatTime(258000L))
    }

    @Test
    fun testProgressCalculation() {
        val durationMs = 258000L // 4:18
        val currentPositionMs = 77000L // 1:17

        val actualProgress = (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
        val remainingMs = (durationMs - currentPositionMs).coerceAtLeast(0L)

        assertEquals(0.2984f, actualProgress, 0.001f)
        assertEquals(181000L, remainingMs)
        assertEquals("3:01", formatTime(remainingMs))
    }

    @Test
    fun testLineIndexMatchingCurrentTime() {
        val lines = listOf(
            LyricLine(
                lineIndex = 0,
                fullText = "Hồn lỡ sa vào đôi mắt em",
                startTimeMs = 35144L,
                endTimeMs = 39000L,
                words = listOf(
                    LyricWord(text = "Hồn ", startTimeMs = 35144L, endTimeMs = 35587L),
                    LyricWord(text = "lỡ ", startTimeMs = 35587L, endTimeMs = 36006L),
                    LyricWord(text = "em", startTimeMs = 37939L, endTimeMs = 39000L)
                )
            ),
            LyricLine(
                lineIndex = 1,
                fullText = "Chiều nao xõa tóc ngồi bên rèm",
                startTimeMs = 42641L,
                endTimeMs = 47000L,
                words = listOf(
                    LyricWord(text = "Chiều ", startTimeMs = 42641L, endTimeMs = 43085L),
                    LyricWord(text = "rèm", startTimeMs = 45488L, endTimeMs = 47000L)
                )
            )
        )

        // Intro (before first line)
        val (introCurr, introNext) = computeLineIndices(10000L, lines)
        assertEquals(0, introCurr)
        assertEquals(1, introNext)

        // Mid singing line 1 (at 36.000s)
        val (line1Curr, line1Next) = computeLineIndices(36000L, lines)
        assertEquals(0, line1Curr)
        assertEquals(1, line1Next)

        // In gap between line 1 & line 2 (at 40.000s, gap is ~3.6s, threshold = 42641 - 1200 = 41441)
        val (gapEarlyCurr, gapEarlyNext) = computeLineIndices(40000L, lines)
        assertEquals(0, gapEarlyCurr)
        assertEquals(1, gapEarlyNext)

        // In gap approaching line 2 (at 41.800s, past 41.441s threshold)
        val (gapLateCurr, gapLateNext) = computeLineIndices(41800L, lines)
        assertEquals(1, gapLateCurr) // Line 2 is now active in advance!
        assertEquals(-1, gapLateNext)

        // Mid singing line 2 (at 43.000s)
        val (line2Curr, line2Next) = computeLineIndices(43000L, lines)
        assertEquals(1, line2Curr)
        assertEquals(-1, line2Next)
    }

    @Test
    fun testWordProgress() {
        val word = LyricWord(text = "Hồn ", startTimeMs = 35144L, endTimeMs = 35644L)

        // Before word starts
        assertEquals(0.0f, word.getProgress(35000L), 0.001f)
        assertFalse(word.isCurrent(35000L))

        // Halfway through word
        assertEquals(0.5f, word.getProgress(35394L), 0.01f)
        assertTrue(word.isCurrent(35394L))

        // Finished word
        assertEquals(1.0f, word.getProgress(36000L), 0.001f)
        assertTrue(word.isPast(36000L))
    }
}

