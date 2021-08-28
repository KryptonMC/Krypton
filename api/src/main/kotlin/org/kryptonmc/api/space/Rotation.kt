/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.space

import java.util.Objects

/**
 * Holder for three rotation amounts, each on their respective axes.
 *
 * Each value must be a [finite][Float.isFinite] value between 0 and
 * 360. If a value larger than 360 degrees is provided, it will be
 * clamped to 360 degrees.
 */
public class Rotation(x: Float, y: Float, z: Float) {

    /**
     * The X rotation, in degrees.
     */
    public val x: Float = if (x.isFinite()) x % 360F else 0F

    /**
     * The Y rotation, in degrees.
     */
    public val y: Float = if (y.isFinite()) y % 360F else 0F

    /**
     * The Z rotation, in degrees.
     */
    public val z: Float = if (z.isFinite()) z % 360F else 0F

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Rotation
        return x == other.x && y == other.y && z == other.z
    }

    override fun hashCode(): Int = Objects.hash(x, y, z)

    override fun toString(): String = "Rotation(x=$x, y=$y, z=$z)"
}
