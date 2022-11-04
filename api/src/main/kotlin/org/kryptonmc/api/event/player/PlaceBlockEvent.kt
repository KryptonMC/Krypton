/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.util.Direction
import org.spongepowered.math.vector.Vector3i

/**
 * Called when a player places a block.
 *
 * Note: This should be called when the client indicates that it has placed a
 * block, **before** the server has started any checks to validate whether the
 * player is able to place the block.
 * This event signals when the player places a block, **not** when the server
 * handles that block placement.
 */
public interface PlaceBlockEvent : InteractEvent {

    /**
     * The block that was placed.
     */
    public val block: BlockState

    /**
     * The hand that the player used to place the block.
     */
    public val hand: Hand

    /**
     * The position where the block was placed.
     */
    public val position: Vector3i

    /**
     * The face of the block on which the block was placed.
     */
    public val face: Direction

    /**
     * Whether the player's head is inside the block.
     */
    public val isInsideBlock: Boolean

    override val type: InteractEvent.Type
        get() = InteractEvent.Type.PLACE_BLOCK
}
