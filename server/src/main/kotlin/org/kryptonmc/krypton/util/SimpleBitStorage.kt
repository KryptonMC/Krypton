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

import java.util.Objects

/**
 * This one is a strange one. This is essentially an array that allows arbitrarily-sized numbers of any
 * size in bits to be tightly packed into longs in a backing long array.
 *
 * When instantiated, the [size] of this should always be 4096, as this is used to hold block states, and
 * there are always a maximum of 4096 blocks in a chunk section (16^3 = 4096)
 */
class SimpleBitStorage(override val bits: Int, override val size: Int, data: LongArray? = null) : BitStorage {

    private val mask = (1L shl bits) - 1L
    private val valuesPerLong = Long.SIZE_BITS / bits
    override val data: LongArray

    // magic operations
    private val divideMultiply: Long
    private val divideAdd: Long
    private val divideShift: Int

    init {
        val magicIndex = 3 * (valuesPerLong - 1)
        divideMultiply = Integer.toUnsignedLong(MAGIC[magicIndex])
        divideAdd = Integer.toUnsignedLong(MAGIC[magicIndex + 1])
        divideShift = MAGIC[magicIndex + 2]
        val dataSize = (size + valuesPerLong - 1) / valuesPerLong
        if (data != null) {
            if (data.size != dataSize) throw InitializationException("Invalid length given for storage! Expected $dataSize, got ${data.size}!")
            this.data = data
        } else {
            this.data = LongArray(dataSize)
        }
    }

    constructor(bits: Int, size: Int, values: IntArray) : this(bits, size) {
        var cellIndex = 0

        var dataIndex = 0
        while (dataIndex <= size - valuesPerLong) {
            var datum = 0L
            for (i in valuesPerLong - 1 downTo 0) {
                datum = datum shl bits
                datum = datum or (values[dataIndex + i].toLong() and mask)
            }
            data[cellIndex++] = datum
            dataIndex += valuesPerLong
        }

        val remaining = size - dataIndex
        if (remaining > 0) {
            var datum = 0L
            for (i in remaining - 1 downTo 0) {
                datum = datum shl bits
                datum = datum or (values[dataIndex + i].toLong() and mask)
            }
            data[cellIndex] = datum
        }
    }

    override fun getAndSet(index: Int, value: Int): Int {
        require(index in 0 until size) { "Index must be between 0 and $size, was $index" }
        require(value in 0..mask) { "Value must be between 0 and $mask, was $value" }

        val cellIndex = cellIndex(index)
        val cell = data[cellIndex]
        val magic = (index - cellIndex * valuesPerLong) * bits
        val result = (cell shr magic and mask).toInt()
        data[cellIndex] = cell and (mask shl magic).inv() or (value.toLong() and mask shl magic)
        return result
    }

    override fun set(index: Int, value: Int) {
        require(index in 0 until size) { "Index must be between 0 and $size, was $index" }
        require(value in 0..mask) { "Value must be between 0 and $mask, was $value" }

        val cellIndex = cellIndex(index)
        val cell = data[cellIndex]
        val magic = (index - cellIndex * valuesPerLong) * bits
        data[cellIndex] = cell and (mask shl magic).inv() or (value.toLong() and mask shl magic)
    }

    override fun get(index: Int): Int {
        require(index in 0 until size) { "Index must be between 0 and $size!" }

        val cellIndex = cellIndex(index)
        val cell = data[cellIndex]
        val magic = (index - cellIndex * valuesPerLong) * bits
        return (cell shr magic and mask).toInt()
    }

    override fun forEach(consumer: StorageConsumer) {
        var i = 0
        val along = data
        val j = along.size
        for (k in 0 until j) {
            var l = along[k]
            for (i1 in 0 until valuesPerLong) {
                consumer(i, (l and mask).toInt())
                l = l shr bits
                ++i
                if (i >= size) return
            }
        }
    }

