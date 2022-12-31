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

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.coordinate.ChunkPos
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MathsTests {

    @Test
    fun `test powers of two do not round up`() {
        for (i in 0 until 31) { // There are 31 signed integer powers of two (that are non-negative)
            assertEquals(1 shl i, Maths.roundUpPow2(1 shl i))
        }
    }

    @Test
    fun `test round up to nearest power of two`() {
        for (i in 0 until 31) {
            // We use / 2 + 1 because it always gives us a value that is 1 above a power of two, which, if rounded up successfully, should imply
            // that all other values will round up properly.
            assertEquals(1 shl i, Maths.roundUpPow2((1 shl i) / 2 + 1))
        }
    }

    @Test
    fun `test powers of two return true for is power of two`() {
        for (i in 0 until 31) {
            assertTrue(Maths.isPowerOfTwo(1 shl i))
        }
    }

    @Test
    fun `test non powers of two return false for is power of two`() {
        for (i in 2 until 31) {
            assertFalse(Maths.isPowerOfTwo((1 shl i) / 2 + 1))
        }
    }

    @Test
    fun `test clamping`() {
        assertEquals(-10, Maths.clamp(-20, -10, 10))
        assertEquals(10, Maths.clamp(20, -10, 10))
        assertEquals(-10, Maths.clamp(-11, -10, 10))
        assertEquals(10, Maths.clamp(11, -10, 10))
        assertEquals(-5, Maths.clamp(-5, -10, 10))
        assertEquals(5, Maths.clamp(5, -10, 10))
        assertEquals(-1.0, Maths.clamp(-2.0, -1.0, 1.0))
        assertEquals(1.0, Maths.clamp(2.0, -1.0, 1.0))
        assertEquals(-1.0, Maths.clamp(-1.000000000000001, -1.0, 1.0))
        assertEquals(1.0, Maths.clamp(1.000000000000001, -1.0, 1.0))
        assertEquals(0.5, Maths.clamp(0.5, 0.0, 1.0))
        assertEquals(-1F, Maths.clamp(-2F, -1F, 1F))
        assertEquals(1F, Maths.clamp(2F, -1F, 1F))
        assertEquals(-1F, Maths.clamp(-1.0000001F, -1F, 1F))
        assertEquals(1F, Maths.clamp(1.0000001F, -1F, 1F))
        assertEquals(0.5F, Maths.clamp(0.5F, 0F, 1F))
    }

    @Test
    fun `test next random value returns low when low greater than high`() {
        // We make sure that any calls always return a predictable value, so the random generator can't just generate the boundary values
        // and pass the tests.
        val random = mockk<RandomSource> {
            every { nextInt(any()) } returns 0
            every { nextFloat() } returns 0F
        }
        assertEquals(10, Maths.nextInt(random, 10, 5))
        assertEquals(10, Maths.nextInt(random, 10, 10))
        assertEquals(1F, Maths.nextFloat(random, 1F, 0.5F))
        assertEquals(1F, Maths.nextFloat(random, 1F, 1F))
    }

    @Test
    fun `test ceil round up for positive and negative values`() {
        assertEquals(10, Maths.ceil(9.1))
        assertEquals(10, Maths.ceil(9.00000000000001))
        assertEquals(10, Maths.ceil(9.9))
        assertEquals(10, Maths.ceil(9.99999999999999))
        assertEquals(10, Maths.ceil(10.0))
        assertEquals(-9, Maths.ceil(-9.9))
        assertEquals(-9, Maths.ceil(-9.99999999999999))
        assertEquals(-9, Maths.ceil(-9.1))
        assertEquals(-9, Maths.ceil(-9.00000000000001))
        assertEquals(-9, Maths.ceil(-9.0))
    }

    @Test
    fun `test floor round down for positive and negative values`() {
        // Float floor
        assertEquals(9, Maths.floor(9.9F))
        assertEquals(9, Maths.floor(9.999999F))
        assertEquals(9, Maths.floor(9.1F))
        assertEquals(9, Maths.floor(9.000001F))
        assertEquals(9, Maths.floor(9F))
        assertEquals(-10, Maths.floor(-9.1F))
        assertEquals(-10, Maths.floor(-9.000001F))
        assertEquals(-10, Maths.floor(-9.9F))
        assertEquals(-10, Maths.floor(-9.999999F))
        assertEquals(-10, Maths.floor(-10F))
        // Double floor
        assertEquals(9, Maths.floor(9.9))
        assertEquals(9, Maths.floor(9.99999999999999))
        assertEquals(9, Maths.floor(9.1))
        assertEquals(9, Maths.floor(9.00000000000001))
        assertEquals(9, Maths.floor(9.0))
        assertEquals(-10, Maths.floor(-9.1))
        assertEquals(-10, Maths.floor(-9.00000000000001))
        assertEquals(-10, Maths.floor(-9.9))
        assertEquals(-10, Maths.floor(-9.99999999999999))
        assertEquals(-10, Maths.floor(-10.0))
        // Double floor to long
        assertEquals(9L, Maths.lfloor(9.9))
        assertEquals(9L, Maths.lfloor(9.99999999999999))
        assertEquals(9L, Maths.lfloor(9.1))
        assertEquals(9L, Maths.lfloor(9.00000000000001))
        assertEquals(9L, Maths.lfloor(9.0))
        assertEquals(-10L, Maths.lfloor(-9.1))
        assertEquals(-10L, Maths.lfloor(-9.00000000000001))
        assertEquals(-10L, Maths.lfloor(-9.9))
        assertEquals(-10L, Maths.lfloor(-9.99999999999999))
        assertEquals(-10L, Maths.lfloor(-10.0))
    }

    @Test
    fun `test ceil log 2`() {
        assertEquals(25, Maths.ceillog2(30000000))
        assertEquals(5, Maths.ceillog2(32))
        assertEquals(6, Maths.ceillog2(35))
    }

    @Test
    fun `test lerping produces correct values`() {
        doFLerpTest(11.57F, 4F, 25F)
        doFLerpTest(11.57F, 7.42F, 9.21F)
        doFLerpTest(8.92F, 4F, 25F)
        doFLerpTest(8.92F, 7.42F, 9.21F)
        doDLerpTest(11.57, 4.0, 25.0)
        doDLerpTest(11.57, 7.42, 9.21)
        doDLerpTest(8.92, 4.0, 25.0)
        doDLerpTest(8.92, 7.42, 9.21)
    }

    private fun doFLerpTest(delta: Float, start: Float, end: Float) {
        assertEquals(start + delta * (end - start), Maths.lerp(delta, start, end))
    }

    private fun doDLerpTest(delta: Double, start: Double, end: Double) {
        assertEquals(start + delta * (end - start), Maths.lerp(delta, start, end))
    }

    @Test
    fun `test chunk in spiral with known values`() {
        doChunkInSpiralTests(0, 0)
        doChunkInSpiralTests(2, 2)
        doChunkInSpiralTests(-2, -2)
        doChunkInSpiralTests(2, -2)
        doChunkInSpiralTests(-2, 2)
    }

    private fun doChunkInSpiralTests(xOffset: Int, zOffset: Int) {
        assertEquals(ChunkPos.pack(0 + xOffset, 0 + zOffset), Maths.chunkInSpiral(0, xOffset, zOffset))
        assertEquals(ChunkPos.pack(0 + xOffset, -1 + zOffset), Maths.chunkInSpiral(1, xOffset, zOffset))
        assertEquals(ChunkPos.pack(1 + xOffset, -1 + zOffset), Maths.chunkInSpiral(2, xOffset, zOffset))
        assertEquals(ChunkPos.pack(1 + xOffset, 0 + zOffset), Maths.chunkInSpiral(3, xOffset, zOffset))
        assertEquals(ChunkPos.pack(1 + xOffset, 1 + zOffset), Maths.chunkInSpiral(4, xOffset, zOffset))
        assertEquals(ChunkPos.pack(0 + xOffset, 1 + zOffset), Maths.chunkInSpiral(5, xOffset, zOffset))
        assertEquals(ChunkPos.pack(-1 + xOffset, 1 + zOffset), Maths.chunkInSpiral(6, xOffset, zOffset))
        assertEquals(ChunkPos.pack(-1 + xOffset, 0 + zOffset), Maths.chunkInSpiral(7, xOffset, zOffset))
        assertEquals(ChunkPos.pack(-1 + xOffset, -1 + zOffset), Maths.chunkInSpiral(8, xOffset, zOffset))
    }

    @Test
    fun `test lcm with known values`() {
        assertEquals(6, Maths.lcm(2, 3))
        assertEquals(6, Maths.lcm(3, 2))
        assertEquals(837, Maths.lcm(27, 93))
        assertEquals(72, Maths.lcm(24, 36))
        assertEquals(48, Maths.lcm(24, 48))
    }
}
