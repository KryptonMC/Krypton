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

import org.junit.jupiter.api.Test
import org.kryptonmc.api.util.Vec3d
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Vec3dTest {

    @Test
    fun `ensure values with parameters in opposing order compare equal`() {
        val pos1 = Vec3d(1.342, 2.938, 3.1543)
        val pos2 = Vec3d(3.1543, 2.938, 1.342)
        assertEquals(0, pos1.compareTo(pos2))
        assertEquals(0, pos2.compareTo(pos1))
    }

    @Test
    fun `ensure equal values compare equal`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(0, pos.compareTo(pos))
    }

    @Test
    fun `ensure position with values greater than other position compares greater`() {
        val pos1 = Vec3d(1.342, 2.938, 3.1543)
        val pos2 = Vec3d(4.662, 5.921, 6.122)
        assertEquals(-1, pos1.compareTo(pos2))
    }

    @Test
    fun `ensure position with values less than other position compares less`() {
        val pos1 = Vec3d(4.662, 5.921, 6.122)
        val pos2 = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(1, pos1.compareTo(pos2))
    }

    @Test
    fun `ensure add with zero inputs returns equal`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(pos, pos.add(0.0, 0.0, 0.0))
    }

    @Test
    fun `ensure subtract with zero inputs returns equal`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(pos, pos.subtract(0.0, 0.0, 0.0))
    }

    @Test
    fun `ensure add with actual inputs returns correct result`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(Vec3d(pos.x + 4.662, pos.y + 5.921, pos.z + 6.122), pos.add(4.662, 5.921, 6.122))
    }

    @Test
    fun `ensure subtract with actual inputs returns correct result`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(Vec3d(pos.x - 4.662, pos.y - 5.921, pos.z - 6.122), pos.subtract(4.662, 5.921, 6.122))
    }

    @Test
    fun `ensure multiply with 1 inputs returns equal`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(pos, pos.multiply(1.0, 1.0, 1.0))
    }

    @Test
    fun `ensure multiply single with 1 input returns equal`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(pos, pos.multiply(1.0))
    }

    @Test
    fun `ensure divide with 1 inputs returns equal`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(pos, pos.divide(1.0, 1.0, 1.0))
    }

    @Test
    fun `ensure divide single with 1 input returns equal`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(pos, pos.divide(1.0))
    }

    @Test
    fun `ensure multiply with actual inputs returns correct result`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(Vec3d(pos.x * 4.662, pos.y * 5.921, pos.z * 6.122), pos.multiply(4.662, 5.921, 6.122))
    }

    @Test
    fun `ensure multiply single with actual input returns correct result`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(Vec3d(pos.x * 5.342, pos.y * 5.342, pos.z * 5.342), pos.multiply(5.342))
    }

    @Test
    fun `ensure divide with actual inputs returns correct result`() {
        val pos = Vec3d(4.662, 5.921, 6.122)
        assertEquals(Vec3d(pos.x / 1.342, pos.y / 2.938, pos.z / 3.1543), pos.divide(1.342, 2.938, 3.1543))
    }

    @Test
    fun `ensure divide single with actual input returns correct result`() {
        val pos = Vec3d(4.662, 5.921, 6.122)
        assertEquals(Vec3d(pos.x / 2.938, pos.y / 2.938, pos.z / 2.938), pos.divide(2.938))
    }

    @Test
    fun `ensure dot product with simple values returns correct result`() {
        val pos1 = Vec3d(1.342, 2.938, 3.1543)
        val pos2 = Vec3d(4.662, 5.921, 6.122)
        assertEquals(pos1.x * pos2.x + pos1.y * pos2.y + pos1.z * pos2.z, pos1.dot(pos2))
    }

    @Test
    fun `ensure cross product with simple values returns correct result`() {
        val pos1 = Vec3d(1.342, 2.938, 3.1543)
        val pos2 = Vec3d(4.662, 5.921, 6.122)
        val result = Vec3d(pos1.y * pos2.z - pos1.z * pos2.y, pos1.z * pos2.x - pos1.x * pos2.z, pos1.x * pos2.y - pos1.y * pos2.x)
        assertEquals(result, pos1.cross(pos2))
    }

    @Test
    fun `ensure negation with simple values returns correct result`() {
        assertEquals(Vec3d(-1.342, -2.938, -3.1543), Vec3d(1.342, 2.938, 3.1543).negate())
    }

    @Test
    fun `ensure distance squared with simple values returns correct result`() {
        val pos1 = Vec3d(1.342, 2.938, 3.1543)
        val pos2 = Vec3d(4.662, 5.921, 6.122)
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        val dz = pos1.z - pos2.z
        assertEquals(dx * dx + dy * dy + dz * dz, pos1.distanceSquared(pos2.x, pos2.y, pos2.z))
    }

    @Test
    fun `ensure distance with simple values returns correct result`() {
        val pos1 = Vec3d(1.342, 2.938, 3.1543)
        val pos2 = Vec3d(4.662, 5.921, 6.122)
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        val dz = pos1.z - pos2.z
        val sqDistance = dx * dx + dy * dy + dz * dz
        assertEquals(sqrt(sqDistance), pos1.distance(pos2.x, pos2.y, pos2.z))
    }

    @Test
    fun `ensure length squared with simple values returns correct result`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(pos.x * pos.x + pos.y * pos.y + pos.z * pos.z, pos.lengthSquared())
    }

    @Test
    fun `ensure length with simple values returns correct result`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertEquals(sqrt(pos.x * pos.x + pos.y * pos.y + pos.z * pos.z), pos.length())
    }

    @Test
    fun `ensure normalization with simple values returns result with length 1`() {
        val pos = Vec3d(1.342, 2.938, 3.1543)
        assertTrue(abs(pos.normalize().length() - 1.0) < 1.0E-4)
    }
}
