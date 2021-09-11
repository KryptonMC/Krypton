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
 * To keep consistency, a [Position] will always be destructured into
 * (x, y, z), regardless if the implementation has any other fields (such as
 * [Location] having yaw and pitch).
 *
 * The copy function will also only work on the X, Y and Z coordinates of the
 * location.
 * The implementation may also have its own copy function however that can
 * operate on more than just these three.
 *
 * In addition, this class is also responsible for specialising all of the
 * return types of the functions inherited from [Position], so that assignment
 * works as it should.
 */
@Suppress("UNCHECKED_CAST")
public sealed class AbstractPosition<T : AbstractPosition<T>>(
    final override val x: Double,
    final override val y: Double,
    final override val z: Double
) : Position {

    final override val length: Double by lazy { sqrt(lengthSquared) }
    final override val lengthSquared: Double = x * x + y * y + z * z
    final override val blockX: Int = x.floor()
    final override val blockY: Int = y.floor()
    final override val blockZ: Int = z.floor()
    final override val isNormalized: Boolean = abs(lengthSquared - 1) < Position.EPSILON

    abstract override fun copy(x: Double, y: Double, z: Double): T

    /**
     * Gets the X component of this position, for destructuring.
     */
    public operator fun component1(): Double = x

    /**
     * Gets the Y component of this position, for destructuring.
     */
    public operator fun component2(): Double = y

    /**
     * Gets the Z component of this position, for destructuring.
     */
    public operator fun component3(): Double = z

    final override fun plus(other: Position): T = copy(x + other.x, y + other.y, z + other.z)

    final override fun minus(other: Position): T = copy(x - other.x, y - other.y, z - other.z)

    final override fun times(other: Position): T = copy(x * other.x, y * other.y, z * other.z)

    final override fun times(factor: Int): T = copy(x * factor, y * factor, z * factor)

    final override fun times(factor: Double): T = copy(x * factor, y * factor, z * factor)

    final override fun times(factor: Float): T = copy(x * factor, y * factor, z * factor)

    final override fun div(other: Position): T = copy(x / other.x, y / other.y, z / other.z)

    final override fun div(factor: Int): T = copy(x / factor, y / factor, z / factor)

    final override fun div(factor: Double): T = copy(x / factor, y / factor, z / factor)

    final override fun div(factor: Float): T = copy(x / factor, y / factor, z / factor)

    final override fun rem(other: Position): T = copy(x % other.x, y % other.y, z % other.z)

    final override fun rem(factor: Int): T = copy(x % factor, y % factor, z % factor)

    final override fun rem(factor: Double): T = copy(x % factor, y % factor, z % factor)

    final override fun rem(factor: Float): T = copy(x % factor, y % factor, z % factor)

    final override fun unaryMinus(): T = copy(-x, -y, -z)

    final override fun inc(): T = copy(x + 1, y + 1, z + 1)

    final override fun dec(): T = copy(x - 1, y - 1, z - 1)

    final override fun midpoint(other: Position): T = copy((x + other.x) / 2, (y + other.y) / 2, (z + other.z) / 2)

    final override fun cross(other: Position): T =
        copy(y * other.z - other.y * z, z * other.x - other.z * x, x * other.y - other.x * y)

    final override fun normalize(): T = copy(x / length, y / length, z / length)
}
