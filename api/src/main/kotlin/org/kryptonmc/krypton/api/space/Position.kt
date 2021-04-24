@file:Suppress("INAPPLICABLE_JVM_NAME", "unused")

package org.kryptonmc.krypton.api.space

import org.jetbrains.annotations.Contract
import org.kryptonmc.krypton.api.world.Location
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * A neat abstraction layer between both [Vector] and [Location].
 */
interface Position : Comparable<Position>, Cloneable {

    /**
     * The X coordinate of this [Position]
     */
    val x: Double

    /**
     * The Y coordinate of this [Position]
     */
    val y: Double

    /**
     * The Z coordinate of this [Position]
     */
    val z: Double

    /**
     * The magnitude of this vector, defined as sqrt(x^2 + y^2 + z^2).
     *
     * The result of this calculation will call [sqrt] if the length has not
     * previously be computed, which may be costly on some CPUs or virtual
     * machines. [Double.NaN] will be returned if the inner result of the
     * [sqrt] function overflows, which will be caused if the length is too
     * long.
     */
    @get:JvmName("length")
    val length: Double

    /**
     * The magnitude of this vector squared.
     */
    val lengthSquared: Double @JvmName("lengthSquared") get() = x * x + y * y + z * z

    /**
     * Add the specified [other] vector to this vector
     *
     * @param other the other vector
     * @return a new vector with its coordinates as the sum of this vector and the [other]
     * vector
     */
    @Contract("_ -> new", pure = true)
    @JvmName("add")
    @JvmDefault
    operator fun plus(other: Position) = apply(x + other.x, y + other.y, z + other.z)

    /**
     * Subtract the specified [other] vector from this vector
     *
     * @param other the other vector
     * @return a new vector with its coordinates as the difference of this vector and the [other]
     * vector
     */
    @Contract("_ -> new", pure = true)
    @JvmName("subtract")
    @JvmDefault
    operator fun minus(other: Position) = apply(x - other.x, y - other.y, z - other.z)

    /**
     * Multiply this vector by the specified [other] vector
     *
     * @param other the other vector
     * @return a new vector with its coordinates as the product of this vector and the [other]
     * vector's coordinates
     */
    @Contract("_ -> new", pure = true)
    @JvmName("multiply")
    @JvmDefault
    operator fun times(other: Position) = apply(x * other.x, y * other.y, z * other.z)

    /**
     * Multiply this vector's coordinates by the specified [factor]
     *
     * @param factor the factor to multiply by
     * @return a new vector with its coordinates multiplied by the specified [factor]
     */
    @Contract("_ -> new", pure = true)
    @JvmName("multiply")
    @JvmDefault
    operator fun times(factor: Int) = apply(x * factor, y * factor, z * factor)

    /**
     * Multiply this vector's coordinates by the specified [factor]
     *
     * @param factor the factor to multiply by
     * @return a new vector with its coordinates multiplied by the specified [factor]
     */
    @Contract("_ -> new", pure = true)
    @JvmName("multiply")
    @JvmDefault
    operator fun times(factor: Double) = apply(x * factor, y * factor, z * factor)

    /**
     * Multiply this vector's coordinates by the specified [factor]
     *
     * @param factor the factor to multiply by
     * @return a new vector with its coordinates multiplied by the specified [factor]
     */
    @Contract("_ -> new", pure = true)
    @JvmName("multiply")
    @JvmDefault
    operator fun times(factor: Float) = apply(x * factor, y * factor, z * factor)

    /**
     * Divide this vector by the specified [other] vector
     *
     * @param other the other vector
     * @return a new vector with its coordinates as the result of dividing each by the other
     * vector's coordinates
     */
    @Contract("_ -> new", pure = true)
    @JvmName("divide")
    @JvmDefault
    operator fun div(other: Position) = apply(x / other.x, y / other.y, z / other.z)

    /**
     * Divide this vector's coordinates by the specified [factor]
     *
     * @param factor the factor to divide by
     * @return a new vector with its coordinates divided by the specified [factor]
     */
    @Contract("_ -> new", pure = true)
    @JvmName("divide")
    @JvmDefault
    operator fun div(factor: Int) = apply(x / factor, y / factor, z / factor)

    /**
     * Divide this vector's coordinates by the specified [factor]
     *
     * @param factor the factor to divide by
     * @return a new vector with its coordinates divided by the specified [factor]
     */
    @Contract("_ -> new", pure = true)
    @JvmName("divide")
    @JvmDefault
    operator fun div(factor: Double) = apply(x / factor, y / factor, z / factor)

    /**
     * Divide this vector's coordinates by the specified [factor]
     *
     * @param factor the factor to divide by
     * @return a new vector with its coordinates divided by the specified [factor]
     */
    @Contract("_ -> new", pure = true)
    @JvmName("divide")
    @JvmDefault
    operator fun div(factor: Float) = apply(x / factor, y / factor, z / factor)

    /**
     * Divide this vector by the specified [other] vector and use the remainder
     *
     * @param other the other vector
     * @return a new vector with its coordinates as the result of dividing each by the other
     * vector's coordinates
     */
    @Contract("_ -> new", pure = true)
    @JvmName("mod")
    @JvmDefault
    operator fun rem(other: Position) = apply(x % other.x, y % other.y, z % other.z)

    /**
     * Divide this vector's coordinates by the specified [factor] and use the remainder
     *
     * @param factor the factor to divide by
     * @return a new vector with its coordinates as the remainder of dividing by the
     * specified [factor]
     */
    @Contract("_ -> new", pure = true)
    @JvmName("mod")
    @JvmDefault
    operator fun rem(factor: Int) = apply(x % factor, y % factor, z % factor)

