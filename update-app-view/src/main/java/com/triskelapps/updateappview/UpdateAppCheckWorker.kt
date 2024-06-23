package com.triskelapps.updateappview

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@SuppressLint("RestrictedApi")
class UpdateAppCheckWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val updateAppManager = UpdateAppManager(applicationContext)
    private lateinit var prefs: SharedPreferences

    override suspend fun doWork(): Result {


        prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val lastVersionNotified = prefs.getInt(PREF_LAST_VERSION_NOTIFIED, 0)

        sendRemoteLog("Start worker. lastVersionNotified: $lastVersionNotified, version code: ${UpdateAppManager.VERSION_CODE}")

        return if (lastVersionNotified < UpdateAppManager.VERSION_CODE) {
            checkAppUpdateAvailable()
        } else {
            Result.success()
        }
    }


    private suspend fun checkAppUpdateAvailable(): Result = suspendCoroutine { continuation ->
        updateAppManager.setUpdateAvailableListener {
            sendRemoteLog("onUpdateAvailable. sending notification")
            prepareAndShowNotification() }
        updateAppManager.setFinishListener {
            sendRemoteLog("onFinish")
            continuation.resume(Result.success()) }
        updateAppManager.checkUpdateAvailable()
    }

    private fun prepareAndShowNotification() {

        NotificationUtils(applicationContext).showNotification(UpdateAppManager.PACKAGE_NAME)
        prefs.edit().putInt(PREF_LAST_VERSION_NOTIFIED, UpdateAppManager.VERSION_CODE).apply()
    }


    companion object {
        private val TAG: String = UpdateAppCheckWorker::class.java.simpleName
        private const val PREF_LAST_VERSION_NOTIFIED = "pref_last_version_notified"
        private const val CHANNEL_NOTIF_UPDATE_APP = "channel_update_app"
        private const val ID_UPDATE_APP_NOTIF = 1000
    }
}
