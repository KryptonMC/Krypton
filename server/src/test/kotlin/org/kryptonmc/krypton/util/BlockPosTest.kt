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
import org.kryptonmc.krypton.coordinate.BlockPos
import org.kryptonmc.krypton.testutil.Bootstrapping
import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BlockPosTest {

    @Test
    fun `verify equals and hash code`() {
        EqualsVerifier.forClass(BlockPos::class.java).verify()
    }

    @Test
    fun `ensure same positions compare equal`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(0, pos.compareTo(pos))
    }

    @Test
    fun `ensure positions with reversed field order compare equal`() {
        val pos1 = BlockPos(1, 2, 3)
        val pos2 = BlockPos(3, 2, 1)
        assertEquals(0, pos1.compareTo(pos2))
        assertEquals(0, pos2.compareTo(pos1))
    }

    @Test
    fun `ensure first position with fields all less than second position compares less`() {
        val pos1 = BlockPos(1, 2, 3)
        val pos2 = BlockPos(4, 5, 6)
        assertEquals(-1, pos1.compareTo(pos2))
    }

    @Test
    fun `ensure second position with fields all greater than first position compares greater`() {
        val pos1 = BlockPos(1, 2, 3)
        val pos2 = BlockPos(4, 5, 6)
        assertEquals(1, pos2.compareTo(pos1))
    }

    @Test
    fun `ensure integer add with zero inputs returns equal`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.add(0, 0, 0))
    }

    @Test
    fun `ensure double add with zero inputs returns equal`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.add(0.0, 0.0, 0.0))
    }

    @Test
    fun `ensure subtract with zero inputs returns equal`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.subtract(0, 0, 0))
    }

    @Test
    fun `ensure add with values returns correct position`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x + 4, pos.y + 5, pos.z + 6), pos.add(4, 5, 6))
    }

    @Test
    fun `ensure subtract with values returns correct position`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x + 4, pos.y + 5, pos.z + 6), pos.add(4, 5, 6))
    }

    @Test
    fun `ensure multiply with one inputs returns equal`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.multiply(1, 1, 1))
    }

    @Test
    fun `ensure multiply single with one input returns equal`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.multiply(1, 1, 1))
    }

    @Test
    fun `ensure divide with one inputs returns equal`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.divide(1, 1, 1))
    }

    @Test
    fun `ensure divide single with one input returns equal`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.divide(1))
    }

    @Test
    fun `ensure multiply with values returns correct result`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x * 4, pos.y * 5, pos.z * 6), pos.multiply(4, 5, 6))
    }

    @Test
    fun `ensure multiply single with value returns correct result`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x * 5, pos.y * 5, pos.z * 5), pos.multiply(5))
    }

    @Test
    fun `ensure divide with values returns correct result`() {
        val pos = BlockPos(16, 25, 36)
        assertEquals(BlockPos(pos.x / 4, pos.y / 5, pos.z / 6), pos.divide(4, 5, 6))
    }

    @Test
    fun `ensure divide single with values returns correct result`() {
        val pos = BlockPos(25, 75, 175)
        assertEquals(BlockPos(pos.x / 5, pos.y / 5, pos.z / 5), pos.divide(5))
    }

    @Test
    fun `ensure dot product returns correct value for simple input`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos.x * 4 + pos.y * 5 + pos.z * 6, pos.dot(4, 5, 6))
    }

    @Test
    fun `ensure cross product returns correct value for simple input`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.y * 6 - pos.z * 5, pos.z * 4 - pos.x * 6, pos.x * 5 - pos.y * 4), pos.cross(4, 5, 6))
    }

    @Test
    fun `ensure negation returns correct output for simple input`() {
        assertEquals(BlockPos(-1, -2, -3), BlockPos(1, 2, 3).negate())
    }

    @Test
    fun `ensure distance squared returns correct value for simple input`() {
        val pos1 = BlockPos(1, 2, 3)
        val pos2 = BlockPos(4, 5, 6)
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        val dz = pos1.z - pos2.z
        assertEquals(dx * dx + dy * dy + dz * dz, pos1.distanceSquared(pos2.x, pos2.y, pos2.z))
    }

    @Test
    fun `ensure distance returns correct value for simple input`() {
        val pos1 = BlockPos(1, 2, 3)
        val pos2 = BlockPos(4, 5, 6)
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        val dz = pos1.z - pos2.z
        val sqDistance = dx * dx + dy * dy + dz * dz
        assertEquals(sqrt(sqDistance.toDouble()).toFloat(), pos1.distance(pos2.x, pos2.y, pos2.z))
    }

    @Test
    fun `ensure length squared returns correct value for simple input`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos.x * pos.x + pos.y * pos.y + pos.z * pos.z, pos.lengthSquared())
    }

    @Test
    fun `ensure length returns correct value for simple input`() {
        val pos = BlockPos(1, 2, 3)
        val sqLength = pos.x * pos.x + pos.y * pos.y + pos.z * pos.z
        assertEquals(sqrt(sqLength.toDouble()).toFloat(), pos.length())
    }

    @Test
    fun `ensure above returns equal value for zero distance`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.above(0))
    }

    @Test
    fun `ensure below returns equal value for zero distance`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.below(0))
    }

    @Test
    fun `ensure north returns equal value for zero distance`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.north(0))
    }

    @Test
    fun `ensure south returns equal value for zero distance`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.south(0))
    }

    @Test
    fun `ensure west returns equal value for zero distance`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.west(0))
    }

    @Test
    fun `ensure east returns equal value for zero distance`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(pos, pos.east(0))
    }

    @Test
    fun `ensure relative returns equal value for zero distance in all directions`() {
        val pos = BlockPos(1, 2, 3)
        Direction.values().forEach { assertEquals(pos, pos.relative(it, 0)) }
    }

    @Test
    fun `ensure relative returns equal value for zero distance in all axes`() {
        val pos = BlockPos(1, 2, 3)
        Direction.Axis.values().forEach { assertEquals(pos, pos.relative(it, 0)) }
    }

    @Test
    fun `ensure above without distance increases y by one`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x, pos.y + 1, pos.z), pos.above())
    }

    @Test
    fun `ensure below without distance decreases y by one`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x, pos.y - 1, pos.z), pos.below())
    }

    @Test
    fun `ensure north without distance decreases z by one`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x, pos.y, pos.z - 1), pos.north())
    }

    @Test
    fun `ensure south without distance increases z by one`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x, pos.y, pos.z + 1), pos.south())
    }

    @Test
    fun `ensure west without distance increases y by one`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x - 1, pos.y, pos.z), pos.west())
    }

    @Test
    fun `ensure east without distance increases y by one`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x + 1, pos.y, pos.z), pos.east())
    }

    @Test
    fun `ensure relative without distance returns correct value for all directions`() {
        val pos = BlockPos(1, 2, 3)
        Direction.values().forEach { assertEquals(BlockPos(pos.x + it.normalX, pos.y + it.normalY, pos.z + it.normalZ), pos.relative(it)) }
    }

    @Test
    fun `ensure above with 10 distance increases y by 10`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x, pos.y + 10, pos.z), pos.above(10))
    }

    @Test
    fun `ensure below with 10 distance decreases y by 10`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x, pos.y - 10, pos.z), pos.below(10))
    }

    @Test
    fun `ensure north with 10 distance decreases z by 10`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x, pos.y, pos.z - 10), pos.north(10))
    }

    @Test
    fun `ensure south with 10 distance increases z by 10`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x, pos.y, pos.z + 10), pos.south(10))
    }

    @Test
    fun `ensure west with 10 distance increases y by 10`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x - 10, pos.y, pos.z), pos.west(10))
    }

    @Test
    fun `ensure east with 10 distance increases y by 10`() {
        val pos = BlockPos(1, 2, 3)
        assertEquals(BlockPos(pos.x + 10, pos.y, pos.z), pos.east(10))
    }

    @Test
    fun `ensure relative with 10 distance produces correct value for all directions`() {
        val pos = BlockPos(1, 2, 3)
        Direction.values().forEach {
            assertEquals(BlockPos(pos.x + it.normalX * 10, pos.y + it.normalY * 10, pos.z + it.normalZ * 10), pos.relative(it, 10))
        }
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
