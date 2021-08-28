/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.space

import org.kryptonmc.api.util.floor
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * This abstract class defines common defaults between both of its expected
 * implementations, [Vector] and [Location].
 *
 * To keep consistency, a [Position] will always be destructured into (x, y, z),
 * regardless if the implementation has any other fields (such as [Location] having
 * yaw and pitch).
 *
 * The copy function will also only work on the X, Y and Z coordinates of the location.
 * The implementation may also have its own copy function however that can operate on
 * more than just these three.
 *
 * In addition, this class is also responsible for specialising all of the return types
 * of the functions inherited from [Position], so that assignment works as it should.
 */
@Suppress("UNCHECKED_CAST")
sealed class AbstractPosition<T : AbstractPosition<T>>(
    final override val x: Double,
    final override val y: Double,
    final override val z: Double
) : Position {

    final override val length by lazy { sqrt(lengthSquared) }
    final override val lengthSquared = x * x + y * y + z * z
    final override val blockX = x.floor()
    final override val blockY = y.floor()
    final override val blockZ = z.floor()
    final override val isNormalized = abs(lengthSquared - 1) < Position.EPSILON

    abstract override fun copy(x: Double, y: Double, z: Double): T

    /**
     * The X component of this position, for destructuring
     */
    operator fun component1() = x

    /**
     * The Y component of this position, for destructuring
     */
    operator fun component2() = y

    /**
     * The Z component of this position, for destructuring
     */
    operator fun component3() = z

    final override fun plus(other: Position) = copy(x + other.x, y + other.y, z + other.z)

    final override fun minus(other: Position) = copy(x - other.x, y - other.y, z - other.z)

    final override fun times(other: Position) = copy(x * other.x, y * other.y, z * other.z)

    final override fun times(factor: Int) = copy(x * factor, y * factor, z * factor)

    final override fun times(factor: Double) = copy(x * factor, y * factor, z * factor)

    final override fun times(factor: Float) = copy(x * factor, y * factor, z * factor)

    final override fun div(other: Position) = copy(x / other.x, y / other.y, z / other.z)

    final override fun div(factor: Int) = copy(x / factor, y / factor, z / factor)

    final override fun div(factor: Double) = copy(x / factor, y / factor, z / factor)

    final override fun div(factor: Float) = copy(x / factor, y / factor, z / factor)

    final override fun rem(other: Position) = copy(x % other.x, y % other.y, z % other.z)

    final override fun rem(factor: Int) = copy(x % factor, y % factor, z % factor)

    final override fun rem(factor: Double) = copy(x % factor, y % factor, z % factor)

    final override fun rem(factor: Float) = copy(x % factor, y % factor, z % factor)

    final override fun unaryMinus() = copy(-x, -y, -z)

    final override fun inc() = copy(x + 1, y + 1, z + 1)

    final override fun dec() = copy(x - 1, y - 1, z - 1)

    final override fun midpoint(other: Position) = copy((x + other.x) / 2, (y + other.y) / 2, (z + other.z) / 2)

    final override fun cross(other: Position) = copy(y * other.z - other.y * z, z * other.x - other.z * x, x * other.y - other.x * y)

    final override fun normalize() = copy(x / length, y / length, z / length)
}
