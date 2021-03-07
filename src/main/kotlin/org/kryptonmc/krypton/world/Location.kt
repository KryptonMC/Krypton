package org.kryptonmc.krypton.world

data class Location(
    val world: World,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float = 0.0f,
    val pitch: Float = 0.0f
)