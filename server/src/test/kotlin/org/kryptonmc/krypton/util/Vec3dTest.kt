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
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.coordinate.KryptonVec3d
import org.kryptonmc.krypton.testutil.Bootstrapping
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Vec3dTest {

    @Test
    fun `verify equals and hash code`() {
        EqualsVerifier.forClass(KryptonVec3d::class.java).verify()
    }

    @Test
    fun `ensure values with parameters in opposing order compare equal`() {
        val pos1 = KryptonVec3d(1.342, 2.938, 3.1543)
        val pos2 = KryptonVec3d(3.1543, 2.938, 1.342)
        assertEquals(0, pos1.compareTo(pos2))
        assertEquals(0, pos2.compareTo(pos1))
    }

    @Test
    fun `ensure equal values compare equal`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(0, pos.compareTo(pos))
    }

    @Test
    fun `ensure position with values greater than other position compares greater`() {
        val pos1 = KryptonVec3d(1.342, 2.938, 3.1543)
        val pos2 = KryptonVec3d(4.662, 5.921, 6.122)
        assertEquals(-1, pos1.compareTo(pos2))
    }

    @Test
    fun `ensure position with values less than other position compares less`() {
        val pos1 = KryptonVec3d(4.662, 5.921, 6.122)
        val pos2 = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(1, pos1.compareTo(pos2))
    }

    @Test
    fun `ensure add with zero inputs returns equal`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(pos, pos.add(0.0, 0.0, 0.0))
    }

    @Test
    fun `ensure subtract with zero inputs returns equal`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(pos, pos.subtract(0.0, 0.0, 0.0))
    }

    @Test
    fun `ensure add with actual inputs returns correct result`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(KryptonVec3d(pos.x + 4.662, pos.y + 5.921, pos.z + 6.122), pos.add(4.662, 5.921, 6.122))
    }

    @Test
    fun `ensure subtract with actual inputs returns correct result`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(KryptonVec3d(pos.x - 4.662, pos.y - 5.921, pos.z - 6.122), pos.subtract(4.662, 5.921, 6.122))
    }

    @Test
    fun `ensure multiply with 1 inputs returns equal`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(pos, pos.multiply(1.0, 1.0, 1.0))
    }

    @Test
    fun `ensure multiply single with 1 input returns equal`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(pos, pos.multiply(1.0))
    }

    @Test
    fun `ensure divide with 1 inputs returns equal`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(pos, pos.divide(1.0, 1.0, 1.0))
    }

    @Test
    fun `ensure divide single with 1 input returns equal`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(pos, pos.divide(1.0))
    }

    @Test
    fun `ensure multiply with actual inputs returns correct result`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(KryptonVec3d(pos.x * 4.662, pos.y * 5.921, pos.z * 6.122), pos.multiply(4.662, 5.921, 6.122))
    }

    @Test
    fun `ensure multiply single with actual input returns correct result`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(KryptonVec3d(pos.x * 5.342, pos.y * 5.342, pos.z * 5.342), pos.multiply(5.342))
    }

    @Test
    fun `ensure divide with actual inputs returns correct result`() {
        val pos = KryptonVec3d(4.662, 5.921, 6.122)
        assertEquals(KryptonVec3d(pos.x / 1.342, pos.y / 2.938, pos.z / 3.1543), pos.divide(1.342, 2.938, 3.1543))
    }

    @Test
    fun `ensure divide single with actual input returns correct result`() {
        val pos = KryptonVec3d(4.662, 5.921, 6.122)
        assertEquals(KryptonVec3d(pos.x / 2.938, pos.y / 2.938, pos.z / 2.938), pos.divide(2.938))
    }

    @Test
    fun `ensure dot product with simple values returns correct result`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(pos.x * 4.662 + pos.y * 5.921 + pos.z * 6.122, pos.dot(4.662, 5.921, 6.122))
    }

    @Test
    fun `ensure cross product with simple values returns correct result`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        val result = KryptonVec3d(pos.y * 6.122 - pos.z * 5.921, pos.z * 4.662 - pos.x * 6.122, pos.x * 5.921 - pos.y * 4.662)
        assertEquals(result, pos.cross(4.662, 5.921, 6.122))
    }

    @Test
    fun `ensure negation with simple values returns correct result`() {
        assertEquals(KryptonVec3d(-1.342, -2.938, -3.1543), KryptonVec3d(1.342, 2.938, 3.1543).negate())
    }

    @Test
    fun `ensure distance squared with simple values returns correct result`() {
        val pos1 = KryptonVec3d(1.342, 2.938, 3.1543)
        val pos2 = KryptonVec3d(4.662, 5.921, 6.122)
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        val dz = pos1.z - pos2.z
        assertEquals(dx * dx + dy * dy + dz * dz, pos1.distanceSquared(pos2.x, pos2.y, pos2.z))
    }

    @Test
    fun `ensure distance with simple values returns correct result`() {
        val pos1 = KryptonVec3d(1.342, 2.938, 3.1543)
        val pos2 = KryptonVec3d(4.662, 5.921, 6.122)
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        val dz = pos1.z - pos2.z
        val sqDistance = dx * dx + dy * dy + dz * dz
        assertEquals(sqrt(sqDistance), pos1.distance(pos2.x, pos2.y, pos2.z))
    }

    @Test
    fun `ensure length squared with simple values returns correct result`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(pos.x * pos.x + pos.y * pos.y + pos.z * pos.z, pos.lengthSquared())
    }

    @Test
    fun `ensure length with simple values returns correct result`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(sqrt(pos.x * pos.x + pos.y * pos.y + pos.z * pos.z), pos.length())
    }

    @Test
    fun `ensure normalization with simple values returns result with length 1`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertTrue(abs(pos.normalize().length() - 1.0) < 1.0E-4)
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `load factories`() {
            Bootstrapping.loadFactories()
        }
    }
}
