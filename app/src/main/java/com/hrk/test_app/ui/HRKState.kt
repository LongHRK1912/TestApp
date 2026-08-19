package com.hrk.test_app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.hrk.test_app.utils.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberHRKState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): HRKAppState {
    return remember(
        coroutineScope,
        networkMonitor,
    ) {
        HRKAppState(
            coroutineScope,
            networkMonitor,
        )
    }
}

@Stable
class HRKAppState(
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )
}
