package io.suyong.kakaobridge.kakao

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import io.suyong.kakaobridge.Config
import io.suyong.kakaobridge.logger.LogType
import io.suyong.kakaobridge.logger.Logger

class KakaoTalkListener: NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        if (sbn.packageName == Config.KAKAOTALK) {
            val extender = Notification.WearableExtender(sbn.notification)

            for (act in extender.actions) {
                if (act.remoteInputs.isNotEmpty()) {
                    val extras = sbn.notification.extras

                    val text = extras["android.text"] as String
                    val sender = extras["android.title"] as String
                    val room = (extras["android.summaryText"] ?: sender) as String
                    val session = act

                    val message = Message(room, sender, text, session)
                    Logger.add(LogType.INFO, "Message Recieved", message.toString())
                }
            }
        }
    }
}