/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")
@file:JvmName("NumberUtils")
package org.kryptonmc.api.space

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.space.Position.Companion.EPSILON
import kotlin.math.abs
import kotlin.random.Random

/**
 * A vector is an element of vector space. That is, a three dimensional space that
 * possesses [both direction and magnitude](https://www.youtube.com/watch?v=bOIe0DIMbI8&t=10s).
 *
 * A [Vector] differs from a [Location] in that it is not world bound and does not
 * possess a yaw or pitch component.
 *
 * In addition, all vectors are immutable. Any attempt to modify any properties of
 * this vector will create a new [Vector] with the changes applied (specifically,
 * through the use of the `copy` function)
 *
 * @see [Location]
 */
@Suppress("Indentation")
public class Vector(x: Double, y: Double, z: Double) : AbstractPosition<Vector>(x, y, z) {

    public constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    public constructor(x: Float, y: Float, z: Float) : this(x.toDouble(), y.toDouble(), z.toDouble())

    /**
     * Convert this [Vector] to a [Location] with the given [pitch] and [yaw].
     *
     * @param yaw the yaw
     * @param pitch the pitch
     * @return the new location from this vector
     */
    @Contract("_ -> new", pure = true)
    public fun toLocation(yaw: Float, pitch: Float): Location = Location(x, y, z, yaw, pitch)

    override fun equals(other: Any?): Boolean = other is Vector &&
            abs(x - other.x) < EPSILON &&
            abs(y - other.y) < EPSILON &&
            abs(z - other.z) < EPSILON

    override fun hashCode(): Int {
        var hash = 7 * 79 + (x.toRawBits() xor (x.toRawBits() shr 32)).toInt()
        hash *= 79 + (y.toRawBits() xor (y.toRawBits() shr 32)).toInt()
        hash *= 79 + (z.toRawBits() xor (z.toRawBits() shr 32)).toInt()
        return hash
    }

    override fun toString(): String = "Vector(x=$x, y=$y, z=$z)"

    override fun copy(x: Double, y: Double, z: Double): Vector = Vector(x, y, z)

    public companion object {

        /**
         * Constant for the zero vector.
         */
        @JvmField
        public val ZERO: Vector = Vector(0, 0, 0)

        /**
         * Constant for a unit vector (one with its coordinates all being 1).
         */
        @JvmField
        public val UNIT: Vector = Vector(1, 1, 1)

        /**
         * Retrieve a random [Vector] that has coordinates with completely random
         * values.
         *
         * Just a roll of the dice
         */
        @JvmStatic
        public fun random(): Vector = Vector(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
    }
}