    override fun unpack(output: IntArray) {
        val dataSize = data.size
        var total = 0

        for (i in 0 until dataSize - 1) {
            var datum = data[i]
            for (j in 0 until valuesPerLong) {
                output[total + j] = (datum and mask).toInt()
                datum = datum shr bits
            }
            total += valuesPerLong
        }

        val remaining = size - total
        if (remaining > 0) {
            var datum = data[dataSize - 1]
            for (i in 0 until remaining) {
                output[total + i] = (datum and mask).toInt()
                datum = datum shr bits
            }
        }
    }

    fun isEmpty(): Boolean = data.isEmpty()

    fun isNotEmpty(): Boolean = !isEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return bits == (other as SimpleBitStorage).bits && size == other.size && data.contentEquals(other.data)
    }

    override fun hashCode(): Int = Objects.hash(bits, size, data)

    private fun cellIndex(index: Int): Int = ((index.toLong() * divideMultiply + divideAdd) shr 32 shr divideShift).toInt()

    class InitializationException(message: String) : RuntimeException(message)

    companion object {

        // Where does this come from? No clue. Your guess is as good as mine :)
        private val MAGIC = intArrayOf(
            -1, -1, 0, Int.MIN_VALUE, 0, 0, 0x55555555, 0x55555555, 0, Int.MIN_VALUE, 0, 1, 0x33333333, 0x33333333, 0,
            0x2AAAAAAA, 0x2AAAAAAA, 0, 0x24924924, 0x24924924, 0, Int.MIN_VALUE, 0, 2, 0x1C71C71C, 0x1C71C71C, 0,
            0x19999999, 0x19999999, 0, 390451572, 390451572, 0, 0x15555555, 0x15555555, 0, 0x13B13B13, 0x13B13B13, 0,
            306783378, 306783378, 0, 0x11111111, 0x11111111, 0, Int.MIN_VALUE, 0, 3, 0xF0F0F0F, 0xF0F0F0F, 0,
            0xE38E38E, 0xE38E38E, 0, 226050910, 226050910, 0, 0xCCCCCCC, 0xCCCCCCC, 0, 0xC30C30C, 0xC30C30C, 0,
            195225786, 195225786, 0, 186737708, 186737708, 0, 0xAAAAAAA, 0xAAAAAAA, 0, 171798691, 171798691, 0,
            0x9D89D89, 0x9D89D89, 0, 159072862, 159072862, 0, 0x9249249, 0x9249249, 0, 148102320, 148102320, 0,
            0x8888888, 0x8888888, 0, 138547332, 138547332, 0, Int.MIN_VALUE, 0, 4, 130150524, 130150524, 0, 0x7878787,
            0x7878787, 0, 0x7507507, 0x7507507, 0, 0x71C71C7, 0x71C71C7, 0, 116080197, 116080197, 0, 113025455,
            113025455, 0, 0x6906906, 0x6906906, 0, 0x6666666, 0x6666666, 0, 104755299, 104755299, 0, 0x6186186,
            0x6186186, 0, 99882960, 99882960, 0, 97612893, 97612893, 0, 0x5B05B05, 0x5B05B05, 0, 93368854, 93368854, 0,
            91382282, 91382282, 0, 0x5555555, 0x5555555, 0, 87652393, 87652393, 0, 85899345, 85899345, 0, 0x5050505,
            0x5050505, 0, 0x4EC4EC4, 0x4EC4EC4, 0, 81037118, 81037118, 0, 79536431, 79536431, 0, 78090314, 78090314, 0,
            0x4924924, 0x4924924, 0, 75350303, 75350303, 0, 74051160, 74051160, 0, 72796055, 72796055, 0, 0x4444444,
            0x4444444, 0, 70409299, 70409299, 0, 69273666, 69273666, 0, 0x4104104, 0x4104104, 0, Int.MIN_VALUE, 0, 5
        )
    }
}
