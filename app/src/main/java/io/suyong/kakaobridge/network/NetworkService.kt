package io.suyong.kakaobridge.network

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.suyong.kakaobridge.logger.LogType
import io.suyong.kakaobridge.logger.Logger

class NetworkService : Service() {
    companion object {
        var isRunning = false

        val CONNECT = "connect"
        val EMIT = "emit"
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int) = when(intent.action) {
        CONNECT -> {
            val url = intent.extras?.get("url").toString()

            NetworkManager.connect(url)
            isRunning = true

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