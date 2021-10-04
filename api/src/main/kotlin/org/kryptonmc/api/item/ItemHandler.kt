/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.util.InteractionResult
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3i

/**
 * A handler for a type of item.
 *
 * This helps promote sharing, as these can be reused between multiple item
 * type that share properties.
 *
 * You can register these with [ItemManager.register].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ItemHandler {

    /**
     * Gets the destroy speed of the given [block] when destroyed with the
     * given [item].
     *
     * @param item the item being used to destroy the block
     * @param block the block being destroyed
     */
    public fun getDestroySpeed(item: ItemStack, block: Block): Float

    /**
     * Returns true if this item type is the correct tool to break the
     * given [block], false otherwise.
     *
     * @param block the block to check
     * @return true if this type is the correct tool, false otherwise
     */
    public fun isCorrectTool(block: Block): Boolean

    /**
     * Returns true if the given [player] can attack the given [block] at the
     * given [position] in the given [world].
     *
     * @param player the player attempting to attack the block
     * @param world the world the block is in
     * @param block the block attempting to be attacked
     * @param position the position of the block
     * @return true if the block can be attacked, false otherwise
     */
    public fun canAttackBlock(player: Player, world: World, block: Block, position: Vector3i): Boolean

    /**
     * Called when a player interacts with a specific block, usually when they
     * are attempting to dig it up (left click interaction).
     *
     * @param context the context of which the interaction is taking place in
     * @return the result of the interaction
     */
    public fun interact(context: InteractionContext): InteractionResult

    /**
     * Called when the given [player] uses the item they are holding in the
     * given [hand].
     *
     * @param player the player using the item
     * @param hand the hand used
     * @return the result of using the item
     */
    public fun use(player: Player, hand: Hand): UseItemResult

    /**
     * Called when the given [player] finishes destroying the given [block] at
     * the given [position] in the given [world], using the given [item] to
     * destroy it.
     *
     * @param player the player who destroyed the block
     * @param item the item used to destroy the block
     * @param world the world the block is in
     * @param block the block that was destroyed
     * @param position the position of the block
     * @return if the mining was successful or not
     */
    public fun mineBlock(player: Player, item: ItemStack, world: World, block: Block, position: Vector3i): Boolean
}
