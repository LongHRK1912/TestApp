package com.hrk.test_app.common.update

import android.app.Activity
import android.content.IntentSender
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.tasks.await

class AppUpdateHelper(
    private val appUpdateManager: AppUpdateManager
) {
    companion object {
        private const val UPDATE_REQUEST_CODE = 1001
    }

    suspend fun checkForImmediateUpdate(activity: Activity) {
        val appUpdateInfo = appUpdateManager.appUpdateInfo.await()

        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
            appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
        ) {
            try {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activity,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                    UPDATE_REQUEST_CODE
                )
            } catch (_: IntentSender.SendIntentException) {
            }
        }
    }

    fun completeUpdateIfDownloaded() {
        appUpdateManager.registerListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                appUpdateManager.completeUpdate()
            }
        }
    }
}
