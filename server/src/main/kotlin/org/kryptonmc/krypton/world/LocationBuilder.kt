package org.kryptonmc.krypton.world

/**
 * This is to avoid cyclic dependencies by allowing a world to build its own location
 */
data class LocationBuilder(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float = 0.0f,
    val pitch: Float = 0.0f
)