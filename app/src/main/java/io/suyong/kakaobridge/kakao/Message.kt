package io.suyong.kakaobridge.kakao

import android.app.Notification

import io.suyong.kakaobridge.kakao.Room

data class Message(val room: Room, val sender: User, val message: String, val session: Notification.Action) {
    constructor(room: String, sender: String, message: String, session: Notification.Action) : this(Room.find(room), User.find(sender), message, session)

    override fun toString(): String {
        return "Message { room: ${room.name}, sender: ${sender.name}, message: ${message} }"
    }
}