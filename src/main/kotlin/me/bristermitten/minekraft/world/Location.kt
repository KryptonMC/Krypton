package me.bristermitten.minekraft.world

import kotlin.math.roundToInt

data class Location(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float = 0.0f,
    val pitch: Float = 0.0f
)

fun Float.toAngle() = ((this / 360.0f) * 256.0f).roundToInt()
