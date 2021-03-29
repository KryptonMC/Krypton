package org.kryptonmc.krypton.extension

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.net.InetAddress
import kotlin.math.log
import kotlin.math.max

fun String.toInetAddress(): InetAddress = InetAddress.getByName(this)

inline fun <reified T> logger(): Logger = LogManager.getLogger(T::class.java)

fun logger(name: String): Logger = LogManager.getLogger(name)

fun Int.calculateBits() = max(log(takeHighestOneBit().toDouble(), 2.0).toInt() + 1, 4)

fun Int.toArea() = (this * 2 + 1).square()

fun Int.square() = this * this

fun String.toProtocol(): ByteArray {
    val bytes = encodeToByteArray()
    return byteArrayOf(bytes.size.toByte(), *bytes)
}