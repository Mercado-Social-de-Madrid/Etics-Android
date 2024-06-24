package com.triskelapps.updateappview

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.Operation
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class UpdateAppManager(private val context: Context) {

    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(context)
    private var appUpdateInfo: AppUpdateInfo? = null
    private var onUpdateAvailable: () -> Unit = {}
    private var onUpdateFinish: () -> Unit = {}


    companion object {

        private val TAG: String = UpdateAppManager::class.java.simpleName

        const val TEMPLATE_URL_GOOGLE_PLAY_APP_HTTP: String =
            "https://play.google.com/store/apps/details?id=%s"
        const val TEMPLATE_URL_GOOGLE_PLAY_APP_DIRECT: String = "market://details?id=%s"

        lateinit var PACKAGE_NAME: String
        var VERSION_CODE: Int = 0

        var notificationIcon = 0
        var updateBarStyle: UpdateBarStyle? = null

        @JvmStatic
        @JvmOverloads
        fun init(
            context: Context,
            versionCode: Int,
            notificationIcon: Int,
            updateBarStyle: UpdateBarStyle? = null,
            checkWorkerConfiguration: CheckWorkerConfiguration? = CheckWorkerConfiguration()
        ) {
            PACKAGE_NAME = context.packageName
            VERSION_CODE = versionCode
            if (checkWorkerConfiguration != null) {
                scheduleAppUpdateCheckWork(context, checkWorkerConfiguration)
            }
            UpdateAppManager.notificationIcon = notificationIcon
            UpdateAppManager.updateBarStyle = updateBarStyle

            sendRemoteLog("UpdateAppManager init")
        }

        private fun scheduleAppUpdateCheckWork(
            context: Context?,
            checkWorkerConfiguration: CheckWorkerConfiguration
        ) {
            val constraints: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val updateAppCheckWork: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
                UpdateAppCheckWorker::class.java,
                checkWorkerConfiguration.repeatInterval, checkWorkerConfiguration.repeatIntervalTimeUnit,
                checkWorkerConfiguration.flexInterval, checkWorkerConfiguration.flexIntervalTimeUnit,
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context!!).enqueueUniquePeriodicWork(
                "appUpdateCheckWork",
                ExistingPeriodicWorkPolicy.UPDATE, updateAppCheckWork
            ).state.observeForever {state: Operation.State ->

                sendRemoteLog("Periodic work status: $state")
            }
        }
    }

    fun setUpdateAvailableListener(updateAvailableListener: () -> Unit = {}) {
        this.onUpdateAvailable = updateAvailableListener
    }

    fun setFinishListener(updateFinishListener: () -> Unit = {}) {
        this.onUpdateFinish = updateFinishListener
    }

    fun checkUpdateAvailable() {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo

        appUpdateInfoTask
            .addOnCompleteListener { task: Task<AppUpdateInfo?> ->
                if (task.isSuccessful) {
                    appUpdateInfo = task.result
                    if (appUpdateInfo!!.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                        onUpdateAvailable()
                    }
                } else {
                    Log.e(TAG, "checkUpdateAvailable: ", task.exception)
                }

                onUpdateFinish()

            }
    }

    fun onUpdateVersionClick() {
        if (appUpdateInfo != null) {
            if (appUpdateInfo!!.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                val options: AppUpdateOptions = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                    .setAllowAssetPackDeletion(true).build()

                if (context is Activity) {
                    appUpdateManager.startUpdateFlow(appUpdateInfo!!, context as Activity, options)
                }
            } else {
                openGooglePlay()
            }
        }

    }

    private fun openGooglePlay() {
        val httpUrl = String.format(TEMPLATE_URL_GOOGLE_PLAY_APP_HTTP, PACKAGE_NAME)
        val directUrl = String.format(TEMPLATE_URL_GOOGLE_PLAY_APP_DIRECT, PACKAGE_NAME)

        val directPlayIntent = Intent(Intent.ACTION_VIEW, Uri.parse(directUrl))
        try {
            context.startActivity(directPlayIntent)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl)))
        }
    }

    fun onResume() {
        if (appUpdateInfo != null && appUpdateInfo!!.updateAvailability()
            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
        ) {
            val options: AppUpdateOptions = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                .setAllowAssetPackDeletion(true).build()

            if (context is Activity) {
                appUpdateManager.startUpdateFlow(appUpdateInfo!!, context, options)
            }
        }
    }


}
