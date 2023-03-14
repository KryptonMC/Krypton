/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.util

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * A double vector with an X, Y, and Z component.
 *
 * @property x The X component of this vector.
 * @property y The Y component of this vector.
 * @property z The Z component of this vector.
 */
@JvmRecord
@Suppress("TooManyFunctions")
public data class Vec3d(public val x: Double, public val y: Double, public val z: Double) : Comparable<Vec3d> {

    /**
     * Gets the floored X component of this vector.
     *
     * @return the floored X component
     */
    public fun floorX(): Int = floor(x).toInt()

    /**
     * Gets the floored Y component of this vector.
     *
     * @return the floored Y component
     */
    public fun floorY(): Int = floor(y).toInt()

    /**
     * Gets the floored Z component of this vector.
     *
     * @return the floored Z component
     */
    public fun floorZ(): Int = floor(z).toInt()

    /**
     * Creates a new vector with the given [x] component.
     *
     * @param x the new X component
     * @return the new vector
     */
    public fun withX(x: Double): Vec3d = Vec3d(x, this.y, this.z)

    /**
     * Creates a new vector with the given [y] component.
     *
     * @param y the new Y component
     * @return the new vector
     */
    public fun withY(y: Double): Vec3d = Vec3d(this.x, y, this.z)

    /**
     * Creates a new vector with the given [z] component.
     *
     * @param z the new Z component
     * @return the new vector
     */
    public fun withZ(z: Double): Vec3d = Vec3d(this.x, this.y, z)

    /**
     * Adds the given [x], [y], and [z] values to this vector and returns the
     * result.
     *
     * @param x the X amount to add
     * @param y the Y amount to add
     * @param z the Z amount to add
     * @return the resulting vector
     */
    public fun add(x: Double, y: Double, z: Double): Vec3d = Vec3d(this.x + x, this.y + y, this.z + z)

    /**
     * Adds the given [amount] to this vector and returns the result.
     *
     * @param amount the amount to add
     * @return the resulting vector
     */
    public fun add(amount: Double): Vec3d = add(amount, amount, amount)

    /**
     * Adds the given [other] vector to this vector and returns the result.
     *
     * @param other the vector to add
     * @return the resulting vector
     */
    public fun add(other: Vec3d): Vec3d = add(other.x, other.y, other.z)

    /**
     * Adds the given [other] vector to this vector and returns the result.
     *
     * @param other the vector to add
     * @return the resulting vector
     */
    public fun add(other: Vec3i): Vec3d = add(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())

    /**
     * Subtracts the given [x], [y], and [z] values from this vector and
     * returns the result.
     *
     * @param x the X amount to subtract
     * @param y the Y amount to subtract
     * @param z the Z amount to subtract
     * @return the resulting vector
     */
    public fun subtract(x: Double, y: Double, z: Double): Vec3d = Vec3d(this.x - x, this.y - y, this.z - z)

    /**
     * Subtracts the given [amount] from this vector and returns the result.
     *
     * @param amount the amount to subtract
     * @return the resulting vector
     */
    public fun subtract(amount: Double): Vec3d = subtract(amount, amount, amount)

    /**
     * Subtracts the given [other] vector from this vector and returns the
     * result.
     *
     * @param other the vector to subtract
     * @return the resulting vector
     */
    public fun subtract(other: Vec3d): Vec3d = subtract(other.x, other.y, other.z)

    /**
     * Subtracts the given [other] vector from this vector and returns the
     * result.
     *
     * @param other the vector to subtract
     * @return the resulting vector
     */
    public fun subtract(other: Vec3i): Vec3d = subtract(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())

    /**
     * Multiplies this vector by the given [x], [y], and [z] values and
     * returns the result.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return the resulting vector
     */
    public fun multiply(x: Double, y: Double, z: Double): Vec3d = Vec3d(this.x * x, this.y * y, this.z * z)

    /**
     * Multiplies this vector by the given [other] vector and returns the
     * result.
     *
     * @param other the vector to multiply by
     * @return the resulting vector
     */
    public fun multiply(other: Vec3d): Vec3d = multiply(other.x, other.y, other.z)

    /**
     * Multiplies this vector by the given [other] vector and returns the
     * result.
     *
     * @param other the vector to multiply by
     * @return the resulting vector
     */
    public fun multiply(other: Vec3i): Vec3d = multiply(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())

    /**
     * Multiplies this vector by the given [factor] and returns the result.
     *
     * This is equivalent to calling [multiply] with the same factor for each
     * component.
     *
     * @param factor the factor to multiply by
     * @return the resulting vector
     */
    public fun multiply(factor: Double): Vec3d = multiply(factor, factor, factor)

