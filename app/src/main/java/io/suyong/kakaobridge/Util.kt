package io.suyong.kakaobridge

object Util {
    object Base62 {
        val RADIX = 62
        val CODEC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

        fun encoding(param: Long): String {
            var param = param
            val sb = StringBuffer()

            while (param > 0) {
                sb.append(CODEC[(param % RADIX).toInt()])
                param /= RADIX.toLong()
            }

            return sb.toString()
        }

        fun decoding(param: String): Long {
            var sum: Long = 0
            var power: Long = 1

            for (element in param) {
                sum += CODEC.indexOf(element) * power
                power *= RADIX.toLong()
            }

            return sum
        }
    } // base62 shortening algorithm https://metalkin.tistory.com/53, https://code0xff.tistory.com/106
}