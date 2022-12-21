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
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import java.util.stream.IntStream
import java.util.stream.Stream
import java.util.stream.StreamSupport
import javax.annotation.concurrent.Immutable
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

@Immutable
open class BlockPos(x: Int, y: Int, z: Int) : Vec3i {

    override var x: Int = x
        protected set
    override var y: Int = y
        protected set
    override var z: Int = z
        protected set

    constructor(x: Double, y: Double, z: Double) : this(x.toInt(), y.toInt(), z.toInt())

    fun pack(): Long = pack(x, y, z)

    override fun add(x: Int, y: Int, z: Int): BlockPos {
        if (x == 0 && y == 0 && z == 0) return this
        return BlockPos(this.x + x, this.y + y, this.z + z)
    }

    fun add(x: Double, y: Double, z: Double): BlockPos {
        if (x == 0.0 && y == 0.0 && z == 0.0) return this
        return BlockPos(this.x + x, this.y + y, this.z + z)
    }

    override fun subtract(x: Int, y: Int, z: Int): BlockPos {
        if (x == 0 && y == 0 && z == 0) return this
        return BlockPos(this.x - x, this.y - y, this.z - z)
    }

    override fun multiply(x: Int, y: Int, z: Int): BlockPos {
        if (x == 1 && y == 1 && z == 1) return this
        return BlockPos(this.x * x, this.y * y, this.z * z)
    }

    override fun multiply(factor: Int): BlockPos {
        if (factor == 1) return this
        return BlockPos(x * factor, y * factor, z * factor)
    }

    override fun divide(x: Int, y: Int, z: Int): BlockPos {
        if (x == 1 && y == 1 && z == 1) return this
        return BlockPos(this.x / x, this.y / y, this.z / z)
    }

    override fun divide(factor: Int): BlockPos {
        if (factor == 1) return this
        return BlockPos(x / factor, y / factor, z / factor)
    }

    override fun dot(x: Int, y: Int, z: Int): Int = this.x * x + this.y * y + this.z * z

    override fun cross(x: Int, y: Int, z: Int): BlockPos = BlockPos(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x)

    override fun pow(power: Int): BlockPos = BlockPos(x.toDouble().pow(power), y.toDouble().pow(power), z.toDouble().pow(power))

    override fun abs(): BlockPos = BlockPos(abs(x), abs(y), abs(z))

    override fun negate(): BlockPos = BlockPos(-x, -y, -z)

    override fun distanceSquared(x: Int, y: Int, z: Int): Int {
        val dx = this.x - x
        val dy = this.y - y
        val dz = this.z - z
        return dx * dx + dy * dy + dz * dz
    }

    override fun distance(x: Int, y: Int, z: Int): Float = sqrt(distanceSquared(x, y, z).toDouble()).toFloat()

    override fun lengthSquared(): Int = x * x + y * y + z * z

    override fun length(): Float = sqrt(lengthSquared().toDouble()).toFloat()

    fun above(): BlockPos = BlockPos(x, y + 1, z)

    fun above(distance: Int): BlockPos = if (distance == 0) this else BlockPos(x, y + distance, z)

    fun below(): BlockPos = BlockPos(x, y - 1, z)

    fun below(distance: Int): BlockPos = if (distance == 0) this else BlockPos(x, y - distance, z)

    fun north(): BlockPos = BlockPos(x, y, z - 1)

    fun north(distance: Int): BlockPos = if (distance == 0) this else BlockPos(x, y, z - distance)

    fun south(): BlockPos = BlockPos(x, y, z + 1)

    fun south(distance: Int): BlockPos = if (distance == 0) this else BlockPos(x, y, z + distance)

    fun west(): BlockPos = BlockPos(x - 1, y, z)

    fun west(distance: Int): BlockPos = if (distance == 0) this else BlockPos(x - distance, y, z)

    fun east(): BlockPos = BlockPos(x + 1, y, z)

    fun east(distance: Int): BlockPos = if (distance == 0) this else BlockPos(x + distance, y, z)

    fun relative(direction: Direction): BlockPos = BlockPos(x + direction.normalX, y + direction.normalY, z + direction.normalZ)

    fun relative(direction: Direction, distance: Int): BlockPos {
        if (distance == 0) return this
        return BlockPos(x + direction.normalX * distance, y + direction.normalY * distance, z + direction.normalZ * distance)
    }

    fun relative(axis: Direction.Axis, distance: Int): BlockPos {
        if (distance == 0) return this
        val diffX = if (axis == Direction.Axis.X) distance else 0
        val diffY = if (axis == Direction.Axis.Y) distance else 0
        val diffZ = if (axis == Direction.Axis.Z) distance else 0
        return BlockPos(x + diffX, y + diffY, z + diffZ)
    }

    fun get(axis: Direction.Axis): Int = axis.select(x, y, z)

    open fun immutable(): BlockPos = this

    fun mutable(): Mutable = Mutable(x, y, z)

    final override fun equals(other: Any?): Boolean = this === other || other is BlockPos && x == other.x && y == other.y && z == other.z

    final override fun hashCode(): Int = (y + z * 31) * 31 + x

    final override fun toString(): String = "BlockPos(x=$x, y=$y, z=$z)"

    fun toShortString(): String = "$x, $y, $z"

    final override fun compareTo(other: Vec3i): Int = lengthSquared().compareTo(other.lengthSquared())

    class Mutable(x: Int, y: Int, z: Int) : BlockPos(x, y, z) {

        constructor(x: Double, y: Double, z: Double) : this(x.toInt(), y.toInt(), z.toInt())

