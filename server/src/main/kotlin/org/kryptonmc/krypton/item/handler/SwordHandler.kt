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

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.isPlant
import org.kryptonmc.krypton.world.block.isReplaceablePlant
import org.kryptonmc.krypton.world.block.isVegetable

object SwordHandler : ItemHandler {

    override fun canAttackBlock(
        player: KryptonPlayer,
        world: KryptonWorld,
        block: Block,
        x: Int,
        y: Int,
        z: Int
    ): Boolean = player.gameMode != GameMode.CREATIVE

    override fun destroySpeed(item: KryptonItemStack, block: Block): Float {
        if (block.id == Blocks.COBWEB.id) return 15F
        if (block.isPlant() || block.isReplaceablePlant() || BlockTags.LEAVES.contains(block) || block.isVegetable()) {
            return 1.5F
        }
        return 1F
    }

    override fun isCorrectTool(block: Block): Boolean = block.id == Blocks.COBWEB.id

    override fun mineBlock(player: KryptonPlayer, item: KryptonItemStack, world: KryptonWorld, block: Block, x: Int, y: Int, z: Int): Boolean = true
}
