/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:Suppress("INAPPLICABLE_JVM_NAME", "unused")

package org.kryptonmc.api.space

import org.jetbrains.annotations.Contract
import kotlin.math.acos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * A neat abstraction layer between both [Vector] and [Location].
 */
public interface Position : Cloneable {

    /**
     * The X coordinate of this [Position].
     */
    @get:JvmName("x")
    public val x: Double

    /**
     * The Y coordinate of this [Position].
     */
    @get:JvmName("y")
    public val y: Double

    /**
     * The Z coordinate of this [Position].
     */
    @get:JvmName("z")
    public val z: Double

    /**
     * The floored value of the [x] component. Used for block coordinates.
     */
    @get:JvmName("blockX")
    public val blockX: Int

    /**
     * The floored value of the [y] component. Used for block coordinates.
     */
    @get:JvmName("blockY")
    public val blockY: Int

    /**
     * The floored value of the [z] component. Used for block coordinates.
     */
    @get:JvmName("blockZ")
    public val blockZ: Int

    /**
     * If this position is normalized or not. A position is defined as being normalised
     * if it has a length of 1.
     */
    public val isNormalized: Boolean

    /**
     * The magnitude of this position, defined as sqrt(x^2 + y^2 + z^2).
     *
     * The result of this calculation will call [sqrt] if the length has not
     * previously be computed, which may be costly on some CPUs or virtual
     * machines. [Double.NaN] will be returned if the inner result of the
     * [sqrt] function overflows, which will be caused if the length is too
     * long.
     */
    @get:JvmName("length")
    public val length: Double

    /**
     * The magnitude of this position, squared. Defined as x^2 + y^2 + z^2
     */
    @get:JvmName("lengthSquared")
    public val lengthSquared: Double

    /**
     * Adds the given [other] position to this position and returns a new position
     * with the result.
     *
     * @param other the other position
     * @return a new position that is the result of the addition
     */
    @Contract("_ -> new", pure = true)
    @JvmName("add")
    public operator fun plus(other: Position): Position

    /**
     * Subtracts the given [other] position from this position and returns a new
     * position with the result.
     *
     * @param other the other position
     * @return a new position that is the result of the subtraction
     */
    @Contract("_ -> new", pure = true)
    @JvmName("subtract")
    public operator fun minus(other: Position): Position

    /**
     * Multiplies this position by the given [other] position and returns a new
     * position with the result.
     *
     * @param other the other position
     * @return a new position that is the result of the multiplication
     */
    @Contract("_ -> new", pure = true)
    @JvmName("multiply")
    public operator fun times(other: Position): Position

    /**
     * Multiplies this position's [x], [y], and [z] coordinates by the given [factor],
     * and returns a new position with the result.
     *
     * @param factor the factor to multiply by
     * @return a new position that is the result of the multiplication
     */
    @Contract("_ -> new", pure = true)
    @JvmName("multiply")
    public operator fun times(factor: Int): Position

    /**
     * Multiplies this position's [x], [y], and [z] coordinates by the given [factor],
     * and returns a new position with the result.
     *
     * @param factor the factor to multiply by
     * @return a new position that is the result of the multiplication
     */
    @Contract("_ -> new", pure = true)
    @JvmName("multiply")
    public operator fun times(factor: Double): Position

    /**
     * Multiplies this position's [x], [y], and [z] coordinates by the given [factor],
     * and returns a new position with the result.
     *
     * @param factor the factor to multiply by
     * @return a new position that is the result of the multiplication
     */
    @Contract("_ -> new", pure = true)
    @JvmName("multiply")
    public operator fun times(factor: Float): Position

    /**
     * Divides this position by the given [other] position and returns a new position
     * with the result.
     *
     * @param other the other position
     * @return a new position that is the result of the division
     */
    @Contract("_ -> new", pure = true)
    @JvmName("divide")
    public operator fun div(other: Position): Position

    /**
     * Divides this position's [x], [y], and [z] coordinates by the given [factor],
     * and returns a new position with the result.
     *
     * @param factor the factor to divide by
     * @return a new position that is the result of the division
     */
    @Contract("_ -> new", pure = true)
    @JvmName("divide")
    public operator fun div(factor: Int): Position

    /**
     * Divides this position's [x], [y], and [z] coordinates by the given [factor],
     * and returns a new position with the result.
     *
     * @param factor the factor to divide by
     * @return a new position that is the result of the division
     */
    @Contract("_ -> new", pure = true)
    @JvmName("divide")
    public operator fun div(factor: Double): Position

    /**
     * Divides this position's [x], [y], and [z] coordinates by the given [factor],
     * and returns a new position with the result.
     *
     * @param factor the factor to divide by
     * @return a new position that is the result of the division
     */
    @Contract("_ -> new", pure = true)
    @JvmName("divide")
    public operator fun div(factor: Float): Position

