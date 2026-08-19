package com.hrk.test_app.ui.screen.player.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrk.test_app.R
import com.hrk.test_app.common.language.localizedString
import com.hrk.test_app.ui.baseUI.BackHandlerStyle
import com.hrk.test_app.ui.baseUI.BaseScreen
import com.hrk.test_app.ui.screen.player.PlayerViewModel
import com.hrk.test_app.ui.screen.player.model.PlayerEvent
import com.hrk.test_app.ui.screen.player.view.components.KaraokeLyricsView
import com.hrk.test_app.ui.screen.player.view.components.LyricModeSelector
import com.hrk.test_app.ui.screen.player.view.components.PlayerControls
import com.hrk.test_app.ui.screen.player.view.components.PlayerProgressBar
import com.hrk.test_app.ui.screen.player.view.components.VinylTurntable
import com.hrk.test_app.ui.theme.LocalExtendedColors

/**
 * Root Player View complying with agent.md guidelines.
 * Wraps content in BaseScreen and Scaffold, using LocalExtendedColors and localizedString.
 */
@Composable
fun PlayerView(
    viewModel: PlayerViewModel,
    onNavigateBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = LocalExtendedColors.current

    // Aesthetic pastel gradient matching design mockup
    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6B8CEF), // Dreamy Blue
            Color(0xFF98A6F5), // Lavender
            Color(0xFFC7A5EB), // Lilac
            Color(0xFFEEA2CF)  // Pastel Pink
        ),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(1000f, 1500f)
    )

    BaseScreen(
        isLoading = uiState.isLoading,
        onBackPressedCallback = if (onNavigateBack != null) {
            BackHandlerStyle.BackHandler { onNavigateBack() }
        } else {
            BackHandlerStyle.EmptyBackStack
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundGradient)
                    .padding(paddingValues)
                    .statusBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // ── Header: Minimize button, Song Title, Artist & Mode Selector ──
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            // Collapse/Down Chevron
                            IconButton(
                                onClick = { onNavigateBack?.invoke() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = localizedString(R.string.player_collapse),
                                    tint = Color(0xFF16151E),
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            // Title & Artist in Header
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 6.dp)
                            ) {
                                Text(
                                    text = uiState.songTitle,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF16151E)
                                    ),
                                    maxLines = 1
                                )
                                Text(
                                    text = uiState.artistName,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 14.sp,
                                        color = Color(0xFF4A4458)
                                    ),
                                    maxLines = 1
                                )
                            }
                        }

                        // Mode Selector (Yêu cầu 2, 3, 4 toggle)
                        LyricModeSelector(
                            currentMode = uiState.lyricMode,
                            onSelectMode = { mode ->
                                viewModel.handleEvent(PlayerEvent.SetLyricMode(mode))
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    // ── Center: Rotating Vinyl Turntable ──────────────────────
                    Box(
                        modifier = Modifier
                            .weight(1.05f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        VinylTurntable(
                            isPlaying = uiState.isPlaying,
                            turntableSize = 255.dp
                        )
                    }

                    // ── Lyrics Area: Auto-scrolling Karaoke Engine ────────────
                    Box(
                        modifier = Modifier
                            .weight(0.95f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        KaraokeLyricsView(
                            lyricSong = uiState.lyricSong,
                            currentPositionMs = uiState.currentPositionMs,
                            currentLineIndex = uiState.currentLineIndex,
                            nextLineIndex = uiState.nextLineIndex,
                            lyricMode = uiState.lyricMode,
                            onLineClick = { targetMs ->
                                viewModel.handleEvent(PlayerEvent.SeekTo(targetMs))
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ── Bottom Section: Seekbar + Playback Controls ───────────
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Progress / Seek Bar
                        PlayerProgressBar(
                            currentPositionMs = uiState.currentPositionMs,
                            durationMs = uiState.durationMs,
                            onSeekTo = { posMs ->
                                viewModel.handleEvent(PlayerEvent.SeekTo(posMs))
                            }
                        )

                        // Play / Pause / Shuffle / Repeat / Skip Controls
                        PlayerControls(
                            isPlaying = uiState.isPlaying,
                            isShuffle = uiState.isShuffle,
                            repeatMode = uiState.repeatMode,
                            onPlayPause = {
                                viewModel.handleEvent(PlayerEvent.PlayPause)
                            },
                            onNext = {
                                viewModel.handleEvent(PlayerEvent.NextTrack)
                            },
                            onPrevious = {
                                viewModel.handleEvent(PlayerEvent.PreviousTrack)
                            },
                            onToggleShuffle = {
                                viewModel.handleEvent(PlayerEvent.ToggleShuffle)
                            },
                            onToggleRepeat = {
                                viewModel.handleEvent(PlayerEvent.ToggleRepeat)
                            }
                        )
                    }
                }
            }
        }
    }
}
