package io.suyong.kakaobridge.kakao

import android.app.Notification

data class Room(val name: String, val session: Notification.Action) {
    companion object {
        var list: ArrayList<Room> = ArrayList()

        fun find (name: String): Room {
            val result = list.filter {
                if (it.name.equals(name)) {
                    true
                }

                false
            }

            return result[0]
        }
    }
}