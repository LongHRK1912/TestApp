package com.hrk.test_app.ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class AppDestination : NavKey {
    @Serializable
    data object Player : AppDestination()
}


