package com.hrk.test_app.ui.screen.player

import com.hrk.test_app.data.model.LyricDisplayMode
import com.hrk.test_app.data.model.LyricSong
import com.hrk.test_app.data.repository.PlayerRepository
import com.hrk.test_app.ui.baseUI.viewmodel.BaseViewModel
import com.hrk.test_app.ui.screen.player.model.PlayerEvent
import com.hrk.test_app.ui.screen.player.model.PlayerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: PlayerRepository
) : BaseViewModel<PlayerUiState, PlayerEvent>(PlayerUiState()) {

    init {
        loadLyricsAndAudio()
        observePlayerState()
    }

    override fun handleEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.PlayPause -> repository.togglePlayPause()
            is PlayerEvent.Stop -> repository.stop()
            is PlayerEvent.SeekTo -> repository.seekTo(event.positionMs)
            is PlayerEvent.SeekProgress -> {
                val targetMs = (event.progress * uiState.value.durationMs.coerceAtLeast(1L)).toLong()
                repository.seekTo(targetMs)
            }
            is PlayerEvent.NextTrack -> {
                // Seek to start or replay
                repository.seekTo(0L)
            }
            is PlayerEvent.PreviousTrack -> {
                repository.seekTo(0L)
            }
            is PlayerEvent.ToggleShuffle -> repository.toggleShuffle()
            is PlayerEvent.ToggleRepeat -> repository.toggleRepeat()
            is PlayerEvent.SetLyricMode -> {
                updateUiState { it.copy(lyricMode = event.mode, isModeSelectorVisible = false) }
            }
            is PlayerEvent.ToggleModeSelector -> {
                updateUiState { it.copy(isModeSelectorVisible = !it.isModeSelectorVisible) }
            }
            is PlayerEvent.DismissModeSelector -> {
                updateUiState { it.copy(isModeSelectorVisible = false) }
            }
            is PlayerEvent.RetryLoad -> loadLyricsAndAudio()
        }
    }

    private fun loadLyricsAndAudio() = launch {
        updateUiState { it.copy(isLoading = true, errorMessage = null) }

        // Start loading audio
        repository.prepareAudio()

        // Fetch lyrics
        repository.getLyrics().collectLatest { result ->
            result.onSuccess { song ->
                updateUiState {
                    it.copy(
                        isLoading = false,
                        lyricSong = song,
                        songTitle = song.title,
                        artistName = song.artist,
                        currentLineIndex = if (song.lines.isNotEmpty()) 0 else -1,
                        nextLineIndex = if (song.lines.size > 1) 1 else -1,
                    )
                }
            }.onFailure { error ->
                updateUiState {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage
                    )
                }
            }
        }
    }

    private fun observePlayerState() {
        launch {
            repository.currentPositionMs.collectLatest { pos ->
                val lines = uiState.value.lyricSong?.lines.orEmpty()
                val (currIdx, nextIdx) = computeLineIndices(pos, lines)
                updateUiState {
                    it.copy(
                        currentPositionMs = pos,
                        currentLineIndex = currIdx,
                        nextLineIndex = nextIdx
                    )
                }
            }
        }

        launch {
            repository.durationMs.collectLatest { duration ->
                if (duration > 0L) {
                    updateUiState { it.copy(durationMs = duration) }
                }
            }
        }

        launch {
            repository.isPlaying.collectLatest { playing ->
                updateUiState { it.copy(isPlaying = playing) }
            }
        }

        launch {
            repository.isBuffering.collectLatest { buffering ->
                updateUiState { it.copy(isBuffering = buffering) }
            }
        }

        launch {
            repository.isShuffle.collectLatest { shuffle ->
                updateUiState { it.copy(isShuffle = shuffle) }
            }
        }

        launch {
            repository.repeatMode.collectLatest { repeat ->
                updateUiState { it.copy(repeatMode = repeat) }
            }
        }

        launch {
            repository.errorMessage.collectLatest { error ->
                if (error != null) {
                    updateUiState { it.copy(errorMessage = error) }
                }
            }
        }
    }

    private fun computeLineIndices(currentPosMs: Long, lines: List<com.hrk.test_app.data.model.LyricLine>): Pair<Int, Int> {
        if (lines.isEmpty()) return -1 to -1

        // Before first line starts
        if (currentPosMs < lines.first().startTimeMs) {
            return 0 to (if (lines.size > 1) 1 else -1)
        }

        // Active line search
        for (i in lines.indices) {
            val line = lines[i]
            if (currentPosMs in line.startTimeMs..line.endTimeMs) {
                val nextIdx = if (i + 1 < lines.size) i + 1 else -1
                return i to nextIdx
            }
        }

        // Gap between lines
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

        // Past the last line
        return lines.lastIndex to -1
    }
}


