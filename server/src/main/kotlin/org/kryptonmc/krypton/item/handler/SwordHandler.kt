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
package org.kryptonmc.krypton.item.handler

import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.downcast
import org.kryptonmc.krypton.world.block.isPlant
import org.kryptonmc.krypton.world.block.isReplaceablePlant
import org.kryptonmc.krypton.world.block.isVegetable
import org.kryptonmc.krypton.world.block.KryptonBlock

object SwordHandler : ItemHandler {

    private val COBWEB_BLOCK_ID = Blocks.COBWEB.downcast().id
    private const val COBWEB_DESTROY_SPEED = 15F
    private const val PLANT_LEAVES_VEGETABLE_DESTROY_SPEED = 1.5F
    private const val DEFAULT_DESTROY_SPEED = 1F

    override fun canAttackBlock(player: KryptonPlayer, world: KryptonWorld, block: KryptonBlock, x: Int, y: Int, z: Int): Boolean =
        player.gameMode != GameMode.CREATIVE

    override fun destroySpeed(item: KryptonItemStack, block: KryptonBlock): Float {
        if (block.id == COBWEB_BLOCK_ID) return COBWEB_DESTROY_SPEED
        if (block.isPlant() || block.isReplaceablePlant() || BlockTags.LEAVES.contains(block) || block.isVegetable()) {
            return PLANT_LEAVES_VEGETABLE_DESTROY_SPEED
        }
        return DEFAULT_DESTROY_SPEED
    }

    override fun isCorrectTool(block: KryptonBlock): Boolean = block.id == COBWEB_BLOCK_ID

    override fun mineBlock(
        player: KryptonPlayer,
        item: KryptonItemStack,
        world: KryptonWorld,
        block: KryptonBlock,
        x: Int,
        y: Int,
        z: Int
    ): Boolean = true
}
