package io.suyong.kakaobridge.logger

import android.app.Activity
import android.content.Context

object Logger {
    val list = ArrayList<Log>()

    var activity: Activity? = null

    private var addListener: (log: Log) -> Unit = {}
    private var removeListener: (position: Int) -> Unit = {}


    fun add (type: LogType, title: String, vararg content: String) {
        val log = Log(type, title, content.joinToString("\n"))

        list.add(log)
        activity?.runOnUiThread {
            addListener.invoke(log)
        }
    }

    fun remove (position: Int) {
        list.removeAt(position)

        removeListener.invoke(position)
    }

    fun setOnLogAddListener(func: (log: Log) -> Unit) {
        this.addListener = func
    }

    fun setOnLogRemoveListener(func: (position: Int) -> Unit) {
        this.removeListener = func
    }
}