    /**
     * Divides this vector by the given [x], [y], and [z] values and returns
     * the result.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return the resulting vector
     */
    public fun divide(x: Double, y: Double, z: Double): Vec3d = Vec3d(this.x / x, this.y / y, this.z / z)

    /**
     * Divides this vector by the given [other] vector and returns the result.
     *
     * @param other the vector to divide by
     * @return the resulting vector
     */
    public fun divide(other: Vec3d): Vec3d = divide(other.x, other.y, other.z)

    /**
     * Divides this vector by the given [other] vector and returns the result.
     *
     * @param other the vector to divide by
     * @return the resulting vector
     */
    public fun divide(other: Vec3i): Vec3d = divide(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())

    /**
     * Divides this vector by the given [factor] and returns the result.
     *
     * This is equivalent to calling [divide] with the same factor for each
     * component.
     *
     * @param factor the factor to divide by
     * @return the resulting vector
     */
    public fun divide(factor: Double): Vec3d = divide(factor, factor, factor)

    /**
     * Calculates the dot product of this vector and the given [other] vector.
     *
     * @param other the other vector
     * @return the dot product
     */
    public fun dot(other: Vec3d): Double = x * other.x + y * other.y + z * other.z

    /**
     * Calculates the cross product of this vector and the given [other]
     * vector.
     *
     * @param other the other vector
     * @return the cross product
     */
    public fun cross(other: Vec3d): Vec3d = Vec3d(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x)

    /**
     * Returns a vector with the absolute values of the components of this
     * vector.
     *
     * @return an absolute vector
     */
    public fun abs(): Vec3d = Vec3d(abs(x), abs(y), abs(z))

    /**
     * Returns a vector with the components of this vector negated.
     *
     * @return a negated vector
     */
    public fun negate(): Vec3d = Vec3d(-x, -y, -z)

    /**
     * Returns a normalized version of this vector.
     *
     * A normalized vector is defined as a vector with a length of 1.
     *
     * @return a normalized vector
     */
    public fun normalize(): Vec3d {
        val length = length()
        return Vec3d(x / length, y / length, z / length)
    }

    /**
     * Calculates the squared distance between this vector and the given [x],
     * [y], and [z] values.
     *
     * @param x the X distance
     * @param y the Y distance
     * @param z the Z distance
     * @return the squared distance
     */
    public fun distanceSquared(x: Double, y: Double, z: Double): Double {
        val dx = this.x - x
        val dy = this.y - y
        val dz = this.z - z
        return dx * dx + dy * dy + dz * dz
    }

    /**
     * Calculates the squared distance between this vector and the
     * given [other] vector.
     *
     * @param other the other vector
     * @return the squared distance
     */
    public fun distanceSquared(other: Vec3d): Double = distanceSquared(other.x, other.y, other.z)

    /**
     * Calculates the squared distance between this vector and the
     * given [other] vector.
     *
     * @param other the other vector
     * @return the squared distance
     */
    public fun distanceSquared(other: Vec3i): Double = distanceSquared(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())

    /**
     * Calculates the distance between this vector and the given [x], [y],
     * and [z] values.
     *
     * @param x the X distance
     * @param y the Y distance
     * @param z the Z distance
     * @return the distance
     */
    public fun distance(x: Double, y: Double, z: Double): Double = sqrt(distanceSquared(x, y, z))

    /**
     * Calculates the distance between this vector and the given [other]
     * vector.
     *
     * @param other the other vector
     * @return the distance
     */
    public fun distance(other: Vec3d): Double = distance(other.x, other.y, other.z)

    /**
     * Calculates the distance between this vector and the given [other]
     * vector.
     *
     * @param other the other vector
     * @return the distance
     */
    public fun distance(other: Vec3i): Double = distance(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())

    /**
     * Computes the length of this vector.
     *
     * @return the length
     */
    public fun lengthSquared(): Double = x * x + y * y + z * z

    /**
     * Calculates the length of this vector.
     *
     * @return the length
     */
    public fun length(): Double = sqrt(lengthSquared())

    /**
     * Converts this double vector to an equivalent integer vector.
     *
     * @return the converted vector
     */
    public fun asVec3i(): Vec3i = Vec3i(floorX(), floorY(), floorZ())

    /**
     * Converts this double vector to an equivalent position.
     *
     * @return the converted position
     */
    public fun asPosition(): Position = Position(x, y, z)

    override fun compareTo(other: Vec3d): Int = lengthSquared().compareTo(other.lengthSquared())

    override fun toString(): String = "($x, $y, $z)"

    public companion object {

        /**
         * The zero vector.
         */
        @JvmField
        public val ZERO: Vec3d = Vec3d(0.0, 0.0, 0.0)
    }
}
