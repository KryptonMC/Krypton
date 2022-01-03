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
package org.kryptonmc.krypton

import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.krypton.util.BitStorage
import org.kryptonmc.krypton.util.SimpleBitStorage
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BitStorageTests {

    @Test
    fun `test retrieval`() {
        val data = longArrayOf(6148914691236517205, 6148914691236517205)
        val storage = SimpleBitStorage(BITS, SIZE, data)
        assertThrows<IllegalArgumentException> { storage[32] }
        assertThrows<IllegalArgumentException> { storage[-1] }
        for (i in 0..31) {
            assertEquals(5, storage[i])
        }
    }

    @Test
    fun `test construction`() {
        val valuesPerLong = Long.SIZE_BITS / BITS
        val dataSize = (SIZE + valuesPerLong - 1) / valuesPerLong
        assertThrows<SimpleBitStorage.InitializationException> { SimpleBitStorage(BITS, SIZE, LongArray(dataSize + 1)) }
        assertDoesNotThrow { SimpleBitStorage(BITS, SIZE, LongArray(dataSize)) }
        assertEquals(dataSize, SimpleBitStorage(BITS, SIZE).data.size)
    }

    @Test
    fun `test setting of data`() {
        val storage = SimpleBitStorage(BITS, SIZE)
        assertThrows<IllegalArgumentException> { storage[32] = 0 }
        assertThrows<IllegalArgumentException> { storage[-1] = 0 }
        assertThrows<IllegalArgumentException> { storage[0] = 16 }
        assertThrows<IllegalArgumentException> { storage[0] = -1 }
        storage[0] = 5
        assertEquals(5, storage[0])
    }

    @Test
    fun `test get and set`() {
        val storage = SimpleBitStorage(BITS, SIZE)
        assertThrows<IllegalArgumentException> { storage.getAndSet(32, 0) }
        assertThrows<IllegalArgumentException> { storage.getAndSet(-1, 0) }
        assertThrows<IllegalArgumentException> { storage.getAndSet(0, 16) }
        assertThrows<IllegalArgumentException> { storage.getAndSet(0, -1) }
        assertEquals(0, storage.getAndSet(0, 5))
        assertEquals(5, storage[0])
    }

    @Test
    fun `test iteration`() {
        val data = longArrayOf(6148914691236517205, 6148914691236517205)
        val storage = SimpleBitStorage(BITS, SIZE, data)
        storage.forEach { _, value -> assertEquals(5, value) }
        storage[0] = 12
        storage.forEach { index, value -> if (index == 0) assertEquals(12, value) }
    }

    @Test
    fun `test unpacking to int array`() {
        val data = longArrayOf(6148914691236517205, 6148914691236517205)
        val storage = SimpleBitStorage(BITS, SIZE, data)
        val output = IntArray(SIZE)
        assertThrows<ArrayIndexOutOfBoundsException> { storage.unpack(IntArray(0)) }
        assertDoesNotThrow { storage.unpack(output) }
        output.forEach { assertEquals(5, it) }
    }

    @Test
    fun `test equality and hashing`() {
        val data = longArrayOf(6148914691236517205, 6148914691236517205)
        val first = SimpleBitStorage(BITS, SIZE, data)
        val second = SimpleBitStorage(BITS, SIZE, data)
        val third = SimpleBitStorage(BITS, SIZE + 1, data.copyOf(data.size + 1))
        val fourth = SimpleBitStorage(BITS - 1, SIZE, data)
        val fifth = SimpleBitStorage(BITS, SIZE, data.copyOf().apply { set(0, 10) })
        assertEquals(first, first)
        assertEquals(first, second)
        assertNotEquals<BitStorage?>(first, null)
        assertNotEquals(first, third)
        assertNotEquals(first, fourth)
        assertNotEquals(first, fifth)
        assertEquals(first.hashCode(), second.hashCode())
    }

    @Test
    fun `test data packing constructor`() {
        val data = longArrayOf(6148914691236517205, 6148914691236517205)
        val unpacked = IntArray(32) { 5 }
        val storage = SimpleBitStorage(BITS, SIZE, unpacked)
        assertContentEquals(data, storage.data)
    }

    companion object {

        private const val BITS = 4
        private const val SIZE = 32
    }
}
