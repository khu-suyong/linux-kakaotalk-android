package io.suyong.kakaobridge.kakao

import android.app.Notification

data class Message(val room: String, val sender: String, val message: String, val session: Notification.Action) {
    override fun toString(): String {
        return "Message { room: ${room}, sender: ${sender}, message: ${message} }"
    }
}