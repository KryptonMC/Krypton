package org.kryptonmc.krypton.api.world

import org.kryptonmc.krypton.api.space.Vector

/**
 * A [Location] is a three-dimensional space in a world. That is, it possesses
 * an x, y and z coordinate, as well as angles of rotation in [pitch] and [yaw]
 *
 * As opposed to a [Vector], a [Location] is bound to a world.
 *
 * @author Callum Seabrook
 * @see [Vector]
 */
data class Location(
    val world: World,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float = 0.0f,
    val pitch: Float = 0.0f
) : Cloneable {

    operator fun plus(other: Location): Location {
        if (world != other.world) throw IllegalArgumentException("Cannot add locations from different worlds!")
        return copy(x = x + other.x, y = y + other.y, z = z + other.z)
    }

    operator fun plus(other: Vector) = copy(x = x + other.x, y = y + other.y, z = z + other.z)

    operator fun minus(other: Location): Location {
        if (world != other.world) throw IllegalArgumentException("Cannot add locations from different worlds!")
        return copy(x = x - other.x, y = y - other.y, z = z - other.z)
    }

    operator fun minus(other: Vector) = copy(x = x - other.x, y = y - other.y, z = z - other.z)

    operator fun times(other: Location) = copy(x = x * other.x, y = y * other.y, z = z * other.z)

    operator fun times(factor: Int) = copy(x = x * factor, y = y * factor, z = z * factor)

    operator fun times(factor: Double) = copy(x = x * factor, y = y * factor, z = z * factor)

    operator fun times(factor: Float) = copy(x = x * factor, y = y * factor, z = z * factor)

    fun toVector() = Vector(x, y, z)
}