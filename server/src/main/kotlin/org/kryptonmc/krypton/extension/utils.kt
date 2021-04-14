package org.kryptonmc.krypton.extension

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.kryptonmc.krypton.api.world.Gamemode
import java.net.InetAddress
import kotlin.math.log2
import kotlin.math.max

/**
 * Convert a [String] to an [InetAddress]
 *
 * @author Callum Seabrook
 */
fun String.toInetAddress(): InetAddress = InetAddress.getByName(this)

/**
 * Retrieve a [Logger] instance from the specified class [T]
 *
 * @param T the class to retrieve a logger for
 * @return a [Logger] with the name of the specified class [T]
 *
 * @author Callum Seabrook
 */
inline fun <reified T> logger(): Logger = LogManager.getLogger(T::class.java)

/**
 * Retrieve a [Logger] instance with the specified [name]
 *
 * @param name the name of the logger
 * @return a [Logger] with the specified [name]
 *
 * @author Callum Seabrook
 */
fun logger(name: String): Logger = LogManager.getLogger(name)

/**
 * Calculate the amount of non-empty significant bits in this [Int]
 * For example, if the bits are 00110000, this returns 6.
 *
 * @author Callum Seabrook
 */
fun Int.calculateBits() = max(log2(takeHighestOneBit().toDouble()).toInt() + 1, 4)

/**
 * Gets the area from this view distance [Int]
 *
 * @author Callum Seabrook
 */
fun Int.toArea() = (this * 2 + 1).square()

fun Int.square() = this * this

fun String.toProtocol(): ByteArray {
    val bytes = encodeToByteArray()
    return byteArrayOf(bytes.size.toByte(), *bytes)
}

val Gamemode.canBuild: Boolean
    get() = this == Gamemode.SURVIVAL || this == Gamemode.CREATIVE