package com.hrk.test_app.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrk.test_app.ui.theme.TestAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        var splashDurationExpired = false

        installSplashScreen().apply {
//            setKeepOnScreenCondition {
//                if (!splashDurationExpired) {
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        splashDurationExpired = true
//                    }, DELAY_SPLASH)
//                }
//                mainViewModel.startDestination.value == null || !splashDurationExpired
//            }
//
//            setOnExitAnimationListener { screen ->
//                val zoomX = ObjectAnimator.ofFloat(
//                    screen.iconView,
//                    View.SCALE_X,
//                    1f,
//                    1.5f
//                )
//                val zoomY = ObjectAnimator.ofFloat(
//                    screen.iconView,
//                    View.SCALE_Y,
//                    1f,
//                    1.5f
//                )
//                val fadeOut = ObjectAnimator.ofFloat(
//                    screen.iconView,
//                    View.ALPHA,
//                    1f,
//                    0f
//                )
//
//                AnimatorSet().apply {
//                    interpolator = AnticipateInterpolator()
//                    duration = 500L
//                    playTogether(zoomX, zoomY, fadeOut)
//                    doOnEnd { screen.remove() }
//                    start()
//                }
//            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val startDestination by mainViewModel.startDestination.collectAsStateWithLifecycle()
            val currentTheme by mainViewModel.currentTheme.collectAsStateWithLifecycle()
            val localeState by mainViewModel.localeState.collectAsStateWithLifecycle()

            TestAppTheme(
                themeMode = currentTheme,
                localeState = localeState,
            ) {
                startDestination?.let { destination ->
                    AppNavigation(
                        networkMonitor = mainViewModel.networkMonitor,
                        startDestination = destination,
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.checkForUpdates(this)
    }
}