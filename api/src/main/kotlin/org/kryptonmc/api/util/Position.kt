/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * A position with coordinates and rotation.
 *
 * This is primarily used to represent the position of an entity in a world.
 *
 * @property x The X component.
 * @property y The Y component.
 * @property z The Z component.
 * @property yaw The rotation on the X axis.
 * @property pitch The rotation on the Y axis.
 */
@JvmRecord
@Suppress("TooManyFunctions")
public data class Position(
    public val x: Double,
    public val y: Double,
    public val z: Double,
    public val yaw: Float,
    public val pitch: Float
) {

    /**
     * Creates a new position with the given [x], [y], and [z] coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the new position
     */
    public constructor(x: Double, y: Double, z: Double) : this(x, y, z, 0F, 0F)

    /**
     * Gets the block X coordinate of this position.
     *
     * @return the block X
     */
    public fun blockX(): Int = floor(x).toInt()

    /**
     * Gets the block Y coordinate of this position.
     *
     * @return the block Y
     */
    public fun blockY(): Int = floor(y).toInt()

    /**
     * Gets the block Z coordinate of this position.
     *
     * @return the block Z
     */
    public fun blockZ(): Int = floor(z).toInt()

    /**
     * Gets the chunk X coordinate of this position.
     *
     * @return the chunk X
     */
    public fun chunkX(): Int = blockX() shr 4

    /**
     * Gets the chunk Z coordinate of this position.
     *
     * @return the chunk Z
     */
    public fun chunkZ(): Int = blockZ() shr 4

    /**
     * Creates a new position with the given [x] component.
     *
     * @param x the new X component
     * @return the new position
     */
    public fun withX(x: Double): Position = Position(x, this.y, this.z, this.yaw, this.pitch)

    /**
     * Creates a new position with the given [y] component.
     *
     * @param y the new Y component
     * @return the new position
     */
    public fun withY(y: Double): Position = Position(this.x, y, this.z, this.yaw, this.pitch)

    /**
     * Creates a new position with the given [z] component.
     *
     * @param z the new Z component
     * @return the new position
     */
    public fun withZ(z: Double): Position = Position(this.x, this.y, z, this.yaw, this.pitch)

    /**
     * Creates a new position with the given [x], [y], and [z] coordinates and
     * the rotation of this position.
     *
     * @param x the new X coordinate
     * @param y the new Y coordinate
     * @param z the new Z coordinate
     * @return the new position
     */
    public fun withCoordinates(x: Double, y: Double, z: Double): Position = Position(x, y, z, this.yaw, this.pitch)

    /**
     * Creates a new position with the given [yaw].
     *
     * @param yaw the new yaw
     * @return the new position
     */
    public fun withYaw(yaw: Float): Position = Position(this.x, this.y, this.z, yaw, this.pitch)

    /**
     * Creates a new position with the given [pitch].
     *
     * @param pitch the new pitch
     * @return the new position
     */
    public fun withPitch(pitch: Float): Position = Position(this.x, this.y, this.z, this.yaw, pitch)

    /**
     * Creates a new position with the given [yaw] and [pitch] and the
     * coordinates of this position.
     *
     * @param yaw the new yaw
     * @param pitch the new pitch
     * @return the new position
     */
    public fun withRotation(yaw: Float, pitch: Float): Position = Position(this.x, this.y, this.z, yaw, pitch)

    /**
     * Gets a unit vector pointing in the direction that this position is
     * facing.
     *
     * @return the direction vector
     */
    public fun direction(): Vec3d {
        val rotX = yaw.toDouble()
        val rotY = pitch.toDouble()
        val xz = cos(Math.toRadians(rotY))
        return Vec3d(-xz * sin(Math.toRadians(rotX)), -sin(Math.toRadians(rotY)), xz * cos(Math.toRadians(rotX)))
    }

    /**
     * Adds the given [x], [y], and [z] values to this position and returns
     * the result.
     *
     * @param x the X amount to add
     * @param y the Y amount to add
     * @param z the Z amount to add
     * @return the resulting position
     */
    public fun add(x: Double, y: Double, z: Double): Position = Position(this.x + x, this.y + y, this.z + z, this.yaw, this.pitch)

    /**
     * Adds the given [amount] to this position and returns the result.
     *
     * @param amount the amount to add
     * @return the resulting position
     */
    public fun add(amount: Double): Position = add(amount, amount, amount)

    /**
     * Adds the given [other] position to this position and returns the result.
     *
     * Only the coordinates of the other position will be added to this
     * position, **not** the rotation.
     *
     * @param other the position to add
     * @return the resulting position
     */
    public fun add(other: Position): Position = add(other.x, other.y, other.z)

    /**
     * Adds the given [other] vector to this position and returns the result.
     *
     * @param other the vector to add
     * @return the resulting position
     */
    public fun add(other: Vec3d): Position = add(other.x, other.y, other.z)

    /**
     * Adds the given [other] vector to this position and returns the result.
     *
     * @param other the vector to add
     * @return the resulting position
     */
    public fun add(other: Vec3i): Position = add(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())

    /**
     * Subtracts the given [x], [y], and [z] values from this vector and
     * returns the result.
     *
     * @param x the X amount to subtract
     * @param y the Y amount to subtract
     * @param z the Z amount to subtract
     * @return the resulting position
     */
    public fun subtract(x: Double, y: Double, z: Double): Position = Position(this.x - x, this.y - y, this.z - z)

    /**
     * Subtracts the given [amount] from this position and returns the result.
     *
     * @param amount the amount to subtract
     * @return the resulting position
     */
    public fun subtract(amount: Double): Position = subtract(amount, amount, amount)

    /**
     * Subtracts the given [other] position from this position and returns the
     * result.
     *
     * Only the coordinates of the other position will be subtracted from this
     * position, **not** the rotation.
     *
     * @param other the position to subtract
     * @return the resulting position
     */
    public fun subtract(other: Position): Position = subtract(other.x, other.y, other.z)

    /**
     * Subtracts the given [other] vector from this position and returns the
     * result.
     *
     * @param other the vector to subtract
     * @return the resulting position
     */
    public fun subtract(other: Vec3d): Position = subtract(other.x, other.y, other.z)

    /**
     * Subtracts the given [other] vector from this position and returns the
     * result.
     *
     * @param other the vector to subtract
     * @return the resulting position
     */
    public fun subtract(other: Vec3i): Position = subtract(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())

    /**
     * Multiplies this position by the given [x], [y], and [z] values and
     * returns the result.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return the resulting position
     */
    public fun multiply(x: Double, y: Double, z: Double): Position = Position(this.x * x, this.y * y, this.z * z, this.yaw, this.pitch)

    /**
     * Multiplies this position by the given [other] position and returns the
     * result.
     *
     * Only the coordinates of the other position will be multiplied by this
     * position, **not** the rotation.
     *
     * @param other the position to multiply by
     * @return the resulting position
     */
    public fun multiply(other: Position): Position = multiply(other.x, other.y, other.z)

    /**
     * Multiplies this position by the given [other] vector and returns the
     * result.
     *
     * @param other the vector to multiply by
     * @return the resulting position
     */
    public fun multiply(other: Vec3d): Position = multiply(other.x, other.y, other.z)

    /**
     * Multiplies this position by the given [other] vector and returns the
     * result.
     *
     * @param other the vector to multiply by
     * @return the resulting position
     */
    public fun multiply(other: Vec3i): Position = multiply(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())

    /**
     * Multiplies this position by the given [factor] and returns the result.
     *
     * This is equivalent to calling [multiply] with the same factor for each
     * component.
     *
     * @param factor the factor to multiply by
     * @return the resulting position
     */
    public fun multiply(factor: Double): Position = multiply(factor, factor, factor)

    /**
     * Divides this position by the given [x], [y], and [z] values and returns
     * the result.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return the resulting vector
     */
    public fun divide(x: Double, y: Double, z: Double): Position = Position(this.x / x, this.y / y, this.z / z, this.yaw, this.pitch)

    /**
     * Divides this position by the given [other] position and returns the
     * result.
     *
     * Only the coordinates of the other position will be multiplied by this
     * position, **not** the rotation.
     *
     * @param other the position to divide by
     * @return the resulting position
     */
    public fun divide(other: Position): Position = divide(other.x, other.y, other.z)

    /**
     * Divides this position by the given [other] vector and returns the result.
     *
     * @param other the vector to divide by
     * @return the resulting position
     */
    public fun divide(other: Vec3d): Position = divide(other.x, other.y, other.z)

    /**
     * Divides this position by the given [other] vector and returns the result.
     *
     * @param other the vector to divide by
     * @return the resulting position
     */
    public fun divide(other: Vec3i): Position = divide(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())

    /**
     * Divides this position by the given [factor] and returns the result.
     *
     * This is equivalent to calling [divide] with the same factor for each
     * component.
     *
     * @param factor the factor to divide by
     * @return the resulting position
     */
    public fun divide(factor: Double): Position = divide(factor, factor, factor)

    /**
     * Calculates the squared distance between this position and the
     * given [x], [y], and [z] values.
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
     * Calculates the squared distance between this position and the
     * given [other] position.
     *
     * @param other the other position
     * @return the squared distance
     */
    public fun distanceSquared(other: Position): Double = distanceSquared(other.x, other.y, other.z)

    /**
     * Calculates the squared distance between this position and the
     * given [other] vector.
     *
     * @param other the other vector
     * @return the squared distance
     */
    public fun distanceSquared(other: Vec3d): Double = distanceSquared(other.x, other.y, other.z)

    /**
     * Calculates the squared distance between this position and the
     * given [other] vector.
     *
     * @param other the other vector
     * @return the squared distance
     */
    public fun distanceSquared(other: Vec3i): Double = distanceSquared(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())

    /**
     * Calculates the distance between this position and the given [x], [y],
     * and [z] values.
     *
     * @param x the X distance
     * @param y the Y distance
     * @param z the Z distance
     * @return the distance
     */
    public fun distance(x: Double, y: Double, z: Double): Double = sqrt(distanceSquared(x, y, z))

    /**
     * Calculates the distance between this position and the given [other]
     * position.
     *
     * @param other the other position
     * @return the distance
     */
    public fun distance(other: Position): Double = distance(other.x, other.y, other.z)

    /**
     * Calculates the distance between this position and the given [other]
     * vector.
     *
     * @param other the other vector
     * @return the distance
     */
    public fun distance(other: Vec3d): Double = distance(other.x, other.y, other.z)

    /**
     * Calculates the distance between this position and the given [other]
     * vector.
     *
     * @param other the other vector
     * @return the distance
     */
    public fun distance(other: Vec3i): Double = distance(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())

    /**
     * Converts this position to an equivalent double vector.
     *
     * @return the converted vector
     */
    public fun asVec3d(): Vec3d = Vec3d(x, y, z)

    /**
     * Converts this position to an equivalent integer vector.
     *
     * @return the converted vector
     */
    public fun asVec3i(): Vec3i = Vec3i(blockX(), blockY(), blockZ())

    override fun toString(): String = "($x, $y, $z, $yaw, $pitch)"

    public companion object {

        /**
         * The zero position.
         */
        @JvmField
        public val ZERO: Position = Position(0.0, 0.0, 0.0, 0F, 0F)
    }
}
