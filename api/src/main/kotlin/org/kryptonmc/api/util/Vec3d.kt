/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import javax.annotation.concurrent.Immutable

/**
 * A vector with 3 double components.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
public interface Vec3d : Comparable<Vec3d> {

    /**
     * The X component of this vector.
     */
    @get:JvmName("x")
    public val x: Double

    /**
     * The Y component of this vector.
     */
    @get:JvmName("y")
    public val y: Double

    /**
     * The Z component of this vector.
     */
    @get:JvmName("z")
    public val z: Double

    /**
     * Adds the given [x], [y], and [z] values to the respective components of
     * this vector and returns the result.
     *
     * @param x the X amount to add
     * @param y the Y amount to add
     * @param z the Z amount to add
     * @return the resulting vector
     */
    public fun add(x: Double, y: Double, z: Double): Vec3d

    /**
     * Adds the given [other] vector to this vector and returns the result.
     *
     * @param other the other vector to add
     * @return the resulting vector
     */
    public fun add(other: Vec3d): Vec3d = add(other.x, other.y, other.z)

    /**
     * Subtracts the given [x], [y], and [z] values from the respective
     * components of this vector and returns the result.
     *
     * @param x the X amount to subtract
     * @param y the Y amount to subtract
     * @param z the Z amount to subtract
     * @return the resulting vector
     */
    public fun subtract(x: Double, y: Double, z: Double): Vec3d

    /**
     * Subtracts the given [other] vector from this vector and returns the
     * result.
     *
     * @param other the other vector to subtract
     * @return the resulting vector
     */
    public fun subtract(other: Vec3d): Vec3d = subtract(other.x, other.y, other.z)

    /**
     * Multiplies the components of this vector by the given [x], [y], and [z]
     * values and returns the result.
     *
     * @param x the X amount to multiply by
     * @param y the Y amount to multiply by
     * @param z the Z amount to multiply by
     * @return the resulting vector
     */
    public fun multiply(x: Double, y: Double, z: Double): Vec3d

    /**
     * Multiplies this vector by the given [other] vector and returns the
     * result.
     *
     * @param other the other vector to multiply by
     * @return the resulting vector
     */
    public fun multiply(other: Vec3d): Vec3d = multiply(other.x, other.y, other.z)

    /**
     * Multiplies the components of this vector by the given [factor] and
     * returns the result.
     *
     * @param factor the factor to multiply by
     * @return the resulting vector
     */
    public fun multiply(factor: Double): Vec3d

    /**
     * Divides the components of this vector by the given [x], [y], and [z]
     * values and returns the result.
     *
     * @param x the X amount to divide by
     * @param y the Y amount to divide by
     * @param z the Z amount to divide by
     * @return the resulting vector
     */
    public fun divide(x: Double, y: Double, z: Double): Vec3d

    /**
     * Divides this vector by the given [other] vector and returns the result.
     *
     * @param other the other vector to divide by
     * @return the resulting vector
     */
    public fun divide(other: Vec3d): Vec3d = divide(other.x, other.y, other.z)

    /**
     * Divides the components of this vector by the given [factor] and returns
     * the result.
     *
     * @param factor the factor to divide by
     * @return the resulting vector
     */
    public fun divide(factor: Double): Vec3d

    /**
     * Calculates the dot product of this vector and the given [x], [y], and
     * [z] components.
     *
     * @param x the X component
     * @param y the Y component
     * @param z the Z component
     * @return the dot product
     */
    public fun dot(x: Double, y: Double, z: Double): Double

    /**
     * Calculates the dot product of this vector and the given [other] vector.
     *
     * @param other the other vector
     * @return the dot product
     */
    public fun dot(other: Vec3d): Double = dot(other.x, other.y, other.z)

    /**
     * Calculates the cross product of this vector and the given [x], [y], and
     * [z] components and returns the result as a vector.
     *
     * @param x the X component
     * @param y the Y component
     * @param z the Z component
     * @return the cross product
     */
    public fun cross(x: Double, y: Double, z: Double): Vec3d

    /**
     * Calculates the cross product of this vector and the given [other] vector
     * and returns the result as a vector.
     *
     * @param other the other vector
     * @return the cross product
     */
    public fun cross(other: Vec3d): Vec3d = cross(other.x, other.y, other.z)

    /**
     * Raises the components of this vector to the given [power] and returns
     * the resulting vector.
     *
     * @param power the power to raise the components to
     * @return the resulting vector
     */
    public fun pow(power: Double): Vec3d

    /**
     * Returns the vector with the absolute value of each component.
     *
     * @return the absolute vector
     */
    public fun abs(): Vec3d

    /**
     * Returns the vector with the components of this vector negated, i.e.
     * positive becomes negative and negative becomes positive.
     *
     * @return the negated vector
     */
    public fun negate(): Vec3d

    /**
     * Calculates the squared distance between this vector and the vector with
     * the given [x], [y], and [z] components.
     *
     * @param x the X component
     * @param y the Y component
     * @param z the Z component
     * @return the squared distance to the components
     */
    public fun distanceSquared(x: Double, y: Double, z: Double): Double

    /**
     * Calculates the squared distance between this vector and the given
     * [other] vector.
     *
     * @param other the other vector
     * @return the squared distance to the other vector
     */
    public fun distanceSquared(other: Vec3d): Double = distanceSquared(other.x, other.y, other.z)

    /**
     * Calculates the distance between this vector and the vector with the
     * given [x], [y], and [z] components.
     *
     * @param x the X component
     * @param y the Y component
     * @param z the Z component
     * @return the distance to the components
     */
    public fun distance(x: Double, y: Double, z: Double): Double

    /**
     * Calculates the distance between this vector and the given [other]
     * vector.
     *
     * @param other the other vector
     * @return the distance to the other vector
     */
    public fun distance(other: Vec3d): Double = distance(other.x, other.y, other.z)

    /**
     * Gets the X component of this vector as an integer using the floor
     * function to always round the value down.
     *
     * @return the floored X component
     */
    public fun floorX(): Int

    /**
     * Gets the Y component of this vector as an integer using the floor
     * function to always round the value down.
     *
     * @return the floored Y component
     */
    public fun floorY(): Int

    /**
     * Gets the Z component of this vector as an integer using the floor
     * function to always round the value down.
     *
     * @return the floored Z component
     */
    public fun floorZ(): Int

    /**
     * Calculates the squared length of this vector.
     *
     * @return the squared length
     */
    public fun lengthSquared(): Double

    /**
     * Calculates the length of this vector.
     *
     * @return the length
     */
    public fun length(): Double

    /**
     * Normalizes this vector to have length 1.
     *
     * @return the normalized vector
     */
    public fun normalize(): Vec3d

    /**
     * Part of the destructuring declaration. Gets the X component.
     *
     * @return the X component
     */
    public operator fun component1(): Double = x

    /**
     * Part of the destructuring declaration. Gets the Y component.
     *
     * @return the Y component
     */
    public operator fun component2(): Double = y

    /**
     * Part of the destructuring declaration. Gets the Z component.
     *
     * @return the Z component
     */
    public operator fun component3(): Double = z

    /**
     * An operation that may be applied to an input vector to produce an output
     * vector.
     */
    public fun interface Operation {

        /**
         * Applies this operation to the given [input] vector, with the given
         * [x], [y], and [z] components as inputs, and returns the result.
         *
         * @param input the input vector
         * @param x the X component
         * @param y the Y component
         * @param z the Z component
         * @return the resulting vector
         */
        public fun apply(input: Vec3d, x: Double, y: Double, z: Double): Vec3i
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun of(x: Double, y: Double, z: Double): Vec3d
    }

    public companion object {

        /**
         * The vector with all its components set to zero.
         */
        @JvmField
        public val ZERO: Vec3d = of(0.0, 0.0, 0.0)

        /**
         * Creates a new double vector from the given [x], [y], and [z]
         * components.
         *
         * @param x the X component
         * @param y the Y component
         * @param z the Z component
         * @return a new vector
         */
        @JvmStatic
        public fun of(x: Double, y: Double, z: Double): Vec3d = Krypton.factory<Factory>().of(x, y, z)
    }
}
