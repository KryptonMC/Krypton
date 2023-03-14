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
package org.kryptonmc.krypton.coordinate

import com.google.common.collect.AbstractIterator
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import java.util.stream.IntStream
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object BlockPos {

    @JvmField
    val CODEC: Codec<Vec3i> = Codec.INT_STREAM.comapFlatMap(
        { stream -> Codecs.fixedSize(stream, 3).map { Vec3i(it[0], it[1], it[2]) } },
        { IntStream.of(it.x, it.y, it.z) }
    )
    private val PACKED_X_LENGTH = 1 + Maths.log2(Maths.roundUpPow2(30000000))
    private val PACKED_Z_LENGTH = PACKED_X_LENGTH
    private val PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH
    private val PACKED_X_MASK = (1L shl PACKED_X_LENGTH) - 1L
    private val PACKED_Y_MASK = (1L shl PACKED_Y_LENGTH) - 1L
    private val PACKED_Z_MASK = (1L shl PACKED_Z_LENGTH) - 1L
    private val Z_OFFSET = PACKED_Y_LENGTH
    private val X_OFFSET = PACKED_Y_LENGTH + PACKED_Z_LENGTH

    @JvmStatic
    fun unpackX(packed: Long): Int = (packed shl 64 - X_OFFSET - PACKED_X_LENGTH shr 64 - PACKED_X_LENGTH).toInt()

    @JvmStatic
    fun unpackY(packed: Long): Int = (packed shl 64 - PACKED_Y_LENGTH shr 64 - PACKED_Y_LENGTH).toInt()

    @JvmStatic
    fun unpackZ(packed: Long): Int = (packed shl 64 - Z_OFFSET - PACKED_Z_LENGTH shr 64 - PACKED_Z_LENGTH).toInt()

    @JvmStatic
    fun unpack(packed: Long): Vec3i = Vec3i(unpackX(packed), unpackY(packed), unpackZ(packed))

    /*
     * In this encoding, the X, Y, and Z coordinates are packed in to a single long, with the X coordinate occupying the most significant 26 bits,
     * followed by the Z coordinate occupying the next 26 bits, then the Y coordinate, occupying the least significant 12 bits.
     *
     * This encoding allows for the following maximum values:
     * - X and Z: minimum of -33554432 (-2^25) and maximum of 33554431 (2^25 - 1)
     * - Y: minimum of -2048 (-2^11) and maximum of 2047 (2^11 - 1)
     */
    @JvmStatic
    fun pack(x: Int, y: Int, z: Int): Long {
        var result = 0L
        result = result or (x.toLong() and PACKED_X_MASK shl X_OFFSET)
        result = result or (y.toLong() and PACKED_Y_MASK shl 0)
        return result or (z.toLong() and PACKED_Z_MASK shl Z_OFFSET)
    }

    @JvmStatic
    fun pack(pos: Vec3i): Long = pack(pos.x, pos.y, pos.z)

    @JvmStatic
    fun withinManhattan(position: Vec3i, sizeX: Int, sizeY: Int, sizeZ: Int): Iterable<Vec3i> {
        val maxDepth = sizeX + sizeY + sizeZ
        return Iterable {
            object : AbstractIterator<Vec3i>() {

                private var cursor = Vec3i.ZERO
                private var currentDepth = 0
                private var maxX = 0
                private var maxY = 0
                private var x = 0
                private var y = 0
                private var zMirror = false

                override fun computeNext(): Vec3i? {
                    if (zMirror) {
                        zMirror = false
                        cursor = Vec3i(cursor.x, cursor.y, position.z - (cursor.z - position.z))
                        return cursor
                    }

                    var result: Vec3i? = null
                    while (result == null) {
                        if (y > maxY) {
                            ++x
                            if (x > maxX) {
                                ++currentDepth
                                if (currentDepth > maxDepth) return endOfData()
                                maxX = min(sizeX, currentDepth)
                                x = -maxX
                            }
                            maxY = min(sizeY, currentDepth - abs(x))
                            y = -maxY
                        }
                        val currentX = x
                        val currentY = y
                        val z = currentDepth - abs(currentX) - abs(currentY)
                        if (z <= sizeZ) {
                            zMirror = z != 0
                            cursor = Vec3i(position.x + currentX, position.y + currentY, position.z + z)
                            result = cursor
                        }
                        ++y
                    }
                    return result
                }
            }
        }
    }

    @JvmStatic
    fun betweenClosed(first: Vec3i, second: Vec3i): Iterable<Vec3i> = betweenClosed(
        min(first.x, second.x), min(first.y, second.y), min(first.z, second.z),
        max(first.x, second.x), max(first.y, second.y), max(first.z, second.z)
    )

    @JvmStatic
    fun betweenClosed(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Iterable<Vec3i> {
        val dx = x2 - x1 + 1
        val dy = y2 - y1 + 1
        val dz = z2 - z1 + 1
        val total = dx * dy * dz
        return Iterable {
            object : AbstractIterator<Vec3i>() {

                private var index = 0

                override fun computeNext(): Vec3i? {
                    if (index == total) return endOfData()
                    val offsetX = index % dx
                    val offsetY = index / dx % dy
                    val offsetZ = offsetX / dy
                    ++index
                    return Vec3i(x1 + offsetX, y1 + offsetY, z1 + offsetZ)
                }
            }
        }
    }

    @JvmStatic
    fun betweenClosedStream(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Stream<Vec3i> =
        StreamSupport.stream(betweenClosed(x1, y1, z1, x2, y2, z2).spliterator(), false)

    @JvmStatic
    fun betweenClosedStream(box: BoundingBox): Stream<Vec3i> = betweenClosedStream(
        Maths.floor(box.minX), Maths.floor(box.minY), Maths.floor(box.minZ),
        Maths.floor(box.maxX), Maths.floor(box.maxY), Maths.floor(box.maxZ)
    )
}
