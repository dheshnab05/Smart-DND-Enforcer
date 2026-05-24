package com.example.sentrycallnew

import android.app.Notification
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import java.util.*
import androidx.core.net.toUri

class NotificationService : NotificationListenerService() {

    private lateinit var dbHelper: DBHelper
    private val callCountMap = mutableMapOf<String, Int>()

    private var mediaPlayer: MediaPlayer? = null
    private var ringtone: android.media.Ringtone? = null

    override fun onCreate() {
        super.onCreate()
        dbHelper = DBHelper(this)
        Log.d("SERVICE", "Notification Service Started")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        if (sbn.packageName != "com.whatsapp") return

        val extras = sbn.notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()

        if (text == null) return

        val lowerText = text.lowercase()
        // If NOT incoming call → STOP immediately
        if (!(lowerText.contains("incoming voice call") ||
                    lowerText.contains("incoming video call"))
        ) {
            stopRingtone()
            return
        }

        // Only true incoming call reaches here

        val callerName = (title ?: text ?: "Unknown").lowercase().trim()

        Log.d("ML_ENGINE", "Incoming WhatsApp call from: $callerName")

        val isKnown = dbHelper.isContactInDb(callerName)

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val isNight = currentHour >= 22 || currentHour < 6

        val count = callCountMap.getOrDefault(callerName, 0) + 1
        callCountMap[callerName] = count

        Log.d("ML_ENGINE", "isKnown=$isKnown, count=$count, isNight=$isNight")

        val shouldRing = if (isKnown) {
            if (isNight) count >= 3 else true
        } else {
            false
        }

        if (shouldRing) {
            playImportantRingtone()
        } else {
            stopRingtone()
            Log.d("ACTION", "Silent")
        }
    }

    private fun playImportantRingtone() {
        try {
            if (ringtone?.isPlaying == true || mediaPlayer?.isPlaying == true) return

            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

            audioManager.setStreamVolume(
                AudioManager.STREAM_ALARM,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),
                0
            )

            val uri = "android.resource://$packageName/${R.raw.emergency_ringtone}".toUri()

            ringtone = android.media.RingtoneManager.getRingtone(this, uri)
            ringtone?.setStreamType(AudioManager.STREAM_ALARM)
            ringtone?.play()

            Log.d("AUDIO", "🔊 Ringtone started")

        } catch (e: Exception) {
            Log.e("CRASH", "Play error: ${e.message}")
        }
    }
    private fun stopRingtone() {
        try {
            ringtone?.stop()
            ringtone = null

            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null

            Log.d("AUDIO", "Stopped instantly")

        } catch (e: Exception) {
            Log.e("CRASH", "Stop error: ${e.message}")
        }
    }
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        if (sbn.packageName != "com.whatsapp") return
        stopRingtone()
        Log.d("AUDIO", "Removed → stopped immediately")
    }
}