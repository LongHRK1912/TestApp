package com.hrk.test_app.ui.baseUI

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.hrk.test_app.R
import com.hrk.test_app.common.language.localizedString
import com.hrk.test_app.ui.theme.LocalExtendedColors
import com.hrk.test_app.utils.ComposeUtils.clickableSingle
import com.hrk.test_app.utils.ComposeUtils.keyboardAsState

@Composable
fun BaseScreen(
    defaultBackground: Color = LocalExtendedColors.current.background,
    focusRequester: FocusRequester? = null,
    scrollState: ScrollState? = null,
    isLoading: Boolean = false,
    onBackPressedCallback: BackHandlerStyle,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val imeState by keyboardAsState()

    var lastBackPressed by remember { mutableLongStateOf(0L) }
    val toastBack = localizedString(R.string.press_back_again_to_exit)

    val backHandler: () -> Unit = {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressed < 2000) {
            activity?.finish()
        } else {
            lastBackPressed = currentTime
            Toast.makeText(
                context,
                toastBack,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val hideKeyBoardAndClearFocus = {
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    LaunchedEffect(key1 = imeState) {
        if (imeState) {
            scrollState?.let {
                it.animateScrollTo(it.value, tween(300))
            }
        }
        if (!imeState) {
            hideKeyBoardAndClearFocus.invoke()
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        focusRequester?.let {
            it.requestFocus()
            keyboardController?.show()
        }
    })

    BackHandler {
        if (imeState) {
            hideKeyBoardAndClearFocus.invoke()
        } else {
            when (onBackPressedCallback) {
                is BackHandlerStyle.BackHandler -> {
                    onBackPressedCallback.callBack.invoke()
                }

                BackHandlerStyle.EmptyBackStack -> backHandler()
            }
        }
    }

    Box(
        modifier = Modifier
            .clickableSingle(
                indicated = false
            ) {
                hideKeyBoardAndClearFocus.invoke()
            }
            .fillMaxSize()
            .background(defaultBackground)
    ) {
        content.invoke()

        if (isLoading) {
            HRKLoading()
        }
    }
}

sealed class BackHandlerStyle {
    data object EmptyBackStack : BackHandlerStyle()
    data class BackHandler(val callBack: () -> Unit) : BackHandlerStyle()
}
