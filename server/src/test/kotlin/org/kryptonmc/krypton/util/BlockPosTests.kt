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
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.testutil.Bootstrapping
import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class BlockPosTests {

    @Test
    fun `verify equals and hash code`() {
        EqualsVerifier.forClass(BlockPos::class.java).verify()
    }

    @Test
    fun `verify comparisons`() {
        val pos1a = BlockPos(1, 2, 3)
        val pos1b = BlockPos(3, 2, 1)
        assertEquals(0, pos1a.compareTo(pos1b))
        assertEquals(0, pos1b.compareTo(pos1a))
        assertEquals(0, pos1a.compareTo(pos1a))
        assertEquals(0, pos1b.compareTo(pos1b))
        val pos2 = BlockPos(4, 5, 6)
        assertEquals(-1, pos1a.compareTo(pos2))
        assertEquals(-1, pos1b.compareTo(pos2))
        assertEquals(1, pos2.compareTo(pos1a))
        assertEquals(1, pos2.compareTo(pos1b))
    }

    @Test
    fun `test add and subtract with zero returns same`() {
        val pos = BlockPos(1, 2, 3)
        assertSame(pos, pos.add(0, 0, 0))
        assertSame(pos, pos.add(0.0, 0.0, 0.0))
        assertSame(pos, pos.subtract(0, 0, 0))
    }

    @Test
    fun `test add and subtract`() {
        val pos = BlockPos(1, 2, 3)
        val x = 4
        val y = 5
        val z = 6
        assertEquals(BlockPos(pos.x + x, pos.y + y, pos.z + z), pos.add(x, y, z))
        assertEquals(BlockPos(pos.x - x, pos.y - y, pos.z - z), pos.subtract(x, y, z))
    }

    @Test
    fun `test multiply and divide with one returns same`() {
        val pos = BlockPos(1, 2, 3)
        assertSame(pos, pos.multiply(1, 1, 1))
        assertSame(pos, pos.multiply(1))
        assertSame(pos, pos.divide(1, 1, 1))
        assertSame(pos, pos.divide(1))
    }

    @Test
    fun `test multiply and divide`() {
        val pos = BlockPos(1, 2, 3)
        val x = 4
        val y = 5
        val z = 6
        assertEquals(BlockPos(pos.x * x, pos.y * y, pos.z * z), pos.multiply(x, y, z))
        assertEquals(BlockPos(pos.x * 5, pos.y * 5, pos.z * 5), pos.multiply(5))
        assertEquals(BlockPos(pos.x / x, pos.y / y, pos.z / z), pos.divide(x, y, z))
        assertEquals(BlockPos(pos.x / 5, pos.y / 5, pos.z / 5), pos.divide(5))
    }

    @Test
    fun `test dot product`() {
        val pos = BlockPos(1, 2, 3)
        val x = 4
        val y = 5
        val z = 6
        assertEquals(pos.x * x + pos.y * y + pos.z * z, pos.dot(x, y, z))
    }

    @Test
    fun `test cross product`() {
        val pos = BlockPos(1, 2, 3)
        val x = 4
        val y = 5
        val z = 6
        assertEquals(BlockPos(pos.y * z - pos.z * y, pos.z * x - pos.x * z, pos.x * y - pos.y * x), pos.cross(x, y, z))
    }

    @Test
    fun `test negation`() {
        val x = 1
        val y = 2
        val z = 3
        assertEquals(BlockPos(-x, -y, -z), BlockPos(x, y, z).negate())
    }

    @Test
    fun `test distance and distance squared`() {
        val pos1 = BlockPos(1, 2, 3)
        val pos2 = BlockPos(4, 5, 6)
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        val dz = pos1.z - pos2.z
        val sqDistance = dx * dx + dy * dy + dz * dz
        assertEquals(sqDistance, pos1.distanceSquared(pos2.x, pos2.y, pos2.z))
        assertEquals(sqrt(sqDistance.toDouble()).toFloat(), pos1.distance(pos2.x, pos2.y, pos2.z))
    }

    @Test
    fun `test length and length squared`() {
        val pos = BlockPos(1, 2, 3)
        val sqLength = pos.x * pos.x + pos.y * pos.y + pos.z * pos.z
        assertEquals(sqLength, pos.lengthSquared())
        assertEquals(sqrt(sqLength.toDouble()).toFloat(), pos.length())
    }

    @Test
    fun `test relative positions with zero distance produce same as input`() {
        val pos = BlockPos(1, 2, 3)
        assertSame(pos, pos.above(0))
        assertSame(pos, pos.below(0))
        assertSame(pos, pos.north(0))
        assertSame(pos, pos.south(0))
        assertSame(pos, pos.west(0))
        assertSame(pos, pos.east(0))
        Direction.values().forEach { assertSame(pos, pos.relative(it, 0)) }
        Direction.Axis.values().forEach { assertSame(pos, pos.relative(it, 0)) }
    }

    @Test
    fun `test relative positions without distance`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x, pos.y + 1, pos.z), pos.above())
        assertEquals(BlockPos(pos.x, pos.y - 1, pos.z), pos.below())
        assertEquals(BlockPos(pos.x, pos.y, pos.z - 1), pos.north())
        assertEquals(BlockPos(pos.x, pos.y, pos.z + 1), pos.south())
        assertEquals(BlockPos(pos.x - 1, pos.y, pos.z), pos.west())
        assertEquals(BlockPos(pos.x + 1, pos.y, pos.z), pos.east())
        Direction.values().forEach { assertEquals(BlockPos(pos.x + it.normalX, pos.y + it.normalY, pos.z + it.normalZ), pos.relative(it)) }
    }

    @Test
    fun `test relative positions with distance`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x, pos.y + 10, pos.z), pos.above(10))
        assertEquals(BlockPos(pos.x, pos.y - 10, pos.z), pos.below(10))
        assertEquals(BlockPos(pos.x, pos.y, pos.z - 10), pos.north(10))
        assertEquals(BlockPos(pos.x, pos.y, pos.z + 10), pos.south(10))
        assertEquals(BlockPos(pos.x - 10, pos.y, pos.z), pos.west(10))
        assertEquals(BlockPos(pos.x + 10, pos.y, pos.z), pos.east(10))
        Direction.values().forEach {
            assertEquals(BlockPos(pos.x + it.normalX * 10, pos.y + it.normalY * 10, pos.z + it.normalZ * 10), pos.relative(it, 10))
        }
    }

    @Test
    fun `test between closed produces values only in given range`() {
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
