/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import kotlin.math.abs
import kotlin.math.sqrt

/**
 * An integer vector with an X, Y, and Z component.
 *
 * This is mostly used to represent positions of blocks in a world, as blocks
 * are axis-aligned and so have integer coordinates.
 *
 * @property x The X component of this vector.
 * @property y The Y component of this vector.
 * @property z The Z component of this vector.
 */
@JvmRecord
@Suppress("TooManyFunctions")
public data class Vec3i(public val x: Int, public val y: Int, public val z: Int) : Comparable<Vec3i> {

    /**
     * Gets the chunk X coordinate of this vector.
     *
     * @return the chunk X
     */
    public fun chunkX(): Int = x shr 4

    /**
     * Gets the chunk Z coordinate of this vector.
     *
     * @return the chunk Z
     */
    public fun chunkZ(): Int = z shr 4

    /**
     * Creates a new vector with the given [x] component.
     *
     * @param x the new X component
     * @return the new vector
     */
    public fun withX(x: Int): Vec3i = Vec3i(x, this.y, this.z)

    /**
     * Creates a new vector with the given [y] component.
     *
     * @param y the new Y component
     * @return the new vector
     */
    public fun withY(y: Int): Vec3i = Vec3i(this.x, y, this.z)

    /**
     * Creates a new vector with the given [z] component.
     *
     * @param z the new Z component
     * @return the new vector
     */
    public fun withZ(z: Int): Vec3i = Vec3i(this.x, this.y, z)

    /**
     * Adds the given [x], [y], and [z] values to this vector and returns the
     * result.
     *
     * @param x the X amount to add
     * @param y the Y amount to add
     * @param z the Z amount to add
     * @return the resulting vector
     */
    public fun add(x: Int, y: Int, z: Int): Vec3i = Vec3i(this.x + x, this.y + y, this.z + z)

    /**
     * Adds the given [amount] to this vector and returns the result.
     *
     * @param amount the amount to add
     * @return the resulting vector
     */
    public fun add(amount: Int): Vec3i = add(amount, amount, amount)

    /**
     * Adds the given [other] vector to this vector and returns the result.
     *
     * @param other the vector to add
     * @return the resulting vector
     */
    public fun add(other: Vec3i): Vec3i = add(other.x, other.y, other.z)

    /**
     * Adds the given [other] vector to this vector and returns the result.
     *
     * @param other the vector to add
     * @return the resulting vector
     */
    public fun add(other: Vec3d): Vec3i = add(other.floorX(), other.floorY(), other.floorZ())

    /**
     * Subtracts the given [x], [y], and [z] values from this vector and
     * returns the result.
     *
     * @param x the X amount to subtract
     * @param y the Y amount to subtract
     * @param z the Z amount to subtract
     * @return the resulting vector
     */
    public fun subtract(x: Int, y: Int, z: Int): Vec3i = Vec3i(this.x - x, this.y - y, this.z - z)

    /**
     * Subtracts the given [amount] from this vector and returns the result.
     *
     * @param amount the amount to subtract
     * @return the resulting vector
     */
    public fun subtract(amount: Int): Vec3i = subtract(amount, amount, amount)

    /**
     * Subtracts the given [other] vector from this vector and returns the
     * result.
     *
     * @param other the vector to subtract
     * @return the resulting vector
     */
    public fun subtract(other: Vec3i): Vec3i = subtract(other.x, other.y, other.z)

    /**
     * Subtracts the given [other] vector from this vector and returns the
     * result.
     *
     * @param other the vector to subtract
     * @return the resulting vector
     */
    public fun subtract(other: Vec3d): Vec3i = subtract(other.floorX(), other.floorY(), other.floorZ())

    /**
     * Multiplies this vector by the given [x], [y], and [z] values and
     * returns the result.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return the resulting vector
     */
    public fun multiply(x: Int, y: Int, z: Int): Vec3i = Vec3i(this.x * x, this.y * y, this.z * z)

    /**
     * Multiplies this vector by the given [other] vector and returns the
     * result.
     *
     * @param other the vector to multiply by
     * @return the resulting vector
     */
    public fun multiply(other: Vec3i): Vec3i = multiply(other.x, other.y, other.z)

    /**
     * Multiplies this vector by the given [other] vector and returns the
     * result.
     *
     * @param other the vector to multiply by
     * @return the resulting vector
     */
    public fun multiply(other: Vec3d): Vec3i = multiply(other.floorX(), other.floorY(), other.floorZ())

    /**
     * Multiplies this vector by the given [factor] and returns the result.
     *
     * This is equivalent to calling [multiply] with the same factor for each
     * component.
     *
     * @param factor the factor to multiply by
     * @return the resulting vector
     */
    public fun multiply(factor: Int): Vec3i = multiply(factor, factor, factor)

