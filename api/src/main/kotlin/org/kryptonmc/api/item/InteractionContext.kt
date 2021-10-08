/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import org.kryptonmc.api.block.BlockHitResult
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i

/**
 * Context for when a player interacts with a block.
 */
public interface InteractionContext {

    /**
     * The player that is performing the interaction.
     */
    public val player: Player

    /**
     * The world the block being interacted with is in.
     */
    public val world: World

    /**
     * The item that the player held while interacting.
     */
    public val heldItem: ItemStack

    /**
     * The hand being used to interact with.
     */
    public val hand: Hand

    /**
     * The result of the player attempting to hit the block.
     */
    public val hitResult: BlockHitResult

    /**
     * The position of the block that is being interacted with.
     */
    public val position: Vector3i
        get() = hitResult.position

    /**
     * The face of the block that the player clicked.
     */
    public val clickedFace: Direction
        get() = hitResult.direction

    /**
     * The location where the player clicked.
     */
    public val clickLocation: Vector3d
        get() = hitResult.clickLocation

    /**
     * If the player is inside the block.
     */
    public val isInside: Boolean
        get() = hitResult.isInside

    /**
     * The pitch of the player.
     */
    public val pitch: Float
        get() = player.rotation.y()
}
