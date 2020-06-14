package io.suyong.kakaobridge.network

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import io.suyong.kakaobridge.MainActivity
import io.suyong.kakaobridge.kakao.KakaoManager
import io.suyong.kakaobridge.logger.LogType
import io.suyong.kakaobridge.logger.Logger


class NetworkService : Service() {
    companion object {
        var isRunning = false

        val CONNECT = "connect"
        val EMIT = "emit"

        val CHANNEL_ID = "test-channel-id"
        val CHANNEL_NAME = "Test channel name"
    }

    private fun init() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_NONE
            )
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setContentTitle("KakaoTalk Bridge System is running...")
            .setOngoing(true)

        startForeground(1, builder.build())

        NetworkManager.on("send") {
            val text = it.get("text") as String
            val room = it.get("room") as String

            KakaoManager.send(room, text)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int) = when(intent.action) {
        CONNECT -> {
            val url = intent.extras?.get("url").toString()

            NetworkManager.connect(url)
            isRunning = true

            init()

            START_STICKY
        }
        EMIT -> {
            NetworkManager.emit("message")
            Logger.add(LogType.DEBUG, "send message")

            START_STICKY
        }
        else -> {
            Logger.add(LogType.WARN, "Started but not match action", "action: ${intent.action}")

            START_STICKY
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        isRunning = false

        NetworkManager.disconnect()

        super.onDestroy()
    }
}