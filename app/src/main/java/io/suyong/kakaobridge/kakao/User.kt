package io.suyong.kakaobridge.kakao

import android.app.Notification

data class User(val name: String, val session: Notification.Action) {
    companion object {
        var list = mutableListOf<User>()

        fun find(name: String): User? {
            User.list.forEach {
                if (it.name == name) {
                    return it
                }
            }

            return null
        }

        fun add(name: String, session: Notification.Action) {
            if (find(name) == null) {
                list.add(User(name, session))
            }
        }

        fun add(user: User) {
            if (find(user.name) == null) {
                list.add(user)
            }
        }
    }
}