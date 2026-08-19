package com.hrk.test_app.data.service

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.hrk.test_app.data.model.RepeatMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var exoPlayer: ExoPlayer? = null
    private var positionPollingJob: Job? = null

    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()

    private val _repeatMode = MutableStateFlow(RepeatMode.Off)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        if (exoPlayer != null) return

        exoPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> {
                            _isBuffering.value = true
                        }
                        Player.STATE_READY -> {
                            _isBuffering.value = false
                            _durationMs.value = duration.coerceAtLeast(0L)
                        }
                        Player.STATE_ENDED -> {
                            _isPlaying.value = false
                            _isBuffering.value = false
                            when (_repeatMode.value) {
                                RepeatMode.One -> {
                                    seekTo(0L)
                                    play()
                                }
                                RepeatMode.All -> {
                                    seekTo(0L)
                                    play()
                                }
                                RepeatMode.Off -> {
                                    seekTo(0L)
                                }
                            }
                        }
                        Player.STATE_IDLE -> {
                            _isBuffering.value = false
                        }
                    }
                }

                override fun onIsPlayingChanged(playing: Boolean) {
                    _isPlaying.value = playing
                    if (playing) {
                        startPositionPolling()
                    } else {
                        stopPositionPolling()
                        // Ensure final position is recorded
                        exoPlayer?.let { _currentPositionMs.value = it.currentPosition.coerceAtLeast(0L) }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    _isBuffering.value = false
                    _isPlaying.value = false
                    _errorMessage.value = error.localizedMessage
                    // If remote fails, try fallback to asset
                    tryAssetFallback()
                }
            })
        }
    }

    fun prepareAudio(url: String, fallbackAssetPath: String = "beat.mp3", autoPlay: Boolean = true) {
        initializePlayer()
        scope.launch {
            try {
                val mediaItem = MediaItem.fromUri(Uri.parse(url))
                exoPlayer?.apply {
                    setMediaItem(mediaItem)
                    playWhenReady = autoPlay
                    prepare()
                }
            } catch (e: Exception) {
                tryAssetFallback(fallbackAssetPath, autoPlay)
            }
        }
    }

    private fun tryAssetFallback(assetPath: String = "beat.mp3", autoPlay: Boolean = true) {
        try {
            val assetUri = Uri.parse("asset:///$assetPath")
            val mediaItem = MediaItem.fromUri(assetUri)
            exoPlayer?.apply {
                setMediaItem(mediaItem)
                playWhenReady = autoPlay
                prepare()
            }
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load audio: ${e.localizedMessage}"
        }
    }

    fun play() {
        exoPlayer?.play()
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun stop() {
        exoPlayer?.pause()
        seekTo(0L)
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            pause()
        } else {
            play()
        }
    }

    fun seekTo(positionMs: Long) {
        val target = positionMs.coerceIn(0L, _durationMs.value.coerceAtLeast(1L))
        _currentPositionMs.value = target
        exoPlayer?.seekTo(target)
    }

    fun toggleShuffle() {
        _isShuffle.value = !_isShuffle.value
    }

    fun toggleRepeat() {
        _repeatMode.value = when (_repeatMode.value) {
            RepeatMode.Off -> RepeatMode.All
            RepeatMode.All -> RepeatMode.One
            RepeatMode.One -> RepeatMode.Off
        }
    }

    private fun startPositionPolling() {
        positionPollingJob?.cancel()
        positionPollingJob = scope.launch {
            while (isActive) {
                exoPlayer?.let { player ->
                    if (player.isPlaying) {
                        _currentPositionMs.value = player.currentPosition.coerceAtLeast(0L)
                    }
                }
                delay(POSITION_UPDATE_INTERVAL_MS)
            }
        }
    }

    private fun stopPositionPolling() {
        positionPollingJob?.cancel()
        positionPollingJob = null
    }

    fun release() {
        stopPositionPolling()
        exoPlayer?.release()
        exoPlayer = null
    }

    companion object {
        // 16ms = ~60 updates per second for ultra-smooth karaoke color fill animation
        private const val POSITION_UPDATE_INTERVAL_MS = 16L
    }
}
