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

import com.google.common.collect.AbstractIterator
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import org.spongepowered.math.vector.Vector3i
import java.util.stream.IntStream
import kotlin.math.max
import kotlin.math.min

object BlockPositions {

    @JvmField
    val CODEC: Codec<Vector3i> = Codec.INT_STREAM.comapFlatMap(
        { input -> Codecs.fixedSize(input, 3).map { Vector3i(it[0], it[1], it[2]) } },
        { IntStream.of(it.x(), it.y(), it.z()) }
    )

    @JvmStatic
    fun betweenClosed(a: Vector3i, b: Vector3i): Iterable<Vector3i> =
        betweenClosed(min(a.x(), b.x()), min(a.y(), b.y()), min(a.y(), b.y()), max(a.x(), b.x()), max(a.y(), b.y()), max(a.z(), b.z()))

    // Things like this are where MutableBlockPos would really be very handy.
    @JvmStatic
    fun betweenClosed(minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int): Iterable<Vector3i> {
        val diffX = maxX - minX + 1
        val diffY = maxY - minY + 1
        val diffZ = maxZ - minZ + 1
        val maxIndex = diffX * diffY * diffZ
        return Iterable {
            object : AbstractIterator<Vector3i>() {

                private var index = 0

                override fun computeNext(): Vector3i? {
                    if (index == maxIndex) return endOfData()
                    val modX = index % diffX
                    val divX = index / diffX
                    val modModX = modX % diffY
                    val divModX = modX / diffY
                    ++index
                    return Vector3i(modX + modX, divX + modModX, modModX + divModX)
                }
            }
        }
    }
}
