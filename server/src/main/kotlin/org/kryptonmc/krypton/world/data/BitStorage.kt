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
package org.kryptonmc.krypton.world.data

/**
 * This one is a strange one. This is essentially an array that allows arbitrarily-sized numbers of any
 * size in bits to be tightly packed into longs in a backing long array.
 *
 * When instantiated, the [size] of this should always be 4096, as this is used to hold block states, and
 * there are always a maximum of 4096 blocks in a chunk section (16^3 = 4096)
 */
class BitStorage(
    private val bits: Int,
    val size: Int,
    data: LongArray? = null
) {

    private val mask = (1L shl bits) - 1L
    private val valuesPerLong = 64 / bits
    val data = data ?: LongArray((size + valuesPerLong - 1) / valuesPerLong)

    // magic operations
    private val divideMultiply: Long
    private val divideAdd: Long
    private val divideShift: Int

    init {
        val threeTimesMask = 3 * (valuesPerLong - 1)
        divideMultiply = Integer.toUnsignedLong(MAGIC[threeTimesMask])
        divideAdd = Integer.toUnsignedLong(MAGIC[threeTimesMask + 1])
        divideShift = MAGIC[threeTimesMask + 2]
    }

    fun getAndSet(index: Int, value: Int): Int {
        require(index in 0 until size) { "Index must be between 0 and $size, was $index" }
        require(value in 0..mask) { "Value must be between 0 and $mask, was $value" }

        val cellIndex = cellIndex(index)
        val cell = data[cellIndex]
        val magic = (index - cellIndex * valuesPerLong) * bits
        val result = (cell shr magic and mask).toInt()
        data[cellIndex] = cell and (mask shl magic).inv() or (value.toLong() and mask shl magic)
        return result
    }

    operator fun set(index: Int, value: Int) {
        require(index in 0 until size) { "Index must be between 0 and $size, was $index" }
        require(value in 0..mask) { "Value must be between 0 and $mask, was $value" }

        val cellIndex = cellIndex(index)
        val cell = data[cellIndex]
        val magic = (index - cellIndex * valuesPerLong) * bits
        data[cellIndex] = cell and (mask shl magic).inv() or (value.toLong() and mask shl magic)
        println("Cell index $cellIndex updated to ${data[cellIndex]} (was $cell)")
    }

    operator fun get(index: Int): Int {
        require(index in 0 until size) { "Index must be between 0 and $size!" }

        val cellIndex = cellIndex(index)
        val cell = data[cellIndex]
        val magic = (index - cellIndex * valuesPerLong) * bits
        return (cell shr magic and mask).toInt()
    }

    fun count(consumer: (Int) -> Unit) {
        var count = 0
        data.forEach {
            var temp = it
            for (i in 0 until valuesPerLong) {
                consumer((temp and mask).toInt())
                temp = temp shr bits
                count++
                if (count >= size) return
            }
        }
    }

    private fun cellIndex(index: Int) = (index.toLong() * divideMultiply + divideAdd shr 32 shr divideShift).toInt()

    fun isEmpty() = data.isEmpty()

    fun isNotEmpty() = !isEmpty()

    override fun equals(other: Any?) = other is BitStorage && bits == other.bits && size == other.size && data.contentEquals(other.data)

    override fun hashCode() = arrayOf(bits, size, data).contentDeepHashCode()

    companion object {

        // Where does this come from? No clue. Your guess is as good as mine :)
        private val MAGIC = intArrayOf(
            -1, -1, 0, Int.MIN_VALUE, 0, 0, 0x55555555, 0x55555555, 0, Int.MIN_VALUE, 0, 1, 0x33333333, 0x33333333, 0,
            0x2AAAAAAA, 0x2AAAAAAA, 0, 0x24924924, 0x24924924, 0, Int.MIN_VALUE, 0, 2, 0x1C71C71C, 0x1C71C71C, 0, 0x19999999,
            0x19999999, 0, 390_451_572, 390_451_572, 0, 0x15555555, 0x15555555, 0, 0x13B13B13, 0x13B13B13, 0, 306_783_378, 306_783_378,
            0, 0x11111111, 0x11111111, 0, Int.MIN_VALUE, 0, 3, 0xF0F0F0F, 0xF0F0F0F, 0, 0xE38E38E, 0xE38E38E, 0, 226_050_910, 226_050_910,
            0, 0xCCCCCCC, 0xCCCCCCC, 0, 0xC30C30C, 0xC30C30C, 0, 195_225_786, 195_225_786, 0, 186_737_708, 186_737_708, 0, 0xAAAAAAA,
            0xAAAAAAA, 0, 171_798_691, 171_798_691, 0, 0x9D89D89, 0x9D89D89, 0, 159_072_862, 159_072_862, 0, 0x9249249, 0x9249249, 0,
            148_102_320, 148_102_320, 0, 0x8888888, 0x8888888, 0, 138_547_332, 138_547_332, 0, Int.MIN_VALUE, 0, 4, 130_150_524,
            130_150_524, 0, 0x7878787, 0x7878787, 0, 0x7507507, 0x7507507, 0, 0x71C71C7, 0x71C71C7, 0, 116_080_197, 116_080_197, 0,
            113_025_455, 113_025_455, 0, 0x6906906, 0x6906906, 0, 0x6666666, 0x6666666, 0, 104_755_299, 104_755_299, 0, 0x6186186,
            0x6186186, 0, 99_882_960, 99_882_960, 0, 97_612_893, 97_612_893, 0, 0x5B05B05, 0x5B05B05, 0, 93_368_854, 93_368_854, 0,
            91_382_282, 91_382_282, 0, 0x5555555, 0x5555555, 0, 87_652_393, 87_652_393, 0, 85_899_345, 85_899_345, 0, 0x5050505, 0x5050505,
            0, 0x4EC4EC4, 0x4EC4EC4, 0, 81_037_118, 81_037_118, 0, 79_536_431, 79_536_431, 0, 78_090_314, 78_090_314, 0, 0x4924924,
            0x4924924, 0, 75_350_303, 75_350_303, 0, 74_051_160, 74_051_160, 0, 72_796_055, 72_796_055, 0, 0x4444444, 0x4444444, 0,
            70_409_299, 70_409_299, 0, 69_273_666, 69_273_666, 0, 0x4104104, 0x4104104, 0, Int.MIN_VALUE, 0, 5
        )
    }
}
