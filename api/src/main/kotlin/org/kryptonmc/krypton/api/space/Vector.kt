@file:Suppress("unused", "MemberVisibilityCanBePrivate")
@file:JvmName("NumberUtils")
package org.kryptonmc.krypton.api.space

import org.jetbrains.annotations.Contract
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World
import kotlin.math.*
import kotlin.random.Random

/**
 * A vector is an element of vector space. That is, a three dimensional space that
 * possesses both direction and magnitude.
 *
 * A [Vector] differs from a [Location] in that it is not world bound and does not
 * possess a yaw or pitch component.
 *
 * In addition, all vectors are immutable. Any attempt to modify any properties of
 * this vector will create a new [Vector] with the changes applied (specifically,
 * through the use of the `copy` function)
 *
 * @author Callum Seabrook
 * @see [Location]
 */
@Suppress("EqualsOrHashCode") // data class' generated default is what we want
data class Vector(
    override val x: Double,
    override val y: Double,
    override val z: Double
) : Position {

    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    constructor(x: Float, y: Float, z: Float) : this(x.toDouble(), y.toDouble(), z.toDouble())

    /**
     * The magnitude of this vector, defined as sqrt(x^2 + y^2 + z^2).
     *
     * The result of this calculation will call [sqrt] if the length has not
     * previously be computed, which may be costly on some CPUs or virtual
     * machines. [Double.NaN] will be returned if the inner result of the
     * [sqrt] function overflows, which will be caused if the length is too
     * long.
     */
    override val length by lazy { sqrt(lengthSquared) }

    /**
     * Convert this [Vector] to a [Location] with the specified
     * [world]
     *
     * @param world the world of the new [Location]
     * @return the new location from this vector
     */
    @Contract("_ -> new", pure = true)
    fun toLocation(world: World) = Location(world, x, y, z)

    /**
     * Convert this [Vector] to a [Location] with the specified
     * [world], [pitch] and [yaw].
     *
     * @param world the world of the new [Location]
     * @param yaw the yaw of the new [Location]
     * @param pitch the pitch of the new [Location]
     * @return the new location from this vector
     */
    @Contract("_ -> new", pure = true)
    fun toLocation(world: World, yaw: Float, pitch: Float) = Location(world, x, y, z, yaw, pitch)

    override fun equals(other: Any?): Boolean {
        if (other !is Vector) return false
        return abs(x - other.x) < EPSILON && abs(y - other.y) < EPSILON && abs(z - other.z) < EPSILON && (this::class == other::class)
    }

    override fun apply(x: Double, y: Double, z: Double) = copy(x = x, y = y, z = z)

    companion object {

        /**
         * Error correction term for fuzzy [equals] method, to account
         * for floating point errors.
         */
        const val EPSILON = 0.000001

        @JvmField
        val ZERO = Vector(0, 0, 0)

        /**
         * Retrieve a random [Vector] that has coordinates with completely random
         * values.
         *
         * Just a roll of the dice
         */
        @JvmStatic
        fun random() = Vector(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
    }
}

fun Double.square() = this * this