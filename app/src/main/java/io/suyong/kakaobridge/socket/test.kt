package io.suyong.kakaobridge.socket

import io.socket.client.IO

class test(url: String) {
    private val socket = IO.socket(url)
}