package com.hrk.test_app.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.hrk.test_app.R
import com.hrk.test_app.common.language.localizedString
import com.hrk.test_app.ui.baseUI.ButtonModel
import com.hrk.test_app.ui.baseUI.ButtonType
import com.hrk.test_app.ui.baseUI.HRKButton
import com.hrk.test_app.ui.baseUI.TypeSize
import com.hrk.test_app.ui.screen.player.PlayerScreen
import com.hrk.test_app.ui.theme.LocalExtendedColors
import com.hrk.test_app.utils.NetworkMonitor

@Composable
fun AppNavigation(
    networkMonitor: NetworkMonitor,
    appState: HRKAppState = rememberHRKState(
        networkMonitor = networkMonitor,
    ),
    startDestination: AppDestination = AppDestination.Player,
) {
    val colors = LocalExtendedColors.current
    val context = LocalContext.current
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(startDestination)

    val onBackStack: () -> Unit = {
        if (backStack.count() > 1) {
            backStack.removeLastOrNull()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.ime),
        snackbarHost = {
            if (isOffline) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.card)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = localizedString(R.string.not_connected_content),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = colors.onCard
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    HRKButton(
                        model = ButtonModel(
                            text = localizedString(R.string.open_settings),
                            onClick = {
                                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            },
                            type = ButtonType.Outlined,
                            sizeType = TypeSize.WrapContent,
                            contentColor = colors.onCard,
                            outlinedColor = colors.onCard
                        ),
                        modifier = Modifier.wrapContentSize()
                    )
                }
            }
        },
    ) { _ ->
        NavDisplay(
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            backStack = backStack,
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background),
            onBack = onBackStack,
            entryProvider = entryProvider {
                entry<AppDestination.Player> {
                    PlayerScreen(
                        onNavigateBack = onBackStack
                    )
                }
            },
        )
    }
}

