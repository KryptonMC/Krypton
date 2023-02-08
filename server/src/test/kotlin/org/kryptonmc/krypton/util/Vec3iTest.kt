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

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.coordinate.BlockPos
import org.kryptonmc.krypton.testutil.Bootstrapping
import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Vec3iTest {

    @Test
    fun `ensure same positions compare equal`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(0, pos.compareTo(pos))
    }

    @Test
    fun `ensure positions with reversed field order compare equal`() {
        val pos1 = Vec3i(1, 2, 3)
        val pos2 = Vec3i(3, 2, 1)
        assertEquals(0, pos1.compareTo(pos2))
        assertEquals(0, pos2.compareTo(pos1))
    }

    @Test
    fun `ensure first position with fields all less than second position compares less`() {
        val pos1 = Vec3i(1, 2, 3)
        val pos2 = Vec3i(4, 5, 6)
        assertEquals(-1, pos1.compareTo(pos2))
    }

    @Test
    fun `ensure second position with fields all greater than first position compares greater`() {
        val pos1 = Vec3i(1, 2, 3)
        val pos2 = Vec3i(4, 5, 6)
        assertEquals(1, pos2.compareTo(pos1))
    }

    @Test
    fun `ensure integer add with zero inputs returns equal`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(pos, pos.add(0, 0, 0))
    }

    @Test
    fun `ensure double add with zero inputs returns equal`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(pos, pos.add(Vec3d.ZERO))
    }

    @Test
    fun `ensure subtract with zero inputs returns equal`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(pos, pos.subtract(0, 0, 0))
    }

    @Test
    fun `ensure add with values returns correct position`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(Vec3i(pos.x + 4, pos.y + 5, pos.z + 6), pos.add(4, 5, 6))
    }

    @Test
    fun `ensure subtract with values returns correct position`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(Vec3i(pos.x + 4, pos.y + 5, pos.z + 6), pos.add(4, 5, 6))
    }

    @Test
    fun `ensure multiply with one inputs returns equal`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(pos, pos.multiply(1, 1, 1))
    }

    @Test
    fun `ensure multiply single with one input returns equal`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(pos, pos.multiply(1, 1, 1))
    }

    @Test
    fun `ensure divide with one inputs returns equal`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(pos, pos.divide(1, 1, 1))
    }

    @Test
    fun `ensure divide single with one input returns equal`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(pos, pos.divide(1))
    }

    @Test
    fun `ensure multiply with values returns correct result`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(Vec3i(pos.x * 4, pos.y * 5, pos.z * 6), pos.multiply(4, 5, 6))
    }

    @Test
    fun `ensure multiply single with value returns correct result`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(Vec3i(pos.x * 5, pos.y * 5, pos.z * 5), pos.multiply(5))
    }

    @Test
    fun `ensure divide with values returns correct result`() {
        val pos = Vec3i(16, 25, 36)
        assertEquals(Vec3i(pos.x / 4, pos.y / 5, pos.z / 6), pos.divide(4, 5, 6))
    }

    @Test
    fun `ensure divide single with values returns correct result`() {
        val pos = Vec3i(25, 75, 175)
        assertEquals(Vec3i(pos.x / 5, pos.y / 5, pos.z / 5), pos.divide(5))
    }

    @Test
    fun `ensure distance squared returns correct value for simple input`() {
        val pos1 = Vec3i(1, 2, 3)
        val pos2 = Vec3i(4, 5, 6)
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        val dz = pos1.z - pos2.z
        assertEquals(dx * dx + dy * dy + dz * dz, pos1.distanceSquared(pos2.x, pos2.y, pos2.z))
    }

    @Test
    fun `ensure distance returns correct value for simple input`() {
        val pos1 = Vec3i(1, 2, 3)
        val pos2 = Vec3i(4, 5, 6)
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        val dz = pos1.z - pos2.z
        val sqDistance = dx * dx + dy * dy + dz * dz
        assertEquals(sqrt(sqDistance.toDouble()), pos1.distance(pos2.x, pos2.y, pos2.z))
    }

    @Test
    fun `ensure length squared returns correct value for simple input`() {
        val pos = Vec3i(1, 2, 3)
        assertEquals(pos.x * pos.x + pos.y * pos.y + pos.z * pos.z, pos.lengthSquared())
    }

    @Test
    fun `ensure length returns correct value for simple input`() {
        val pos = Vec3i(1, 2, 3)
        val sqLength = pos.x * pos.x + pos.y * pos.y + pos.z * pos.z
        assertEquals(sqrt(sqLength.toDouble()), pos.length())
    }

    @Test
    fun `ensure relative without distance returns correct value for all directions`() {
        val pos = Vec3i(1, 2, 3)
        Direction.values().forEach { assertEquals(pos.add(it.normal), pos.relative(it)) }
    }

    @Test
    fun `ensure between closed produces values only in given range`() {
        val range = BlockPos.betweenClosed(-2, -2, -2, 2, 2, 2)
        for (pos in range) {
            assertTrue(pos.x >= -2)
            assertTrue(pos.x <= 2)
            assertTrue(pos.y >= -2)
            assertTrue(pos.y <= 2)
            assertTrue(pos.z >= -2)
            assertTrue(pos.z <= 2)
        }
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `load factories`() {
            Bootstrapping.loadFactories()
        }
    }
}
