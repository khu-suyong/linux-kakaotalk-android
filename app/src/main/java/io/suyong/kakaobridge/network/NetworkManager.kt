package io.suyong.kakaobridge.network

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.suyong.kakaobridge.logger.LogType
import io.suyong.kakaobridge.logger.Logger
import org.json.JSONObject
import java.lang.Exception

object NetworkManager {
    var url: String = ""

    private var socket: Socket? = null
    private var map = HashMap<String, MutableList<(Any) -> Any>>()

    fun connect(url: String? = null) {
        this.url = url ?: this.url

        try {
            socket = IO.socket(this.url)
            init()

            Logger.add(LogType.INFO, "Network manager started", "url: ${this.url}")
        } catch (error: Exception) {
            Logger.add(LogType.ERROR, "Failed to start network manager", error.toString())
        }
    }

    fun disconnect() {
        socket?.disconnect()
        Logger.add(LogType.INFO, "Network manager stopped")
    }

    fun emit(event: String, obj: Map<String, String>? = null) {
        if (obj != null) {
            val json = JSONObject()

            for (element in obj) {
                json.put(element.key, element.value)
            }

            socket?.emit(event, json)
        } else {
            socket?.emit(event)
        }
    }

    fun on(event: String, func: (data: Any) -> Any) {
        map.getOrPut(event) { mutableListOf() }.add(func)
    }

    private fun init() {
        socket?.let { io ->
            io.on(Socket.EVENT_CONNECT) { data ->
                map[Socket.EVENT_CONNECT]?.forEach {
                    it.invoke(data)
                }

                Logger.add(LogType.DEBUG, "Server connected", data.toString())
            }

            io.on(Socket.EVENT_MESSAGE) { data ->
                map[Socket.EVENT_MESSAGE]?.forEach {
                    it.invoke(data)
                }

                Log.d("socket.io", "message")
                Logger.add(LogType.DEBUG, "Message", data.toString())
            }

            io.on(Socket.EVENT_DISCONNECT) { data ->
                map[Socket.EVENT_DISCONNECT]?.forEach {
                    it.invoke(data)
                }

                Logger.add(LogType.DEBUG, "Server disconnected", data.toString())
            }

            io.connect()
        }

        emit(
            "register-client",
            mapOf(
                "type" to "bridge"
            )
        )
    }
}