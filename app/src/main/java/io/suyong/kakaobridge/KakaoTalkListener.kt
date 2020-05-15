package io.suyong.kakaobridge

import android.app.Notification
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.widget.Toast
import io.suyong.kakaobridge.kakao.Message

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
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }
}