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
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.InteractionContext
import org.kryptonmc.api.item.ItemHandler
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.util.InteractionResult
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonUseItemResult
import org.spongepowered.math.vector.Vector3i

interface KryptonItemTimedHandler : ItemHandler {

    override fun canAttackBlock(player: Player, world: World, block: Block, position: Vector3i) = true

    override fun destroySpeed(item: ItemStack, block: Block) = 1F

    override fun use(
        player: Player,
        hand: Hand
    ) = KryptonUseItemResult(InteractionResult.PASS, player.inventory.heldItem(hand))

    fun finishUse(
        player: Player,
        hand: Hand
    ) = KryptonUseItemResult(InteractionResult.PASS, player.inventory.heldItem(hand))

    override fun interact(context: InteractionContext) = InteractionResult.PASS

    override fun isCorrectTool(block: Block) = false

    override fun mineBlock(player: Player, item: ItemStack, world: World, block: Block, position: Vector3i) = false
}
