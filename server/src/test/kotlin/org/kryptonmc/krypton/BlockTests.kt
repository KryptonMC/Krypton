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

import net.kyori.adventure.key.Key
import org.junit.jupiter.api.BeforeAll
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockFace
import org.kryptonmc.krypton.util.Bootstrap
import org.kryptonmc.krypton.world.block.KryptonBlock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BlockTests {

    @Test
    fun `test face opposites`() {
        assertEquals(BlockFace.TOP, BlockFace.BOTTOM.opposite)
        assertEquals(BlockFace.BOTTOM, BlockFace.TOP.opposite)
        assertEquals(BlockFace.SOUTH, BlockFace.NORTH.opposite)
        assertEquals(BlockFace.NORTH, BlockFace.SOUTH.opposite)
        assertEquals(BlockFace.EAST, BlockFace.WEST.opposite)
        assertEquals(BlockFace.WEST, BlockFace.EAST.opposite)
    }

    @Test
    fun `test comparisons`() {
        val blockOne = KryptonBlock.Builder(Key.key("birch_wood"), 5, 78).build()
        val blockTwo = KryptonBlock.Builder(Key.key("acacia_door"), 8, 97).build()
        assertTrue(blockOne.compare(blockOne))
        assertFalse(blockOne.compare(blockTwo))
        assertTrue(blockOne.compare(blockOne, Block.Comparator.IDENTITY))
        assertFalse(blockOne.compare(blockTwo, Block.Comparator.IDENTITY))
        assertTrue(blockOne.compare(blockOne, Block.Comparator.ID))
        assertFalse(blockOne.compare(blockTwo, Block.Comparator.ID))
        assertTrue(blockOne.compare(blockOne, Block.Comparator.STATE))
        assertFalse(blockOne.compare(blockTwo, Block.Comparator.STATE))
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun bootstrap() {
            Bootstrap.preload()
        }
    }
}
