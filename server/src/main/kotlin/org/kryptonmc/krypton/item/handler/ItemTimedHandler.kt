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
import org.kryptonmc.krypton.item.InteractionContext
import org.kryptonmc.krypton.item.UseItemResult
import org.kryptonmc.api.util.InteractionResult
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3i

interface ItemTimedHandler : ItemHandler {

    override fun canAttackBlock(player: KryptonPlayer, world: KryptonWorld, block: Block, position: Vector3i): Boolean = true

    override fun destroySpeed(item: KryptonItemStack, block: Block): Float = 1F

    override fun use(
        player: KryptonPlayer,
        hand: Hand
    ): UseItemResult = UseItemResult(InteractionResult.PASS, player.inventory.heldItem(hand))

    fun finishUse(
        player: KryptonPlayer,
        hand: Hand
    ): UseItemResult = UseItemResult(InteractionResult.PASS, player.inventory.heldItem(hand))

    override fun interact(context: InteractionContext): InteractionResult = InteractionResult.PASS

    override fun isCorrectTool(block: Block): Boolean = false

    override fun mineBlock(player: KryptonPlayer, item: KryptonItemStack, world: KryptonWorld, block: Block, position: Vector3i): Boolean = false
}