    /**
     * Divide this vector's coordinates by the specified [factor] and use the remainder
     *
     * @param factor the factor to divide by
     * @return a new vector with its coordinates as the remainder of dividing by the
     * specified [factor]
     */
    @Contract("_ -> new", pure = true)
    @JvmName("mod")
    @JvmDefault
    operator fun rem(factor: Double) = apply(x % factor, y % factor, z % factor)

    /**
     * Divide this vector's coordinates by the specified [factor] and use the remainder
     *
     * @param factor the factor to divide by
     * @return a new vector with its coordinates as the remainder of dividing by the
     * specified [factor]
     */
    @Contract("_ -> new", pure = true)
    @JvmName("mod")
    @JvmDefault
    operator fun rem(factor: Float) = apply(x % factor, y % factor, z % factor)

    /**
     * Invert this vector
     *
     * @return a new vector with its coordinates inverted
     */
    @Contract("_ -> new", pure = true)
    @JvmName("inv")
    @JvmDefault
    operator fun unaryMinus() = apply(-x, -y, -z)

    /**
     * Increment this vector (by 1)
     *
     * @return a new vector with its coordinates incremented (by 1)
     */
    @Contract("_ -> new", pure = true)
    @JvmDefault
    operator fun inc() = apply(x + 1, y + 1, z + 1)

    /**
     * Decrement this vector (by 1)
     *
     * @return a new vector with its coordinates decremented (by 1)
     */
    @Contract("_ -> new", pure = true)
    @JvmDefault
    operator fun dec() = apply(x - 1, y - 1, z - 1)

    /**
     * Calculates the distance between this [Vector] and the specified
     * [other] vector
     *
     * Beware that this calls [sqrt], which may be costly, so avoid
     * repeatedly calling this method to calculate the vector's magnitude.
     * [Double.NaN] will be returned if the inner result of the [sqrt]
     * function overflows, which will occur if the distance is too long.
     *
     * @param other the other vector
     * @return the distance between this vector and the [other] vector
     */
    @Contract(pure = true)
    @JvmDefault
    fun distance(other: Position) = sqrt(distanceSquared(other))

    /**
     * Calculates the squared distance between this vector and
     * the specified [other] vector
     *
     * @param other the other vector
     * @return the distance between this vector and the [other] vector
     */
    @Contract(pure = true)
    @JvmDefault
    fun distanceSquared(other: Position) = (x - other.x).square() + (y - other.y).square() + (z - other.z).square()

    /**
     * Calculates the angle between this vector and the specified [other] vector,
     * in radians.
     *
     * @param other the other vector
     * @return the angle, in radians, between this vector and the [other] vector
     */
    @Contract(pure = true)
    @JvmDefault
    fun angle(other: Position) = acos(max(min(dot(other) / (length * other.length), -1.0), 1.0))

    /**
     * Creates a new [Vector] that is the midpoint between this [Vector] and
     * the specified [other] vector
     *
     * @param other the other vector
     * @return a new [Vector] that is the midpoint between this [Vector] and
     * the specified [other] vector
     */
    @Contract("_ -> new", pure = true)
    @JvmDefault
    fun midpoint(other: Position) = apply((x + other.x) / 2, (y + other.y) / 2, (z + other.z) / 2)

    /**
     * Calculates the dot product of this vector with the specified [other]
     * vector. The dot product is defines as x1 * x2 + y1 * y2 + z1 * z2
     *
     * @param other the other vector
     * @return the dot product of this [Vector] and the [other] vector
     */
    @Contract(pure = true)
    @JvmDefault
    fun dot(other: Position) = x * other.x + y * other.y + z * other.z

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
    @Contract("_ -> new", pure = true)
    @JvmDefault
    fun cross(other: Position) = apply(y * other.z - other.y * z, z * other.x - other.z * x, x * other.y - other.x * y)

    /**
     * Normalises this vector to a unit vector (a vector with a length of 1)
     *
     * @return a new unit [Vector]
     */
    @Contract("_ -> new", pure = true)
    @JvmDefault
    fun normalize(other: Position) = apply(x / length, y / length, z / length)

    /**
     * The floored value of the X component. Used for block coordinates.
     */
    val blockX: Int get() = floor(x).toInt()

    /**
     * The floored value of the Y component. Used for block coordinates.
     */
    val blockY: Int get() = floor(y).toInt()

    /**
     * The floored value of the Z component. Used for block coordinates.
     */
    val blockZ: Int get() = floor(z).toInt()

    /**
     * If this vector is normalised or not. A vector is defined as being normalised
     * if it has a length of 1.
     */
    val isNormalized: Boolean get() = abs(lengthSquared - 1) == 0.0

    /**
     * Apply the given [x], [y] and [z] values to the given object by returning a new
     * object with the applied values
     *
     * This has the same signature as [copy](https://kotlinlang.org/docs/data-classes.html#copying),
     * but cannot be called copy due to Kotlin limitations
     *
     * Generally, if you have an instance of either [Vector] or [Location], you shouldn't
     * need to use this, having this just allows us to provide implementations for most
     * of the functions in this class
     */
    @Contract("_ -> new", pure = true)
    fun apply(x: Double = this.x, y: Double = this.y, z: Double = this.z): Position

    override fun compareTo(other: Position): Int {
        if (x == other.x && y == other.y && z == other.z) return 0
        if (x > other.x && y > other.y && z > other.z) return 1
        if (x < other.x && y < other.y && z < other.z) return -1
        return 0
    }
}
