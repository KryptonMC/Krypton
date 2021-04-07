package org.kryptonmc.krypton.extension

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.kryptonmc.krypton.api.world.Gamemode
import java.net.InetAddress
import kotlin.math.log2
import kotlin.math.max

fun String.toInetAddress(): InetAddress = InetAddress.getByName(this)

inline fun <reified T> logger(): Logger = LogManager.getLogger(T::class.java)

fun logger(name: String): Logger = LogManager.getLogger(name)

fun Int.calculateBits() = max(log2(takeHighestOneBit().toDouble()).toInt() + 1, 4)

fun Int.toArea() = (this * 2 + 1).square()

fun Int.square() = this * this

fun String.toProtocol(): ByteArray {
    val bytes = encodeToByteArray()
    return byteArrayOf(bytes.size.toByte(), *bytes)
}

val Gamemode.canBuild: Boolean
    get() = this == Gamemode.SURVIVAL || this == Gamemode.CREATIVE