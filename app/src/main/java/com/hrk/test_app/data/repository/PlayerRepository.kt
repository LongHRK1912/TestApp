package com.hrk.test_app.data.repository

import android.content.Context
import com.hrk.test_app.data.model.LyricSong
import com.hrk.test_app.data.model.RepeatMode
import com.hrk.test_app.data.parser.LyricsXmlParser
import com.hrk.test_app.data.service.AudioPlayerManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

interface PlayerRepository {
    val currentPositionMs: StateFlow<Long>
    val durationMs: StateFlow<Long>
    val isPlaying: StateFlow<Boolean>
    val isBuffering: StateFlow<Boolean>
    val isShuffle: StateFlow<Boolean>
    val repeatMode: StateFlow<RepeatMode>
    val errorMessage: StateFlow<String?>

    fun getLyrics(): Flow<Result<LyricSong>>
    fun prepareAudio(url: String = DEFAULT_BEAT_URL)
    fun play()
    fun pause()
    fun stop()
    fun togglePlayPause()
    fun seekTo(positionMs: Long)
    fun toggleShuffle()
    fun toggleRepeat()

    companion object {
        const val DEFAULT_BEAT_URL = "https://storage.googleapis.com/ikara-storage/tmp/beat.mp3"
        const val DEFAULT_LYRICS_URL = "https://storage.googleapis.com/ikara-storage/ikara/lyrics.xml"
    }
}

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioPlayerManager: AudioPlayerManager,
    private val lyricsParser: LyricsXmlParser,
) : PlayerRepository {

    override val currentPositionMs: StateFlow<Long> = audioPlayerManager.currentPositionMs
    override val durationMs: StateFlow<Long> = audioPlayerManager.durationMs
    override val isPlaying: StateFlow<Boolean> = audioPlayerManager.isPlaying
    override val isBuffering: StateFlow<Boolean> = audioPlayerManager.isBuffering
    override val isShuffle: StateFlow<Boolean> = audioPlayerManager.isShuffle
    override val repeatMode: StateFlow<RepeatMode> = audioPlayerManager.repeatMode
    override val errorMessage: StateFlow<String?> = audioPlayerManager.errorMessage

    override fun getLyrics(): Flow<Result<LyricSong>> = flow {
        // First try remote network
        try {
            val url = URL(PlayerRepository.DEFAULT_LYRICS_URL)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 8000
                readTimeout = 8000
                requestMethod = "GET"
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.use { input ->
                    val song = lyricsParser.parse(input)
                    emit(Result.success(song))
                    return@flow
                }
            }
        } catch (_: Exception) {
            // Fallback to local bundled asset
        }

        // Fallback to bundled asset
        try {
            context.assets.open("lyrics.xml").use { input ->
                val song = lyricsParser.parse(input)
                emit(Result.success(song))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun prepareAudio(url: String) {
        audioPlayerManager.prepareAudio(url)
    }

    override fun play() {
        audioPlayerManager.play()
    }

    override fun pause() {
        audioPlayerManager.pause()
    }

    override fun stop() {
        audioPlayerManager.stop()
    }

    override fun togglePlayPause() {
        audioPlayerManager.togglePlayPause()
    }

    override fun seekTo(positionMs: Long) {
        audioPlayerManager.seekTo(positionMs)
    }

    override fun toggleShuffle() {
        audioPlayerManager.toggleShuffle()
    }

    override fun toggleRepeat() {
        audioPlayerManager.toggleRepeat()
    }
}
