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

import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.krypton.util.bits.SimpleBitStorage
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class BitStorageTest {

    @Test
    fun `ensure get with index less than 0 fails`() {
        val storage = SimpleBitStorage(BITS, SIZE, copyData())
        assertThrows<IllegalArgumentException> { storage.get(-1) }
    }

    @Test
    fun `ensure get with index equal to size fails`() {
        val storage = SimpleBitStorage(BITS, SIZE, copyData())
        assertThrows<IllegalArgumentException> { storage.get(storage.size) }
    }

    @Test
    fun `ensure get with index between 0 and size succeeds`() {
        val storage = SimpleBitStorage(BITS, SIZE, copyData())
        for (i in 0 until SIZE) {
            assertEquals(5, storage.get(i))
        }
    }

    @Test
    fun `ensure data with size greater than bit size fails`() {
        val valuesPerLong = Long.SIZE_BITS / BITS
        val dataSize = (SIZE + valuesPerLong - 1) / valuesPerLong
        assertThrows<SimpleBitStorage.InitializationException> { SimpleBitStorage(BITS, SIZE, LongArray(dataSize + 1)) }
    }

    @Test
    fun `ensure data with size equal to bit size succeeds`() {
        val valuesPerLong = Long.SIZE_BITS / BITS
        val dataSize = (SIZE + valuesPerLong - 1) / valuesPerLong
        assertDoesNotThrow { SimpleBitStorage(BITS, SIZE, LongArray(dataSize)) }
    }

    @Test
    fun `ensure no data results in correct data size being allocated`() {
        val valuesPerLong = Long.SIZE_BITS / BITS
        val dataSize = (SIZE + valuesPerLong - 1) / valuesPerLong
        assertEquals(dataSize, SimpleBitStorage(BITS, SIZE).data.size)
    }

    @Test
    fun `ensure set with index equal to size fails`() {
        assertThrows<IllegalArgumentException> { SimpleBitStorage(BITS, SIZE).set(32, 0) }
    }

    @Test
    fun `ensure set with index less than 0 fails`() {
        assertThrows<IllegalArgumentException> { SimpleBitStorage(BITS, SIZE).set(-1, 0) }
    }

    @Test
    fun `ensure set with value more than available bits fails`() {
        assertThrows<IllegalArgumentException> { SimpleBitStorage(BITS, SIZE).set(0, 16) }
    }

    @Test
    fun `ensure set with value less than 0 fails`() {
        assertThrows<IllegalArgumentException> { SimpleBitStorage(BITS, SIZE).set(0, -1) }
    }

    @Test
    fun `ensure set with value actually sets the value`() {
        val storage = SimpleBitStorage(BITS, SIZE)
        storage.set(0, 5)
        assertEquals(5, storage.get(0))
    }

    @Test
    fun `ensure get and set set with index equal to size fails`() {
        assertThrows<IllegalArgumentException> { SimpleBitStorage(BITS, SIZE).getAndSet(32, 0) }
    }

    @Test
    fun `ensure get and set with index less than 0 fails`() {
        assertThrows<IllegalArgumentException> { SimpleBitStorage(BITS, SIZE).getAndSet(-1, 0) }
    }

    @Test
    fun `ensure get and set with value more than available bits fails`() {
        assertThrows<IllegalArgumentException> { SimpleBitStorage(BITS, SIZE).getAndSet(0, 16) }
    }

    @Test
    fun `ensure get and set with value less than 0 fails`() {
        assertThrows<IllegalArgumentException> { SimpleBitStorage(BITS, SIZE).getAndSet(0, -1) }
    }

    @Test
    fun `ensure get and set with value actually sets the value`() {
        val storage = SimpleBitStorage(BITS, SIZE)
        assertEquals(0, storage.getAndSet(0, 5))
        assertEquals(5, storage.get(0))
    }

    @Test
    fun `ensure all values set correctly in for each`() {
        val storage = SimpleBitStorage(BITS, SIZE, copyData())
        assertFalse(storage.data.isEmpty())
        storage.forEach { _, value -> assertEquals(5, value) }
    }

    @Test
    fun `ensure set is visible in for each`() {
        val storage = SimpleBitStorage(BITS, SIZE, copyData())
        storage.set(0, 12)
        storage.forEach { index, value ->
            val expectedValue = if (index == 0) 12 else 5
            assertEquals(expectedValue, value)
        }
    }

    @Test
    fun `ensure unpacking to empty array fails`() {
        val storage = SimpleBitStorage(BITS, SIZE, copyData())
        assertThrows<ArrayIndexOutOfBoundsException> { storage.unpack(IntArray(0)) }
    }

    @Test
    fun `ensure unpacking to array with correct size succeeds`() {
        val storage = SimpleBitStorage(BITS, SIZE, copyData())
        assertDoesNotThrow { storage.unpack(IntArray(SIZE)) }
    }

    @Test
    fun `ensure unpacking to array with correct size fills with correct values`() {
        val storage = SimpleBitStorage(BITS, SIZE, copyData())
        val output = IntArray(SIZE)
        storage.unpack(output)
        output.forEach { assertEquals(5, it) }
    }

    @Test
    fun `verify equals and hash code`() {
        EqualsVerifier.forClass(SimpleBitStorage::class.java)
            .withIgnoredFields("mask", "valuesPerLong", "divideMultiply", "divideAdd", "divideShift")
            .verify()
    }

    @Test
    fun `ensure data packing constructor packs data correctly`() {
        val data = copyData()
        val unpacked = IntArray(SIZE) { 5 }
        val storage = SimpleBitStorage(BITS, SIZE, unpacked)
        assertContentEquals(data, storage.data)
    }

    companion object {

        private const val BITS = 4
        private const val SIZE = 32
        private val DATA = longArrayOf(6148914691236517205, 6148914691236517205)

        @JvmStatic
        private fun copyData(): LongArray = DATA.copyOf()
    }
}
