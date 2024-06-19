package com.triskelapps.updateappview

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
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
        return if (lastVersionNotified < UpdateAppManager.VERSION_CODE) {
            checkAppUpdateAvailable()
        } else {
            Result.success()
        }
    }


    private suspend fun checkAppUpdateAvailable(): Result = suspendCoroutine { continuation ->
        updateAppManager.setUpdateAvailableListener { prepareAndShowNotification() }
        updateAppManager.setFinishListener { continuation.resume(Result.success()) }
        updateAppManager.checkUpdateAvailable()
    }

    private fun prepareAndShowNotification() {
        initializeOreoChannelsNotification()
        showNotification()
        prefs.edit().putInt(PREF_LAST_VERSION_NOTIFIED, UpdateAppManager.VERSION_CODE).apply()
    }


    fun getBaseBuilder(context: Context): NotificationCompat.Builder {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notifBuilder = NotificationCompat.Builder(context, CHANNEL_NOTIF_UPDATE_APP).apply {
            setSound(defaultSoundUri)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
        }

        return notifBuilder
    }


    fun showNotification() {
        val notifBuilder = getBaseBuilder(applicationContext)
        notifBuilder.setContentTitle(applicationContext.getString(R.string.update_available))
        notifBuilder.setContentText(applicationContext.getString(R.string.press_to_update))

        val notification = notifBuilder.build()
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(ID_UPDATE_APP_NOTIF, notification)
    }

    private fun initializeOreoChannelsNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(
                CHANNEL_NOTIF_UPDATE_APP,
                R.string.channel_notif_update_app_name,
                R.string.channel_notif_update_app_description
            )
        }
    }

    @SuppressLint("NewApi")
    private fun createChannel(channelId: String, nameStringId: Int, descriptionStringId: Int) {
        val notificationManager = applicationContext.getSystemService(
            NotificationManager::class.java
        )

        if (notificationManager.getNotificationChannel(channelId) == null) {
            val name: CharSequence = applicationContext.getString(nameStringId)
            val description = applicationContext.getString(descriptionStringId)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private val TAG: String = UpdateAppCheckWorker::class.java.simpleName
        private const val PREF_LAST_VERSION_NOTIFIED = "pref_last_version_notified"
        private const val CHANNEL_NOTIF_UPDATE_APP = "channel_update_app"
        private const val ID_UPDATE_APP_NOTIF = 1000
    }
}
