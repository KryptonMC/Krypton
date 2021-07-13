/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.util.InteractionResult
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3i

/**
 * A handler for a block.
 *
 * This helps promote sharing, as these can be reused between multiple block
 * objects that share properties.
 *
 * You can register these with [BlockManager.register].
 */
interface BlockHandler {

    /**
     * Gets the destroy progress of the given [block] at the given [position], being
     * broken by the given [player] in the given [world].
     *
     * @param player the player breaking the block
     * @param world the world the block is in
     * @param block the block being broken
     * @param position the position of the block
     * @return the destroy progress of the block
     */
    fun getDestroyProgress(player: Player, world: World, block: Block, position: Vector3i): Float

    /**
     * Called when the given [block] is placed by the given [player] at the given [position],
     * being placed on the given [face] (this will be facing the player).
     *
     * @param player the player who placed the block
     * @param block the block that was placed
     * @param position the position of the block
     * @param face the face that the block was placed on
     */
    fun onPlace(player: Player, block: Block, position: Vector3i, face: BlockFace)

    /**
     * Called when the given [block] is destroyed by the given [player] at the given [position],
     * with the given [item].
     *
     * @param player the player who destroyed the block
     * @param block the block that was destroyed
     * @param position the position of the block
     * @param item the item used to destroy the block
     */
    fun onDestroy(player: Player, block: Block, position: Vector3i, item: ItemStack)

    /**
     * Called when the given [player] interacts with the given [block] at the given [position]
     * in the given [world], using the given interaction [hand].
     *
     * @param player the player who interacted with the block
     * @param world the world the block is in
     * @param block the block that was interacted with
     * @param position the position of the block
     * @param hand the hand used to interact with the block
     * @return the result of the interaction
     */
    fun interact(player: Player, world: World, block: Block, position: Vector3i, hand: Hand): InteractionResult

    /**
     * Called when the given [player] attacks the given [block] at the given [position] in
     * the given [world].
     *
     * @param player the player who attacked the block
     * @param world the world the block is in
     * @param block the block that was attacked
     * @param position the position of the block
     */
    fun attack(player: Player, world: World, block: Block, position: Vector3i)
}