        constructor() : this(0, 0, 0)

        fun set(x: Int, y: Int, z: Int): Mutable = apply {
            this.x = x
            this.y = y
            this.z = z
        }

        fun set(x: Double, y: Double, z: Double): Mutable = set(Maths.floor(x), Maths.floor(y), Maths.floor(z))

        fun set(packed: Long): Mutable = set(unpackX(packed), unpackY(packed), unpackZ(packed))

        fun setWithOffset(pos: BlockPos, direction: Direction): Mutable =
            set(pos.x + direction.normalX, pos.y + direction.normalY, pos.z + direction.normalZ)

        fun setX(x: Int): Mutable = apply { this.x = x }

        fun setY(y: Int): Mutable = apply { this.y = y }

        fun setZ(z: Int): Mutable = apply { this.z = z }

        override fun immutable(): BlockPos = BlockPos(x, y, z)
    }

    object Factory : Vec3i.Factory {

        override fun of(x: Int, y: Int, z: Int): Vec3i {
            if (x == 0 && y == 0 && z == 0) return ZERO
            return BlockPos(x, y, z)
        }
    }

    companion object {

        @JvmField
        val CODEC: Codec<BlockPos> = Codec.INT_STREAM.comapFlatMap(
            { stream -> Codecs.fixedSize(stream, 3).map { BlockPos(it[0], it[1], it[2]) } },
            { IntStream.of(it.x, it.y, it.z) }
        )
        @JvmField
        val ZERO: BlockPos = BlockPos(0, 0, 0)
        private val PACKED_X_LENGTH = 1 + Maths.log2(Maths.roundUpPow2(30000000))
        private val PACKED_Z_LENGTH = PACKED_X_LENGTH
        private val PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH
        private val PACKED_X_MASK = (1L shl PACKED_X_LENGTH) - 1L
        private val PACKED_Y_MASK = (1L shl PACKED_Y_LENGTH) - 1L
        private val PACKED_Z_MASK = (1L shl PACKED_Z_LENGTH) - 1L
        private val Z_OFFSET = PACKED_Y_LENGTH
        private val X_OFFSET = PACKED_Y_LENGTH + PACKED_Z_LENGTH

        @JvmStatic
        fun from(vector: Vec3i): BlockPos = if (vector is BlockPos) vector else BlockPos(vector.x, vector.y, vector.z)

        @JvmStatic
        fun unpackX(packed: Long): Int = (packed shl 64 - X_OFFSET - PACKED_X_LENGTH shr 64 - PACKED_X_LENGTH).toInt()

        @JvmStatic
        fun unpackY(packed: Long): Int = (packed shl 64 - PACKED_Y_LENGTH shr 64 - PACKED_Y_LENGTH).toInt()

        @JvmStatic
        fun unpackZ(packed: Long): Int = (packed shl 64 - Z_OFFSET - PACKED_Z_LENGTH shr 64 - PACKED_Z_LENGTH).toInt()

        @JvmStatic
        fun unpack(packed: Long): BlockPos = BlockPos(unpackX(packed), unpackY(packed), unpackZ(packed))

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
        fun withinManhattan(position: BlockPos, sizeX: Int, sizeY: Int, sizeZ: Int): Iterable<BlockPos> {
            val maxDepth = sizeX + sizeY + sizeZ
            return Iterable {
                object : AbstractIterator<BlockPos>() {

                    private val cursor = Mutable()
                    private var currentDepth = 0
                    private var maxX = 0
                    private var maxY = 0
                    private var x = 0
                    private var y = 0
                    private var zMirror = false

                    override fun computeNext(): BlockPos? {
                        if (zMirror) {
                            zMirror = false
                            cursor.setZ(position.z - (cursor.z - position.z))
                            return cursor
                        }
                        var result: BlockPos? = null
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
                                result = cursor.set(position.x + currentX, position.y + currentY, position.z + z)
                            }
                            ++y
                        }
                        return result
                    }
                }
            }
        }

        @JvmStatic
        fun betweenClosed(first: BlockPos, second: BlockPos): Iterable<BlockPos> = betweenClosed(
            min(first.x, second.x), min(first.y, second.y), min(first.z, second.z),
            max(first.x, second.x), max(first.y, second.y), max(first.z, second.z)
        )

        @JvmStatic
        fun betweenClosed(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Iterable<BlockPos> {
            val dx = x2 - x1 + 1
            val dy = y2 - y1 + 1
            val dz = z2 - z1 + 1
            val total = dx * dy * dz
            return Iterable {
                object : AbstractIterator<BlockPos>() {

                    private val cursor = Mutable()
                    private var index = 0

                    override fun computeNext(): BlockPos? {
                        if (index == total) return endOfData()
                        val offsetX = index % dx
                        val offsetY = index / dx % dy
                        val offsetZ = offsetX / dy
                        ++index
                        return cursor.set(x1 + offsetX, y1 + offsetY, z1 + offsetZ)
                    }
                }
            }
        }

        @JvmStatic
        fun betweenClosedStream(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Stream<BlockPos> =
            StreamSupport.stream(betweenClosed(x1, y1, z1, x2, y2, z2).spliterator(), false)

        @JvmStatic
        fun betweenClosedStream(box: BoundingBox): Stream<BlockPos> = betweenClosedStream(
            Maths.floor(box.minimumX), Maths.floor(box.minimumY), Maths.floor(box.minimumZ),
            Maths.floor(box.maximumX), Maths.floor(box.maximumY), Maths.floor(box.maximumZ)
        )
    }
}
