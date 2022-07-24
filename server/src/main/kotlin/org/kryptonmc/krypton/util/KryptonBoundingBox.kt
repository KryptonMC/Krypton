/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util

import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.util.Direction
import org.spongepowered.math.GenericMath
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i
import kotlin.math.max
import kotlin.math.min

@JvmRecord
data class KryptonBoundingBox(
    override val minimumX: Double,
    override val minimumY: Double,
    override val minimumZ: Double,
    override val maximumX: Double,
    override val maximumY: Double,
    override val maximumZ: Double
) : BoundingBox {

    override val xSize: Double
        get() = maximumX - minimumX
    override val ySize: Double
        get() = maximumY - minimumY
    override val zSize: Double
        get() = maximumZ - minimumZ
    override val size: Double
        get() = (xSize + ySize + zSize) / 3.0
    override val volume: Double
        get() = xSize * ySize * zSize
    override val centerX: Double
        get() = GenericMath.lerp(minimumX, maximumX, 0.5)
    override val centerY: Double
        get() = GenericMath.lerp(minimumY, maximumY, 0.5)
    override val centerZ: Double
        get() = GenericMath.lerp(minimumZ, maximumZ, 0.5)

    init {
        require(minimumX <= maximumX) { "Maximum X cannot be less than than minimum X! Maximum is $maximumX and minimum is $minimumX!" }
        require(minimumY <= maximumY) { "Maximum Y cannot be less than than minimum Y! Maximum is $maximumY and minimum is $minimumY!" }
        require(minimumZ <= maximumZ) { "Maximum Z cannot be less than than minimum Z! Maximum is $maximumZ and minimum is $minimumZ!" }
    }

    override fun minimum(axis: Direction.Axis): Double = axis.select(minimumX, minimumY, minimumZ)

    override fun maximum(axis: Direction.Axis): Double = axis.select(maximumX, maximumY, maximumZ)

    override fun inflate(xFactor: Double, yFactor: Double, zFactor: Double): BoundingBox {
        val newMinX = minimumX - xFactor
        val newMinY = minimumY - yFactor
        val newMinZ = minimumZ - zFactor
        val newMaxX = maximumX + xFactor
        val newMaxY = maximumY + yFactor
        val newMaxZ = maximumZ + zFactor
        return KryptonBoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
    }

    override fun intersect(other: BoundingBox): BoundingBox {
        val newMinX = max(minimumX, other.minimumX)
        val newMinY = max(minimumY, other.minimumY)
        val newMinZ = max(minimumZ, other.minimumZ)
        val newMaxX = min(maximumX, other.maximumX)
        val newMaxY = min(maximumY, other.maximumY)
        val newMaxZ = min(maximumZ, other.maximumZ)
        return KryptonBoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
    }

    override fun move(x: Double, y: Double, z: Double): BoundingBox =
        KryptonBoundingBox(minimumX + x, minimumY + y, minimumZ + z, maximumX + x, maximumY + y, maximumZ + z)

    override fun expand(x: Double, y: Double, z: Double): BoundingBox {
        var minX = minimumX
        var minY = minimumY
        var minZ = minimumZ
        var maxX = maximumX
        var maxY = maximumY
        var maxZ = maximumZ
        if (x < 0.0) minX += x else if (x > 0.0) maxX += x
        if (y < 0.0) minY += y else if (y > 0.0) maxY += y
        if (z < 0.0) minZ += z else if (z > 0.0) maxZ += z
        return KryptonBoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
    }

    override fun contract(x: Double, y: Double, z: Double): BoundingBox {
        var minX = minimumX
        var minY = minimumY
        var minZ = minimumZ
        var maxX = maximumX
        var maxY = maximumY
        var maxZ = maximumZ
        if (x < 0.0) minX -= x else if (x > 0.0) maxX -= x
        if (y < 0.0) minY -= y else if (y > 0.0) maxY -= y
        if (z < 0.0) minZ -= z else if (z > 0.0) maxZ -= z
        return KryptonBoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
    }

    override fun intersects(minimumX: Double, minimumY: Double, minimumZ: Double, maximumX: Double, maximumY: Double, maximumZ: Double): Boolean =
        this.minimumX < maximumX && this.maximumX > minimumX &&
            this.minimumY < maximumY && this.maximumY > minimumY &&
            this.minimumZ < maximumZ && this.maximumZ > minimumZ

    override fun intersects(other: BoundingBox): Boolean =
        intersects(other.minimumX, other.minimumY, other.minimumZ, other.maximumX, other.maximumY, other.maximumZ)

    override fun contains(x: Double, y: Double, z: Double): Boolean =
        x in minimumX..maximumX - 1 &&
            y in minimumY..maximumY - 1 &&
            z in minimumZ..maximumZ - 1

    object Factory : BoundingBox.Factory {

        override fun zero(): BoundingBox = ZERO

        override fun unit(): BoundingBox = UNIT

        override fun of(minimumX: Double, minimumY: Double, minimumZ: Double, maximumX: Double, maximumY: Double, maximumZ: Double): BoundingBox =
            KryptonBoundingBox(minimumX, minimumY, minimumZ, maximumX, maximumY, maximumZ)
    }

    companion object {

        private val ZERO = KryptonBoundingBox(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        private val UNIT = KryptonBoundingBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
    }
}
