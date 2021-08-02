/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.world.generation.structure

import com.mojang.serialization.Codec
import org.kryptonmc.krypton.util.fixedSize
import org.kryptonmc.krypton.util.logger
import org.spongepowered.math.vector.Vector3i
import java.util.Objects
import java.util.stream.IntStream
import kotlin.math.max
import kotlin.math.min

class BoundingBox(minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int) {

    var minX = minX
        private set
    var minY = minY
        private set
    var minZ = minZ
        private set
    var maxX = maxX
        private set
    var maxY = maxY
        private set
    var maxZ = maxZ
        private set

    val length: Vector3i
        get() = Vector3i(maxX - minX, maxY - minY, maxZ - minZ)
    val center: Vector3i
        get() = Vector3i(minX + (maxX - minX + 1) / 2, minY + (maxY - minY + 1) / 2, minZ + (maxZ - minZ + 1) / 2)
    val xSpan: Int
        get() = maxX - minX + 1
    val ySpan: Int
        get() = maxY - minY + 1
    val zSpan: Int
        get() = maxZ - minZ + 1

    fun intersects(other: BoundingBox) = maxX >= other.minX && minX <= other.maxX && maxZ >= other.minZ && minZ <= other.maxZ && maxY >= other.minY && minY <= other.maxY

    fun intersects(maxX: Int, maxZ: Int, minX: Int, minZ: Int) = this.maxX >= maxX && this.minX <= minX && this.maxZ >= maxZ && this.minZ <= minZ

    fun encapsulate(other: BoundingBox) = apply {
        minX = min(minX, other.minX)
        minY = min(minY, other.minY)
        minZ = min(minZ, other.minZ)
        maxX = max(maxX, other.maxX)
        maxY = max(maxY, other.maxY)
        maxZ = max(maxZ, other.maxZ)
    }

    fun encapsulate(position: Vector3i) = apply {
        minX = min(minX, position.x())
        minY = min(minY, position.y())
        minZ = min(minZ, position.z())
        maxX = max(maxX, position.x())
        maxY = max(maxY, position.y())
        maxZ = max(maxZ, position.z())
    }

    fun inflate(factor: Int) = apply {
        minX -= factor
        minY -= factor
        minZ -= factor
        maxX += factor
        maxY += factor
        maxZ += factor
    }

    fun move(x: Int, y: Int, z: Int) = apply {
        minX += x
        minY += y
        minZ += z
        maxX += x
        maxY += y
        maxZ += z
    }

    fun move(position: Vector3i) = move(position.x(), position.y(), position.z())

    fun moved(x: Int, y: Int, z: Int) = BoundingBox(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z)

    operator fun contains(position: Vector3i) = position.x() in minX..maxX && position.y() in minY..maxY && position.z() in minZ..maxZ

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as BoundingBox
        return minX == other.minX && minY == other.minY && minZ == other.minZ && maxX == other.maxX && maxY == other.maxY && maxZ == other.maxZ
    }

    override fun hashCode() = Objects.hash(minX, minY, minZ, maxX, maxY, maxZ)

    override fun toString() = "BoundingBox(minX=$minX, minY=$minY, minZ=$minZ, maxX=$maxX, maxY=$maxY, maxZ=$maxZ)"

    companion object {

        private val LOGGER = logger<BoundingBox>()
        val CODEC: Codec<BoundingBox> = Codec.INT_STREAM.comapFlatMap(
            { stream -> stream.fixedSize(6).map { BoundingBox(it[0], it[1], it[2], it[3], it[4], it[5]) } },
            { IntStream.of(it.minX, it.minY, it.minZ, it.maxX, it.maxY, it.maxZ) }
        ).stable()
        val INFINITE = BoundingBox(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
    }
}
