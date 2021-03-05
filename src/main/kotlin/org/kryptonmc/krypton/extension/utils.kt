package org.kryptonmc.krypton.extension

import org.slf4j.Logger
import java.net.InetAddress
import kotlin.math.log
import kotlin.math.max
import org.slf4j.LoggerFactory

fun String.toInetAddress(): InetAddress = InetAddress.getByName(this)

inline fun <reified T> logger(): Logger = LoggerFactory.getLogger(T::class.java)

fun Int.calculateBits() = max(log(takeHighestOneBit().toDouble(), 2.0).toInt() + 1, 4)

fun Int.toArea() = (this * 2 + 1).square()

fun Int.square() = this * this