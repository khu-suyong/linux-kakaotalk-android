package io.suyong.kakaobridge.kakao

import android.app.Notification

data class Room(val name: String, val session: Notification.Action) {
    companion object {
        var list = mutableListOf<Room>()

        fun find (name: String): Room? {
            list.forEach {
                if (it.name == name) {
                    return it
                }
            }

            return null
        }

        fun add (name: String, session: Notification.Action) {
            if (find(name) == null) {
                list.add(Room(name, session))
            }
        }

        fun add (room: Room) {
            if (find(room.name) == null) {
                list.add(room)
            }
        }
    }
}