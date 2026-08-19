package com.hrk.test_app.utils

import android.os.SystemClock
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import com.hrk.test_app.BuildConfig

object ComposeUtils {
    fun Modifier.clickableSingle(
        enabled: Boolean = true,
        onClickLabel: String? = null,
        role: Role? = null,
        indicated: Boolean = BuildConfig.DEBUG,
        onClick: () -> Unit
    ) = composed(
        inspectorInfo = debugInspectorInfo {
            name = "clickable"
            properties["enabled"] = enabled
            properties["onClickLabel"] = onClickLabel
            properties["role"] = role
            properties["onClick"] = onClick
        }
    ) {
        val lastEventTimeMs = remember { mutableLongStateOf(0L) }
        val delayTime = 300L

        this.clickable(
            enabled = enabled,
            onClickLabel = onClickLabel,
            onClick = {
                val now = System.currentTimeMillis()
                if (now - lastEventTimeMs.longValue >= delayTime) {
                    onClick()
                    lastEventTimeMs.longValue = now
                }
            },
            role = role,
            indication = if (indicated) LocalIndication.current else null,
            interactionSource = remember { MutableInteractionSource() }
        )
    }

    @Composable
    fun keyboardAsState(): State<Boolean> {
        val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
        return rememberUpdatedState(isImeVisible)
    }
}