    /**
     * Divides this position by the given [other] position and uses the remainders
     * to create a new position with the result.
     *
     * @param other the other position
     * @return a new position that is the result of the modulo division
     */
    @Contract("_ -> new", pure = true)
    @JvmName("mod")
    public operator fun rem(other: Position): Position

    /**
     * Divides this position's [x], [y], and [z] coordinates by the given [factor]
     * and used the remainders to create a new position with the result.
     *
     * @param factor the factor to divide by
     * @return a new position that is the result of the modulo division
     */
    @Contract("_ -> new", pure = true)
    @JvmName("mod")
    public operator fun rem(factor: Int): Position

    /**
     * Divides this position's [x], [y], and [z] coordinates by the given [factor]
     * and used the remainders to create a new position with the result.
     *
     * @param factor the factor to divide by
     * @return a new position that is the result of the modulo division
     */
    @Contract("_ -> new", pure = true)
    @JvmName("mod")
    public operator fun rem(factor: Double): Position

    /**
     * Divides this position's [x], [y], and [z] coordinates by the given [factor]
     * and used the remainders to create a new position with the result.
     *
     * @param factor the factor to divide by
     * @return a new position that is the result of the modulo division
     */
    @Contract("_ -> new", pure = true)
    @JvmName("mod")
    public operator fun rem(factor: Float): Position

    /**
     * Inverts this position and returns a new position with the result.
     *
     * @return a new position
     */
    @Contract("_ -> new", pure = true)
    @JvmName("inv")
    public operator fun unaryMinus(): Position

    /**
     * Increments this position by 1 and returns a new position with the result.
     *
     * @return a new position that is the result of the increment
     */
    @Contract("_ -> new", pure = true)
    public operator fun inc(): Position

    /**
     * Decrements this position by 1 and returns a new position with the result.
     *
     * @return a new position that is the result of the decrement
     */
    @Contract("_ -> new", pure = true)
    public operator fun dec(): Position

    /**
     * Calculates the distance between this position and the given [other] position.
     *
     * Beware that this calls [sqrt], which may be costly, so avoid repeatedly
     * calling this method to calculate the position's magnitude. [Double.NaN]
     * will be returned if the inner result of the [sqrt] function overflows, which
     * will occur if the distance is too long.
     *
     * @param other the other position
     * @return the distance between this position and the given other position
     */
    @Contract(pure = true)
    public fun distance(other: Position): Double = sqrt(distanceSquared(other))

    /**
     * Calculates the squared distance between this position and the given [other]
     * position.
     *
     * @param other the other position
     * @return the distance between this position and the other position
     */
    @Contract(pure = true)
    public fun distanceSquared(other: Position): Double = (x - other.x) * (x - other.x) +
            (y - other.y) * (y - other.y) +
            (z - other.z) * (z - other.z)

    /**
     * Calculates the angle between this position and the given [other] position, in
     * radians.
     *
     * @param other the other position
     * @return the angle, in radians, between this position and the given other
     * position
     */
    @Contract(pure = true)
    public fun angle(other: Position): Double = acos(min(max(dot(other) / (length * other.length), -1.0), 1.0))

    /**
     * Creates a new position that is the midpoint between this position and the
     * given [other] position.
     *
     * @param other the other position
     * @return a new position that is the midpoint between this position and the
     * given other position
     */
    @Contract("_ -> new", pure = true)
    public fun midpoint(other: Position): Position

    /**
     * Calculates the dot product of this position with the [other] position.
     *
     * The dot product is defines as x1 * x2 + y1 * y2 + z1 * z2.
     *
     * @param other the other position
     * @return the dot product of this position and the given other position
     */
    @Contract(pure = true)
    public fun dot(other: Position): Double = x * other.x + y * other.y + z * other.z

    /**
     * Calculates the cross product of this position with the [other] position.
     *
     * The cross product is defined as:
     * - x = y1 * z2 - y2 * z1
     * - y = z1 * x2 - z2 * x1
     * - z = x1 * y2 - x2 * y1
     *
     * @param other the other position
     * @return a new position that is the cross product of this position and
     * the given [other] position
     */
    @Contract("_ -> new", pure = true)
    public fun cross(other: Position): Position

    /**
     * Normalises this position to a unit position (a position with a length of 1).
     *
     * @return a new unit position
     */
    @Contract("_ -> new", pure = true)
    public fun normalize(): Position

    /**
     * Copies this object and returns a new object with the given [x], [y] and [z] values.
     *
     * Generally, if you have an instance of either [Vector] or [Location], you shouldn't
     * need to use this, having this just allows us to provide implementations for most
     * of the functions in this class.
     */
    @Contract("_ -> new", pure = true)
    public fun copy(x: Double = this.x, y: Double = this.y, z: Double = this.z): Position

    public companion object {

        /**
         * Error correction term for fuzzy [org.kryptonmc.api.space.Vector.equals] method, to account
         * for floating point errors.
         */
        public const val EPSILON: Double = 0.000001
    }
}
