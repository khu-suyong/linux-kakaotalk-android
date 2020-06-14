package io.suyong.kakaobridge.network

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.suyong.kakaobridge.MainActivity
import io.suyong.kakaobridge.logger.LogType
import io.suyong.kakaobridge.logger.Logger
import org.json.JSONObject
import java.lang.Exception

object NetworkManager {
    var url: String = ""

    private var socket: Socket? = null
    private var map = HashMap<String, MutableList<(JSONObject) -> Unit>>()

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
        emit(
            "unregister-client",
            mapOf(
                "type" to "bridge"
            )
        )
        socket?.disconnect()
        Logger.add(LogType.INFO, "Network manager stopped")
    }

    fun emit(event: String, obj: Map<String, String>? = null) {
        if (obj != null) {
            val json = JSONObject()

            json.put("uuid", MainActivity.uuid)
            for (element in obj) {
                json.put(element.key, element.value)
            }

            socket?.emit(event, json)
        } else {
            socket?.emit(event)
        }
    }

    fun on(event: String, func: (data: JSONObject) -> Unit) {
        map.getOrPut(event) { mutableListOf() }.add(func)
    }

    private fun init() {
        socket?.let { io ->
            io.on(Socket.EVENT_CONNECT) { data ->
                val json = JSONObject()

                map[Socket.EVENT_CONNECT]?.forEach {
                    it.invoke(json)
                }

                emit(
                    "register-client",
                    mapOf(
                        "type" to "bridge"
                    )
                )

                Logger.add(LogType.DEBUG, "Server connected", data.toString())
            }

            io.on(Socket.EVENT_MESSAGE) { data ->
                val json = JSONObject(data[0].toString())

                map[Socket.EVENT_MESSAGE]?.forEach {
                    it.invoke(json)
                }

                Log.d("socket.io", "message")
                Logger.add(LogType.DEBUG, "Message", data.toString())
            }

            io.on(Socket.EVENT_DISCONNECT) { data ->
                val json = JSONObject()

                map[Socket.EVENT_DISCONNECT]?.forEach {
                    it.invoke(json)
                }

                Logger.add(LogType.DEBUG, "Server disconnected", data.toString())
            }

            io.connect()
        }
    }
}