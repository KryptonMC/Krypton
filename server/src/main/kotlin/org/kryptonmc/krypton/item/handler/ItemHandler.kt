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
import org.kryptonmc.api.util.InteractionResult
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.InteractionContext
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.UseItemResult
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3i

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
    fun destroySpeed(item: KryptonItemStack, block: Block): Float = 1F

    /**
     * Returns true if this item type is the correct tool to break the
     * given [block], false otherwise.
     */
    fun isCorrectTool(block: Block): Boolean = true

    /**
     * Returns true if the given [player] can attack the given [block] at the
     * given [position] in the given [world].
     */
    fun canAttackBlock(player: KryptonPlayer, world: KryptonWorld, block: Block, position: Vector3i): Boolean = true

    /**
     * Called when a player interacts with a specific block, usually when they
     * are attempting to dig it up (left click interaction).
     */
    fun interact(context: InteractionContext): InteractionResult = InteractionResult.PASS

    /**
     * Called when the given [player] uses the item they are holding in the
     * given [hand].
     */
    fun use(player: KryptonPlayer, hand: Hand): UseItemResult = UseItemResult(InteractionResult.PASS, player.inventory.heldItem(hand))

    /**
     * Called when the given [player] finishes destroying the given [block] at
     * the given [position] in the given [world], using the given [item] to
     * destroy it.
     */
    fun mineBlock(player: KryptonPlayer, item: KryptonItemStack, world: KryptonWorld, block: Block, position: Vector3i): Boolean = false
}
