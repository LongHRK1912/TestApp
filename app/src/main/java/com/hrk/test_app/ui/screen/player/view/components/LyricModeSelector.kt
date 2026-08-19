package com.hrk.test_app.ui.screen.player.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hrk.test_app.common.language.localizedString
import com.hrk.test_app.data.model.LyricDisplayMode

/**
 * Premium Glassmorphism Segmented Switch for Lyric Modes (UI/UX Pro Max Edition)
 */
@Composable
fun LyricModeSelector(
    currentMode: LyricDisplayMode,
    onSelectMode: (LyricDisplayMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.16f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.28f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(3.5.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LyricDisplayMode.entries.forEach { mode ->
            val isSelected = mode == currentMode

            val bgColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else Color.Transparent,
                animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
                label = "PillBgColor_${mode.name}"
            )

            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFF16151E) else Color.White.copy(alpha = 0.82f),
                animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
                label = "PillTextColor_${mode.name}"
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .then(
                        if (isSelected) {
                            Modifier.shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                        } else Modifier
                    )
                    .background(bgColor)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onSelectMode(mode) }
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = localizedString(mode.shortTitleRes),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = textColor
                    )
                )
            }
        }
    }
}
