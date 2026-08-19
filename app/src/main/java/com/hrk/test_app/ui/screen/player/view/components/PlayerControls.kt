package com.hrk.test_app.ui.screen.player.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hrk.test_app.R
import com.hrk.test_app.common.language.localizedString
import com.hrk.test_app.data.model.RepeatMode
import com.hrk.test_app.ui.theme.LocalExtendedColors

/**
 * Bottom playback controls row containing Shuffle, Prev, Play/Pause, Next, and Repeat.
 */
@Composable
fun PlayerControls(
    isPlaying: Boolean,
    isShuffle: Boolean,
    repeatMode: RepeatMode,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleRepeat: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalExtendedColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Shuffle Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            IconButton(
                onClick = onToggleShuffle,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Shuffle,
                    contentDescription = localizedString(R.string.player_shuffle),
                    tint = if (isShuffle) Color.White else Color.White.copy(alpha = 0.55f),
                    modifier = Modifier.size(24.dp)
                )
            }
            // Active dot
            if (isShuffle) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            } else {
                Box(modifier = Modifier.size(4.dp))
            }
        }

        // Previous Track Button
        IconButton(
            onClick = onPrevious,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.SkipPrevious,
                contentDescription = localizedString(R.string.player_prev),
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        // Big Circular Play / Pause Button
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFF16161A))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onPlayPause
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = localizedString(if (isPlaying) R.string.player_pause else R.string.player_play),
                tint = Color.White,
                modifier = Modifier.size(38.dp)
            )
        }

        // Next Track Button
        IconButton(
            onClick = onNext,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.SkipNext,
                contentDescription = localizedString(R.string.player_next),
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        // Repeat Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            IconButton(
                onClick = onToggleRepeat,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = when (repeatMode) {
                        RepeatMode.One -> Icons.Rounded.RepeatOne
                        else -> Icons.Rounded.Repeat
                    },
                    contentDescription = localizedString(R.string.player_repeat),
                    tint = if (repeatMode != RepeatMode.Off) Color.White else Color.White.copy(alpha = 0.55f),
                    modifier = Modifier.size(24.dp)
                )
            }
            if (repeatMode != RepeatMode.Off) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            } else {
                Box(modifier = Modifier.size(4.dp))
            }
        }
    }
}

