/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.util.Direction

/**
 * Called when a player places a block.
 *
 * Note: This should be called when the client indicates that it has placed a
 * block, **before** the server has started any checks to validate whether the
 * player is able to place the block.
 * This event signals when the player places a block, **not** when the server
 * handles that block placement.
 *
 * @param block the block that was placed
 * @param hand the hand that the placed block is in
 * @param x the X coordinate of the block
 * @param y the Y coordinate of the block
 * @param z the Z coordinate of the block
 * @param face the face on which the block was placed
 * @param isInsideBlock whether the player's head is inside a block
 */
public class PlaceBlockEvent(
    player: Player,
    @get:JvmName("block") public val block: Block,
    @get:JvmName("hand") public val hand: Hand,
    @get:JvmName("x") public val x: Int,
    @get:JvmName("y") public val y: Int,
    @get:JvmName("z") public val z: Int,
    @get:JvmName("face") public val face: Direction,
    public val isInsideBlock: Boolean
) : InteractEvent(player, Type.PLACE_BLOCK)
