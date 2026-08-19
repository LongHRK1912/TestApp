package com.hrk.test_app.data.model

import androidx.annotation.StringRes
import com.hrk.test_app.R

/**
 * Represents a single syllable or word in the karaoke lyrics.
 */
data class LyricWord(
    val text: String,
    val startTimeMs: Long,
    val endTimeMs: Long,
) {
    val durationMs: Long
        get() = (endTimeMs - startTimeMs).coerceAtLeast(1L)

    /**
     * Calculates the singing progress for this specific word at given playback timestamp [currentTimeMs].
     * Returns a float in [0.0f, 1.0f].
     */
    fun getProgress(currentTimeMs: Long): Float {
        if (currentTimeMs <= startTimeMs) return 0f
        if (currentTimeMs >= endTimeMs) return 1f
        return ((currentTimeMs - startTimeMs).toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
    }

    fun isCurrent(currentTimeMs: Long): Boolean {
        return currentTimeMs in startTimeMs..endTimeMs
    }

    fun isPast(currentTimeMs: Long): Boolean {
        return currentTimeMs > endTimeMs
    }
}


/**
 * Represents a full line in the lyrics containing multiple words.
 */
data class LyricLine(
    val lineIndex: Int,
    val words: List<LyricWord>,
    val startTimeMs: Long,
    val endTimeMs: Long,
    val fullText: String,
) {
    val durationMs: Long
        get() = (endTimeMs - startTimeMs).coerceAtLeast(1L)

    /**
     * Checks if the line is currently active at [currentTimeMs].
     */
    fun isCurrent(currentTimeMs: Long): Boolean {
        return currentTimeMs in startTimeMs..endTimeMs
    }

    /**
     * Checks if the line has already finished singing at [currentTimeMs].
     */
    fun isPast(currentTimeMs: Long): Boolean {
        return currentTimeMs > endTimeMs
    }
}

/**
 * Complete song lyrics containing all lines.
 */
data class LyricSong(
    val title: String = "Về đâu mái tóc người thương",
    val artist: String = "Quang Lê",
    val lines: List<LyricLine> = emptyList(),
)

/**
 * Supported Lyric display modes mapping to the requirements in the dev test.
 */
enum class LyricDisplayMode(
    @StringRes val titleRes: Int,
    @StringRes val shortTitleRes: Int
) {
    /**
     * Yêu cầu 4: Tô màu mượt mà trong từng ký tự theo thời gian thực (Sub-character smooth sweep).
     */
    Smooth(R.string.lyric_mode_smooth, R.string.lyric_mode_smooth_short),

    /**
     * Yêu cầu 3: Tô màu từng từ / ký tự tức thì khi hát tới (Word/Character by character).
     */
    WordByWord(R.string.lyric_mode_word, R.string.lyric_mode_word_short),

    /**
     * Yêu cầu 2: Tô màu toàn bộ dòng khi hát tới dòng đó (Line by line).
     */
    LineByLine(R.string.lyric_mode_line, R.string.lyric_mode_line_short),
}

/**
 * Repeat modes for audio playback.
 */
enum class RepeatMode {
    Off,
    One,
    All
}
