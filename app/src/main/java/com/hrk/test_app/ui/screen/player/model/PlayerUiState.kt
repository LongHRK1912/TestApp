package com.hrk.test_app.ui.screen.player.model

import com.hrk.test_app.data.model.LyricDisplayMode
import com.hrk.test_app.data.model.LyricSong
import com.hrk.test_app.data.model.RepeatMode

data class PlayerUiState(
    val isLoading: Boolean = true,
    val songTitle: String = "Về đâu mái tóc người thương",
    val artistName: String = "Quang Lê",
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val isShuffle: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.Off,
    val lyricSong: LyricSong? = null,
    val currentLineIndex: Int = -1,
    val nextLineIndex: Int = -1,
    val lyricMode: LyricDisplayMode = LyricDisplayMode.Smooth,
    val isModeSelectorVisible: Boolean = false,
    val errorMessage: String? = null,
) {
    /**
     * Percentage progress [0f, 1f] of the entire song.
     */
    val playbackProgress: Float
        get() = if (durationMs > 0L) (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f
}
