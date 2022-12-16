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

import org.kryptonmc.api.entity.Hand
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.UseItemResult
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.state.KryptonBlockState

/**
 * A handler for a type of item.
 *
 * This helps promote sharing, as these can be reused between multiple item
 * type that share properties.
 */
interface ItemHandler {

    /**
     * Gets the destroy speed of the given [block] when destroyed with the
     * given [item].
     */
    fun destroySpeed(item: KryptonItemStack, block: KryptonBlockState): Float = 1F

    /**
     * Checks if this item type is the correct tool to break the given [block].
     */
    fun isCorrectTool(block: KryptonBlockState): Boolean = true

    /**
     * Checks if the given [player] can attack the given [block] at the given
     * [pos] in the given [world].
     */
    fun canAttackBlock(player: KryptonPlayer, world: KryptonWorld, block: KryptonBlockState, pos: BlockPos): Boolean = true

    fun interactEntity(item: KryptonItemStack, player: KryptonPlayer, entity: KryptonLivingEntity, hand: Hand): InteractionResult =
        InteractionResult.PASS

    /**
     * Called when the given [player] uses the item they are holding in the
     * given [hand].
     */
    fun use(player: KryptonPlayer, hand: Hand): UseItemResult = UseItemResult(InteractionResult.PASS, player.inventory.getHeldItem(hand))

    /**
     * Called when the given [player] finishes destroying the given [block] at
     * the given [pos] in the given [world], using the given [item] to
     * destroy it.
     */
    fun mineBlock(player: KryptonPlayer, item: KryptonItemStack, world: KryptonWorld, block: KryptonBlockState, pos: BlockPos): Boolean = false
}
