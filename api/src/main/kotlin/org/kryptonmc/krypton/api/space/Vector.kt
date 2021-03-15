@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package org.kryptonmc.krypton.api.space

import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World
import kotlin.math.*

/**
 * A vector is an element of vector space. That is, a three dimensional
 * space that possesses both direction and magnitude.
 *
 * A [Vector] differs from a [Location] in that it is not world bound.
 *
 * @author Callum Seabrook
 * @see [Location]
 */
data class Vector(
    val x: Double,
    val y: Double,
    val z: Double
) : Cloneable {

    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun plus(other: Vector) = copy(x = x + other.x, y = y + other.y, z = z + other.z)

    operator fun minus(other: Vector) = copy(x = x - other.x, y = y - other.y, z = z - other.z)

    operator fun times(other: Vector) = copy(x = x * other.x, y = y * other.y, z = z * other.z)

    operator fun times(factor: Int) = copy(x = x * factor, y = y * factor, z = z * factor)

    operator fun times(factor: Double) = copy(x = x * factor, y = y * factor, z = z * factor)

    operator fun times(factor: Float) = copy(x = x * factor, y = y * factor, z = z * factor)

    operator fun div(other: Vector) = copy(x = x / other.x, y = y / other.y, z = z / other.z)

    operator fun rem(other: Vector) = copy(x = x % other.x, y = y % other.y, z = z % other.z)

    operator fun unaryPlus() = copy(x = +x, y = +y, z = +z)

    operator fun unaryMinus() = copy(x = -x, y = -y, z = -z)

    operator fun inc() = copy(x = x + 1, y = y + 1, z = z + 1)

    operator fun dec() = copy(x = x - 1, y = y - 1, z = z - 1)

    /**
     * The magnitude of this vector, defined as sqrt(x^2 + y^2 + z^2).
     *
     * The result of this calculation may call [sqrt] if the length has not
     * previously be computed, which may be costly on some CPUs or virtual
     * machines. [Double.NaN] will be returned if the inner result of the
     * [sqrt] function overflows, which will be caused if the length is too
     * long.
     */
    val length by lazy { sqrt(x * x + y * y + z * z) }

    /**
     * The magnitude of this vector squared.
     */
    val lengthSquared by lazy { x * x + y * y + z * z }

    /**
     * Calculates the distance between this [Vector] and the specified
     * [other] vector
     *
     * Beware that this calls [sqrt], which may be costly, so avoid
     * repeatedly calling this method to calculate the vector's magnitude.
     * NaN will be returned if the inner result of the [sqrt] function
     * overflows, which will occur if the distance is too long.
     *
     * @param other the other vector
     * @return the distance between this vector and the [other] vector
     */
    fun distance(other: Vector) = sqrt((x - other.x).square() + (y - other.y).square() + (z - other.z).square())

    /**
     * Calculates the squared distance between this vector and
     * the specified [other] vector
     *
     * @param other the other vector
     * @return the distance between this vector and the [other] vector
     */
    fun distanceSquared(other: Vector) = (x - other.x).square() + (y - other.y).square() + (z - other.z).square()

    fun angle(other: Vector) = acos(max(min(dot(other) / (length * other.length), -1.0), 1.0))

    /**
     * Creates a new [Vector] that is the midpoint between this [Vector] and
     * the specified [other] vector
     *
     * @param other the other vector
     * @return a new [Vector] that is the midpoint between this [Vector] and
     * the specified [other] vector
     */
    fun midpoint(other: Vector) = copy(x = (x + other.x) / 2, y = (y + other.y) / 2, z = (z + other.z) / 2)

    /**
     * Calculates the dot product of this vector with the specified [other]
     * vector. The dot product is defines as x1 * x2 + y1 * y2 + z1 * z2
     *
     * @param other the other vector
     * @return the dot product of this [Vector] and the [other] vector
     */
    fun dot(other: Vector) = x * other.x + y * other.y + z * other.z

    /**
     * Calculates the cross product of this vector with the specified [other]
     * vector.
     *
     * The cross product is defined as:
     * x = y1 * z2 - y2 * z1
     * y = z1 * x2 - z2 * x1
     * z = x1 * y2 - x2 * y1
     *
     * @param other the other vector
     * @return a new [Vector] that is the cross product of this [Vector] and
     * the specified [other] vector
     */
    fun cross(other: Vector) = copy(x = y * other.z - other.y * z, y = z * other.x - other.z * x, z = x * other.y - other.x * y)

    /**
     * Normalises this vector to a unit vector (a vector with a length of 1)
     *
     * @return a new unit [Vector]
     */
    fun normalize() = copy(x = x / length, y = y / length, z = z / length)

    val isNormalized by lazy { abs(lengthSquared - 1) < EPSILON }

    /**
     * Converts this [Vector] to a [Location] with the specified
     * [world]
     *
     * @param world the world of the new [Location]
     * @return the new location from this vector
     */
    fun toLocation(world: World) = Location(world, x, y, z)

    /**
     * Converts this [Vector] to a [Location] with the specified
     * [world], [pitch] and [yaw].
     *
     * @param world the world of the new [Location]
     * @param yaw the yaw of the new [Location]
     * @param pitch the pitch of the new [Location]
     * @return the new location from this vector
     */
    fun toLocation(world: World, yaw: Float, pitch: Float) = Location(world, x, y, z, yaw, pitch)

    override fun equals(other: Any?): Boolean {
        if (other !is Vector) return false
        return abs(x - other.x) < EPSILON && abs(y - other.y) < EPSILON && abs(z - other.z) < EPSILON && (this::class == other::class)
    }

    override fun hashCode(): Int {
        var hash = 7
        hash = 79 * hash + (x.toRawBits() xor (x.toRawBits() ushr 32)).toInt()
        hash = 79 * hash + (y.toRawBits() xor (y.toRawBits() ushr 32)).toInt()
        hash = 79 * hash + (z.toRawBits() xor (z.toRawBits() ushr 32)).toInt()
        return hash
    }

    companion object {

        /**
         * Error correction term for fuzzy [equals] method, to account
         * for floating point errors.
         */
        const val EPSILON = 0.000001

        val ZERO = Vector(0, 0, 0)
    }
}

/**
 * Returns the square of this number.
 *
 * The square of a number is the number multiplied by itself once.
 *
 * @author Callum Seabrook
 */
fun Double.square() = this * this