package io.suyong.kakaobridge.logger

object Logger {
    val list = ArrayList<Log>()

    private var addListener: (log: Log) -> Unit = {}
    private var removeListener: (position: Int) -> Unit = {}

    fun add (type: LogType, title: String, content: String) {
        val log = Log(type, title, content)

        list.add(log)
        addListener.invoke(log)
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