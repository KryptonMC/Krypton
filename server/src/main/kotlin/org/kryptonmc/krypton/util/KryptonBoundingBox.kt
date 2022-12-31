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
import org.kryptonmc.krypton.util.math.Maths
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

    override val sizeX: Double
        get() = maximumX - minimumX
    override val sizeY: Double
        get() = maximumY - minimumY
    override val sizeZ: Double
        get() = maximumZ - minimumZ
    override val size: Double
        get() = (sizeX + sizeY + sizeZ) / 3.0
    override val volume: Double
        get() = sizeX * sizeY * sizeZ
    override val centerX: Double
        get() = Maths.lerp(0.5, minimumX, maximumX)
    override val centerY: Double
        get() = Maths.lerp(0.5, minimumY, maximumY)
    override val centerZ: Double
        get() = Maths.lerp(0.5, minimumZ, maximumZ)

    init {
        require(minimumX <= maximumX) { "Maximum X cannot be less than than minimum X! Maximum is $maximumX and minimum is $minimumX!" }
        require(minimumY <= maximumY) { "Maximum Y cannot be less than than minimum Y! Maximum is $maximumY and minimum is $minimumY!" }
        require(minimumZ <= maximumZ) { "Maximum Z cannot be less than than minimum Z! Maximum is $maximumZ and minimum is $minimumZ!" }
    }

    override fun inflate(x: Double, y: Double, z: Double): BoundingBox =
        KryptonBoundingBox(minimumX - x, minimumY - y, minimumZ - z, maximumX + x, maximumY + y, maximumZ + z)

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

    override fun intersects(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): Boolean =
        this.minimumX < maxX && this.maximumX > minX &&
            this.minimumY < maxY && this.maximumY > minY &&
            this.minimumZ < maxZ && this.maximumZ > minZ

    override fun contains(x: Double, y: Double, z: Double): Boolean =
        x >= minimumX && x < maximumX && y >= minimumY && y < maximumY && z >= minimumZ && z < maximumZ

    object Factory : BoundingBox.Factory {

        override fun zero(): BoundingBox = ZERO

        override fun unit(): BoundingBox = UNIT

        override fun of(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): BoundingBox =
            KryptonBoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
    }

    companion object {

        private val ZERO = KryptonBoundingBox(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        private val UNIT = KryptonBoundingBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
    }
}
