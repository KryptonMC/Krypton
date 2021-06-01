/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.space.AbstractPosition
import org.kryptonmc.api.space.Position
import org.kryptonmc.api.space.Position.Companion.EPSILON
import org.kryptonmc.api.space.Vector
import kotlin.math.abs

/**
 * A [Location] is a three-dimensional space in a world. That is, it possesses
 * an x, y and z coordinate, as well as angles of rotation in [pitch] and [yaw].
 *
 * As opposed to a [Vector], a [Location] is bound to a world.
 *
 * @param world the world this location is in
 * @see [Vector]
 */
class Location @JvmOverloads constructor(
    val world: World,
    x: Double,
    y: Double,
    z: Double,
    val yaw: Float = 0F,
    val pitch: Float = 0F
) : AbstractPosition(x, y, z) {

    override fun plus(other: Position): Position {
        check(other, "add")
        return super.plus(other)
    }

    override fun minus(other: Position): Position {
        check(other, "subtract")
        return super.minus(other)
    }

    override fun times(other: Position): Position {
        check(other, "multiply")
        return super.times(other)
    }

    override fun div(other: Position): Position {
        check(other, "divide")
        return super.div(other)
    }

    override fun rem(other: Position): Position {
        check(other, "perform modulo division on")
        return super.rem(other)
    }

    private fun check(position: Position, message: String) {
        if (position is Location) require(world == position.world) { "Cannot $message locations from different worlds!" }
    }

    /**
     * Convert this [Location] to a [Vector].
     *
     * @return the new vector from this location
     */
    @Contract("_ -> new", pure = true)
    @Suppress("unused")
    fun toVector() = Vector(x, y, z)

    override fun equals(other: Any?) = other is Location &&
            world == other.world &&
            abs(x - other.x) < EPSILON &&
            abs(y - other.y) < EPSILON &&
            abs(z - other.z) < EPSILON &&
            abs(yaw - other.yaw) < EPSILON &&
            abs(pitch - other.pitch) < EPSILON

    override fun hashCode(): Int {
        var hash = 57 + world.hashCode()
        hash *= 19 + (x.toRawBits() xor (x.toRawBits() shr 32)).toInt()
        hash *= 19 + (y.toRawBits() xor (y.toRawBits() shr 32)).toInt()
        hash *= 19 + (z.toRawBits() xor (z.toRawBits() shr 32)).toInt()
        hash *= 19 + yaw.toRawBits()
        hash *= 19 + pitch.toRawBits()
        return hash
    }

    override fun toString() = "Location(world=$world, x=$x, y=$y, z=$z, yaw=$yaw, pitch=$pitch)"

    override fun copy(x: Double, y: Double, z: Double) = Location(world, x, y, z, yaw, pitch)

    /**
     * Create a copy of this location with the specified values applied to it.
     */
    fun copy(
        world: World = this.world,
        x: Double = this.x,
        y: Double = this.y,
        z: Double = this.z,
        yaw: Float = this.yaw,
        pitch: Float = this.pitch
    ) = Location(world, x, y, z, yaw, pitch)
}
