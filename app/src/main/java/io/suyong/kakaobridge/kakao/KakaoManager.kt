package io.suyong.kakaobridge.kakao

import android.R.id.message
import android.app.Notification
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.suyong.kakaobridge.logger.LogType
import io.suyong.kakaobridge.logger.Logger
import io.suyong.kakaobridge.network.NetworkManager


object KakaoManager {
    val messages = mutableListOf<Message>()
    val rooms = mutableListOf<Room>()
    var context: Context? = null

    fun init(context: Context) {
        this.context = context
    }

    fun add(element: Message) {
        this.messages.add(element)
    }

    fun add(room: String, session: Notification.Action) {
        val exist = this.rooms.find {
            it.name == room
        }

        if (exist == null)
            this.rooms.add(Room(room, session))
    }

    fun send(room: String, text: String) {
        this.rooms.forEach {
            if (it.name == room) {
                val intent = Intent()
                val bundle = Bundle()

                it.session.remoteInputs.forEach { remoteInput -> bundle.putCharSequence(remoteInput.resultKey, text) }
                RemoteInput.addResultsToIntent(it.session.remoteInputs, intent, bundle)

                it.session.actionIntent.send(context, 0, intent)
            }
        }

        Logger.add(LogType.INFO, "send message", "room: ${room}, text: $text")
    }
}