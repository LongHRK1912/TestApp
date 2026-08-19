package com.hrk.test_app.ui.screen.player

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hrk.test_app.ui.screen.player.view.PlayerView

@Composable
fun PlayerScreen(
    onNavigateBack: (() -> Unit)? = null
) {
    val viewModel = hiltViewModel<PlayerViewModel>()

    PlayerView(
        viewModel = viewModel,
        onNavigateBack = onNavigateBack
    )
}
