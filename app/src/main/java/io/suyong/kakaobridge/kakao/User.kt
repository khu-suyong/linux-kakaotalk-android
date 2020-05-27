package io.suyong.kakaobridge.kakao

import android.app.Notification

data class User(val name: String, val session: Notification.Action) {
    companion object {
        var list: ArrayList<User> = ArrayList()

        fun find (name: String): User {
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