/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.tags.BlockTags
import org.kryptonmc.krypton.world.block.isPlant
import org.kryptonmc.krypton.world.block.isReplaceablePlant
import org.kryptonmc.krypton.world.block.isVegetable
import org.spongepowered.math.vector.Vector3i

sealed class SwordHandler(type: ItemType) : KryptonItemHandler(type) {

    override fun canAttackBlock(player: Player, world: World, block: Block, position: Vector3i) = player.gamemode != Gamemode.CREATIVE

    override fun getDestroySpeed(item: ItemStack, block: Block): Float {
        if (block.id == Blocks.COBWEB.id) return 15F
        if (block.isPlant() || block.isReplaceablePlant() || BlockTags.LEAVES.contains(block) || block.isVegetable()) return 1.5F
        return 1F
    }

    override fun isCorrectTool(block: Block) = block.id == Blocks.COBWEB.id

    override fun mineBlock(player: Player, item: ItemStack, world: World, block: Block, position: Vector3i) = true
}

object WoodenSwordHandler : SwordHandler(ItemTypes.WOODEN_SWORD)

object StoneSwordHandler : SwordHandler(ItemTypes.STONE_SWORD)

object GoldenSwordHandler : SwordHandler(ItemTypes.GOLDEN_SWORD)

object IronSwordHandler : SwordHandler(ItemTypes.IRON_SWORD)

object DiamondSwordHandler : SwordHandler(ItemTypes.DIAMOND_SWORD)

object NetheriteSwordHandler : SwordHandler(ItemTypes.NETHERITE_SWORD)
