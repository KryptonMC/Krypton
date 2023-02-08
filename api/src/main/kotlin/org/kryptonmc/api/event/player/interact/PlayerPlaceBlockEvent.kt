/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player.interact

import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i

/**
 * Called when a player places a block.
 */
public interface PlayerPlaceBlockEvent : PlayerInteractEvent {

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
    public val position: Vec3i

    /**
     * The face of the block on which the block was placed.
     */
    public val face: Direction

    /**
     * Whether the player's head is inside the block.
     */
    public val isInsideBlock: Boolean
}
