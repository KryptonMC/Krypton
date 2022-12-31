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

import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.coordinate.BlockPos
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.material.Materials

object SwordHandler : ItemHandler {

    private const val COBWEB_DESTROY_SPEED = 15F
    private const val PLANT_LEAVES_VEGETABLE_DESTROY_SPEED = 1.5F
    private const val DEFAULT_DESTROY_SPEED = 1F

    override fun canAttackBlock(player: KryptonPlayer, world: KryptonWorld, block: KryptonBlockState, pos: BlockPos): Boolean =
        player.gameMode != GameMode.CREATIVE

    override fun destroySpeed(item: KryptonItemStack, block: KryptonBlockState): Float {
        if (block.eq(KryptonBlocks.COBWEB)) return COBWEB_DESTROY_SPEED
        val material = block.material
        val isNotLeaves = !block.eq(BlockTags.LEAVES)
        if (material != Materials.PLANT && material != Materials.REPLACEABLE_PLANT && isNotLeaves && material != Materials.VEGETABLE) {
            return DEFAULT_DESTROY_SPEED
        }
        return PLANT_LEAVES_VEGETABLE_DESTROY_SPEED
    }

    override fun isCorrectTool(block: KryptonBlockState): Boolean = block.eq(KryptonBlocks.COBWEB)

    override fun mineBlock(player: KryptonPlayer, item: KryptonItemStack, world: KryptonWorld, block: KryptonBlockState,
                           pos: BlockPos
    ): Boolean {
        return true
    }
}
