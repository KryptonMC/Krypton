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
package org.kryptonmc.krypton.util

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.util.random.RandomSource
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MathsTest {

    @Test
    fun `ensure powers of two do not round up`() {
        for (i in 0 until 31) { // There are 31 signed integer powers of two (that are non-negative)
            assertEquals(1 shl i, Maths.roundUpPow2(1 shl i))
        }
    }

    @Test
    fun `ensure one above powers of two round up correctly`() {
        for (i in 0 until 31) {
            // We use / 2 + 1 because it always gives us a value that is 1 above a power of two, which, if rounded up successfully, should imply
            // that all other values will round up properly.
            assertEquals(1 shl i, Maths.roundUpPow2((1 shl i) / 2 + 1))
        }
    }

    @Test
    fun `ensure powers of two return true for is power of two`() {
        for (i in 0 until 31) {
            assertTrue(Maths.isPowerOfTwo(1 shl i))
        }
    }

    @Test
    fun `ensure non powers of two return false for is power of two`() {
        for (i in 2 until 31) {
            assertFalse(Maths.isPowerOfTwo((1 shl i) / 2 + 1))
        }
    }

    @Test
    fun `ensure integer value smaller than minimum is clamped to minimum`() {
        assertEquals(-10, Maths.clamp(-20, -10, 10))
    }

    @Test
    fun `ensure integer value larger than maximum is clamped to maximum`() {
        assertEquals(10, Maths.clamp(20, -10, 10))
    }

    @Test
    fun `ensure integer value one below minimum is clamped to minimum`() {
        assertEquals(-10, Maths.clamp(-11, -10, 10))
    }

    @Test
    fun `ensure integer value one above maximum is clamped to maximum`() {
        assertEquals(10, Maths.clamp(11, -10, 10))
    }

    @Test
    fun `ensure integer value in between minimum and maximum is not clamped`() {
        assertEquals(-5, Maths.clamp(-5, -10, 10))
    }

    @Test
    fun `ensure double value smaller than minimum is clamped to minimum`() {
        assertEquals(-1.0, Maths.clamp(-2.0, -1.0, 1.0))
    }

    @Test
    fun `ensure double value larger than minimum is clamped to minimum`() {
        assertEquals(1.0, Maths.clamp(2.0, -1.0, 1.0))
    }

    @Test
    fun `ensure double value smallest unit below minimum is clamped to minimum`() {
        assertEquals(-1.0, Maths.clamp(-1.000000000000001, -1.0, 1.0))
    }

    @Test
    fun `ensure double value smallest unit above maximum is clamped to maximum`() {
        assertEquals(1.0, Maths.clamp(1.000000000000001, -1.0, 1.0))
    }

    @Test
    fun `ensure double value in between minimum and maximum is not clamped`() {
        assertEquals(0.5, Maths.clamp(0.5, 0.0, 1.0))
    }

    @Test
    fun `ensure float value smaller than minimum is clamped to minimum`() {
        assertEquals(-1F, Maths.clamp(-2F, -1F, 1F))
    }

    @Test
    fun `ensure float value larger than minimum is clamped to minimum`() {
        assertEquals(1F, Maths.clamp(2F, -1F, 1F))
    }

    @Test
    fun `ensure float value smallest unit below minimum is clamped to minimum`() {
        assertEquals(-1F, Maths.clamp(-1.0000001F, -1F, 1F))
    }

    @Test
    fun `ensure float value smallest unit above maximum is clamped to maximum`() {
        assertEquals(1F, Maths.clamp(1.0000001F, -1F, 1F))
    }

    @Test
    fun `ensure float value in between minimum and maximum is not clamped`() {
        assertEquals(0.5F, Maths.clamp(0.5F, 0F, 1F))
    }

    @Test
    fun `ensure next random integer returns low when low greater than high`() {
        // We make sure that any calls always return a predictable value, so the random generator can't just generate the boundary values
        // and pass the tests.
        val random = mockk<RandomSource> { every { nextInt(any()) } returns 0 }
        assertEquals(10, Maths.nextInt(random, 10, 5))
        assertEquals(10, Maths.nextInt(random, 10, 10))
    }

    @Test
    fun `ensure next random float returns low when low greater than high`() {
        // We make sure that any calls always return a predictable value, so the random generator can't just generate the boundary values
        // and pass the tests.
        val random = mockk<RandomSource> { every { nextFloat() } returns 0F }
        assertEquals(1F, Maths.nextFloat(random, 1F, 0.5F))
        assertEquals(1F, Maths.nextFloat(random, 1F, 1F))
    }

    @Test
    fun `ensure ceil rounds up correctly for value closest to lower bound`() {
        assertEquals(10, Maths.ceil(9.00000000000001))
    }

    @Test
    fun `ensure ceil rounds up correctly for value closest to upper bound`() {
        assertEquals(10, Maths.ceil(9.99999999999999))
    }

    @Test
    fun `ensure ceil does not round up whole value`() {
        assertEquals(10, Maths.ceil(10.0))
    }

    @Test
    fun `ensure ceil rounds up for negative value closest to lower bound`() {
        assertEquals(-9, Maths.ceil(-9.00000000000001))
    }

    @Test
    fun `ensure ceil rounds up for negative value closest to upper bound`() {
        assertEquals(-9, Maths.ceil(-9.99999999999999))
    }

    @Test
    fun `ensure ceil does not round up negative whole value`() {
        assertEquals(-9, Maths.ceil(-9.0))
    }

    @Test
    fun `ensure floor rounds down for float value closest to lower bound`() {
        assertEquals(9, Maths.floor(9.000001F))
    }

    @Test
    fun `ensure floor rounds down for float value closest to upper bound`() {
        assertEquals(9, Maths.floor(9.999999F))
    }

    @Test
    fun `ensure floor does not round down whole float value`() {
        assertEquals(9, Maths.floor(9F))
    }

    @Test
    fun `ensure floor rounds down for negative float value closest to lower bound`() {
        assertEquals(-10, Maths.floor(-9.999999F))
    }

    @Test
    fun `ensure floor rounds down for negative float value closest to upper bound`() {
        assertEquals(-10, Maths.floor(-9.000001F))
    }

    @Test
    fun `ensure floor does not round down negative whole float value`() {
        assertEquals(-10, Maths.floor(-10F))
    }

    @Test
    fun `ensure floor rounds down for double value closest to lower bound`() {
        assertEquals(9, Maths.floor(9.00000000000001))
    }

    @Test
    fun `ensure floor rounds down for double value closest to upper bound`() {
        assertEquals(9, Maths.floor(9.99999999999999))
    }

    @Test
    fun `ensure floor does not round down double whole value`() {
        assertEquals(9, Maths.floor(9.0))
    }

    @Test
    fun `ensure floor rounds down for negative double value closest to lower bound`() {
        assertEquals(-10, Maths.floor(-9.99999999999999))
    }

    @Test
    fun `ensure floor rounds down for negative double value closest to upper bound`() {
        assertEquals(-10, Maths.floor(-9.00000000000001))
    }

    @Test
    fun `ensure floor does not round down negative double whole value`() {
        assertEquals(-10, Maths.floor(-10.0))
    }

    @Test
    fun `ensure long floor rounds down for double value closest to lower bound`() {
        assertEquals(9L, Maths.lfloor(9.00000000000001))
    }

    @Test
    fun `ensure long floor rounds down for double value closest to upper bound`() {
        assertEquals(9L, Maths.lfloor(9.99999999999999))
    }

    @Test
    fun `ensure long floor does not round down double whole value`() {
        assertEquals(9L, Maths.lfloor(9.0))
    }

    @Test
    fun `ensure long floor rounds down for negative double value closest to lower bound`() {
        assertEquals(-10L, Maths.lfloor(-9.99999999999999))
    }

    @Test
    fun `ensure long floor rounds down for negative double value closest to upper bound`() {
        assertEquals(-10L, Maths.lfloor(-9.00000000000001))
    }

    @Test
    fun `ensure long floor does not round down negative double whole value`() {
        assertEquals(-10L, Maths.lfloor(-10.0))
    }

    @Test
    fun `ensure ceil log 2 returns 5 for known input 32`() {
        assertEquals(5, Maths.ceillog2(32))
    }

    @Test
    fun `ensure ceil log 2 returns 6 for known input 35`() {
        assertEquals(6, Maths.ceillog2(35))
    }

    @Test
    fun `ensure ceil log 2 returns 25 for production input 30000000`() {
        assertEquals(25, Maths.ceillog2(30000000))
    }

    @Test
    fun `ensure lcm returns 6 for known inputs 2 and 3`() {
        assertEquals(6, Maths.lcm(2, 3))
    }

    @Test
    fun `ensure lcm returns 6 for known inputs 2 and 3 opposite way around`() {
        assertEquals(6, Maths.lcm(3, 2))
    }

    @Test
    fun `ensure lcm returns correct value for inputs with no common factors`() {
        assertEquals(837, Maths.lcm(27, 93))
    }

    @Test
    fun `ensure lcm returns correct value for inputs with common factors`() {
        assertEquals(72, Maths.lcm(24, 36))
    }
}
