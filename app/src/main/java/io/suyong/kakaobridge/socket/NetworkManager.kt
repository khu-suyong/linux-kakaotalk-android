package io.suyong.kakaobridge.socket

import io.socket.client.IO
import io.socket.client.Socket
import io.suyong.kakaobridge.logger.LogType
import io.suyong.kakaobridge.logger.Logger
import java.lang.Exception

class NetworkManager(var url: String = "") {
    private var socket: Socket? = null

    fun connect(): Boolean {
        try {
            socket = IO.socket(url)
            Logger.add(LogType.INFO, "Connected Server", "url: ${url}")

            return true
         } catch (error: Exception) {
            Logger.add(LogType.ERROR, "Failed to connect server", error.toString())

            return false
        }
    }

}