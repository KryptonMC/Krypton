package org.kryptonmc.krypton.api.world

import org.jetbrains.annotations.Contract
import org.kryptonmc.krypton.api.space.Position
import org.kryptonmc.krypton.api.space.Vector
import kotlin.math.sqrt

/**
 * A [Location] is a three-dimensional space in a world. That is, it possesses
 * an x, y and z coordinate, as well as angles of rotation in [pitch] and [yaw]
 *
 * As opposed to a [Vector], a [Location] is bound to a world.
 *
 * @author Callum Seabrook
 * @see [Vector]
 */
data class Location @JvmOverloads constructor(
    val world: World,
    override val x: Double,
    override val y: Double,
    override val z: Double,
    val yaw: Float = 0F,
    val pitch: Float = 0F
) : Position {

    override val length by lazy { sqrt(lengthSquared) }

    override fun plus(other: Position): Position {
        if (other is Location) require(world == other.world) { "Cannot add locations from different worlds!" }
        return super.plus(other)
    }

    override fun minus(other: Position): Position {
        if (other is Location) require(world == other.world) { "Cannot subtract locations from different worlds!" }
        return super.minus(other)
    }

    override fun times(other: Position): Position {
        if (other is Location) require(world == other.world) { "Cannot multiply locations from different worlds!" }
        return super.times(other)
    }

    override fun div(other: Position): Position {
        if (other is Location) require(world == other.world) { "Cannot divide locations from different worlds!" }
        return super.div(other)
    }

    override fun rem(other: Position): Position {
        if (other is Location) require(world == other.world) { "Cannot perform modulo division on locations from different worlds!" }
        return super.rem(other)
    }

    override fun apply(x: Double, y: Double, z: Double) = copy(x = x, y = y, z = z)

    /**
     * Convert this [Location] to a [Vector]
     *
     * @return the new vector from this location
     */
    @Contract("_ -> new", pure = true)
    @Suppress("unused")
    fun toVector() = Vector(x, y, z)
}