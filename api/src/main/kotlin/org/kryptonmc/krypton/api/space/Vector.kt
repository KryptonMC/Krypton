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
package org.kryptonmc.krypton.api.space

import org.jetbrains.annotations.Contract
import org.kryptonmc.krypton.api.space.Position.Companion.EPSILON
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World
import kotlin.math.abs
import kotlin.math.sqrt
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
@Suppress("EqualsOrHashCode") // data class' generated default is what we want
data class Vector(
    override val x: Double,
    override val y: Double,
    override val z: Double
) : Position {

    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    constructor(x: Float, y: Float, z: Float) : this(x.toDouble(), y.toDouble(), z.toDouble())

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
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Vector
        return abs(x - other.x) < EPSILON &&
                abs(y - other.y) < EPSILON &&
                abs(z - other.z) < EPSILON
    }

    override fun apply(x: Double, y: Double, z: Double) = copy(x = x, y = y, z = z)

    companion object {

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

/**
 * Square a double
 */
fun Double.square() = this * this
