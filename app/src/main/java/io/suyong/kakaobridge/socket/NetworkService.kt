package io.suyong.kakaobridge.socket

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.suyong.kakaobridge.logger.LogType
import io.suyong.kakaobridge.logger.Logger

class NetworkService : Service() {
    companion object {
        var isRunning = false
    }

    private val networkManager = NetworkManager()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Logger.add(LogType.INFO, "Network Service Start", "data: ${flags}")
        isRunning = true

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        Logger.add(LogType.INFO, "Network Service Stop", "data: ")
        isRunning = false

        super.onDestroy()
    }
}

object NetworkServiceObject {
    var isRunning = false
}