package org.kryptonmc.krypton.util

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.kryptonmc.krypton.api.world.Gamemode
import java.net.InetAddress
import kotlin.math.log2
import kotlin.math.max

/**
 * Convert a [String] to an [InetAddress]
 */
fun String.toInetAddress(): InetAddress = InetAddress.getByName(this)

/**
 * Retrieve a [Logger] instance from the specified class [T]
 *
 * @param T the class to retrieve a logger for
 * @return a [Logger] with the name of the specified class [T]
 */
inline fun <reified T> logger(): Logger = LogManager.getLogger(T::class.java)

/**
 * Retrieve a [Logger] instance with the specified [name]
 *
 * @param name the name of the logger
 * @return a [Logger] with the specified [name]
 */
fun logger(name: String): Logger = LogManager.getLogger(name)

/**
 * Calculate the amount of non-empty significant bits in this [Int]
 * For example, if the bits are 00110000, this returns 6.
 */
fun Int.calculateBits() = max(log2(takeHighestOneBit().toDouble()).toInt() + 1, 4)

/**
 * Gets the area from this view distance [Int]
 */
fun Int.toArea() = (this * 2 + 1).square()

/**
 * Square an integer. This avoids us having to use [Math.pow], which is inefficient for this.
 */
fun Int.square() = this * this

/**
 * Convert an ordinary string to a protocol string, with its length prefixed.
 */
fun String.toProtocol(): ByteArray {
    val bytes = encodeToByteArray()
    return byteArrayOf(bytes.size.toByte(), *bytes)
}

/**
 * If this gamemode has privileges to build. Only players in survival and creative can build (break/place blocks)
 */
val Gamemode.canBuild: Boolean get() = this == Gamemode.SURVIVAL || this == Gamemode.CREATIVE