    /**
     * Divides this vector by the given [x], [y], and [z] values and returns
     * the result.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return the resulting vector
     */
    public fun divide(x: Int, y: Int, z: Int): Vec3i = Vec3i(this.x / x, this.y / y, this.z / z)

    /**
     * Divides this vector by the given [other] vector and returns the result.
     *
     * @param other the vector to divide by
     * @return the resulting vector
     */
    public fun divide(other: Vec3i): Vec3i = divide(other.x, other.y, other.z)

    /**
     * Divides this vector by the given [other] vector and returns the result.
     *
     * @param other the vector to divide by
     * @return the resulting vector
     */
    public fun divide(other: Vec3d): Vec3i = divide(other.floorX(), other.floorY(), other.floorZ())

    /**
     * Divides this vector by the given [factor] and returns the result.
     *
     * This is equivalent to calling [divide] with the same factor for each
     * component.
     *
     * @param factor the factor to divide by
     * @return the resulting vector
     */
    public fun divide(factor: Int): Vec3i = divide(factor, factor, factor)

    /**
     * Gets the vector in the given [direction] relative to this vector.
     *
     * For example, if this vector is (3, 4, 5) and the direction is NORTH,
     * the result will be (3, 4, 4), as north is in the -Z direction.
     */
    public fun relative(direction: Direction): Vec3i = add(direction.normal)

    /**
     * Calculates the dot product of this vector and the given [other] vector.
     *
     * @param other the other vector
     * @return the dot product
     */
    public fun dot(other: Vec3i): Int = x * other.x + y * other.y + z * other.z

    /**
     * Calculates the cross product of this vector and the given [other]
     * vector.
     *
     * @param other the other vector
     * @return the cross product
     */
    public fun cross(other: Vec3i): Vec3i = Vec3i(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x)

    /**
     * Returns a vector with the absolute values of the components of this
     * vector.
     *
     * @return an absolute vector
     */
    public fun abs(): Vec3i = Vec3i(abs(x), abs(y), abs(z))

    /**
     * Returns a vector with the components of this vector negated.
     *
     * @return a negated vector
     */
    public fun negate(): Vec3i = Vec3i(-x, -y, -z)

    /**
     * Calculates the squared distance between this vector and the given [x],
     * [y], and [z] values.
     *
     * @param x the X distance
     * @param y the Y distance
     * @param z the Z distance
     * @return the squared distance
     */
    public fun distanceSquared(x: Int, y: Int, z: Int): Int {
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
    public fun distanceSquared(other: Vec3i): Int = distanceSquared(other.x, other.y, other.z)

    /**
     * Calculates the squared distance between this vector and the
     * given [other] vector.
     *
     * @param other the other vector
     * @return the squared distance
     */
    public fun distanceSquared(other: Vec3d): Int = distanceSquared(other.floorX(), other.floorY(), other.floorZ())

    /**
     * Calculates the distance between this vector and the given [x], [y],
     * and [z] values.
     *
     * @param x the X distance
     * @param y the Y distance
     * @param z the Z distance
     * @return the distance
     */
    public fun distance(x: Int, y: Int, z: Int): Double = sqrt(distanceSquared(x, y, z).toDouble())

    /**
     * Calculates the distance between this vector and the given [other]
     * vector.
     *
     * @param other the other vector
     * @return the distance
     */
    public fun distance(other: Vec3i): Double = distance(other.x, other.y, other.z)

    /**
     * Calculates the distance between this vector and the given [other]
     * vector.
     *
     * @param other the other vector
     * @return the distance
     */
    public fun distance(other: Vec3d): Double = distance(other.floorX(), other.floorY(), other.floorZ())

    /**
     * Computes the length of this vector.
     *
     * @return the length
     */
    public fun lengthSquared(): Int = x * x + y * y + z * z

    /**
     * Calculates the length of this vector.
     *
     * @return the length
     */
    public fun length(): Double = sqrt(lengthSquared().toDouble())

    /**
     * Converts this integer vector to an equivalent double vector.
     *
     * @return the converted vector
     */
    public fun asVec3d(): Vec3d = Vec3d(x.toDouble(), y.toDouble(), z.toDouble())

    /**
     * Converts this integer vector to an equivalent double vector, at the
     * center of a block.
     *
     * This is done by adding 0.5 to each of the components.
     *
     * @return the converted centered vector
     */
    public fun asCenteredVec3d(): Vec3d = Vec3d(x + 0.5, y + 0.5, z + 0.5)

    /**
     * Converts this integer vector to an equivalent position.
     *
     * @return the converted position
     */
    public fun asPosition(): Position = Position(x.toDouble(), y.toDouble(), z.toDouble())

    override fun compareTo(other: Vec3i): Int = lengthSquared().compareTo(other.lengthSquared())

    override fun toString(): String = "($x, $y, $z)"

    public companion object {

        /**
         * The zero vector.
         */
        @JvmField
        public val ZERO: Vec3i = Vec3i(0, 0, 0)
    }
}
