package io.suyong.kakaobridge.logger

import java.util.Date

data class Log(val type: LogType, val title: String, val content: String, val date: Date = Date())

enum class LogType {
    ERROR,
    WARN,
    INFO
}