/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.item.handler

import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.UseItemResult
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
    fun canAttackBlock(player: KryptonPlayer, world: KryptonWorld, block: KryptonBlockState, pos: Vec3i): Boolean = true

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
    fun mineBlock(player: KryptonPlayer, item: KryptonItemStack, world: KryptonWorld, block: KryptonBlockState, pos: Vec3i): Boolean = false
}
