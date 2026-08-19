package com.hrk.test_app.ui.screen.player.model

import com.hrk.test_app.data.model.LyricDisplayMode

sealed interface PlayerEvent {
    data object PlayPause : PlayerEvent
    data object Stop : PlayerEvent
    data class SeekTo(val positionMs: Long) : PlayerEvent
    data class SeekProgress(val progress: Float) : PlayerEvent
    data object NextTrack : PlayerEvent
    data object PreviousTrack : PlayerEvent
    data object ToggleShuffle : PlayerEvent
    data object ToggleRepeat : PlayerEvent
    data class SetLyricMode(val mode: LyricDisplayMode) : PlayerEvent
    data object ToggleModeSelector : PlayerEvent
    data object DismissModeSelector : PlayerEvent
    data object RetryLoad : PlayerEvent
}
