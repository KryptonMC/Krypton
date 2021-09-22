/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.block

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockFace
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player

/**
 * Called when a block is interacted with.
 *
 * @param player the player who interacted with the block
 * @param hand the hand used to interact
 * @param block the block interacted with
 * @param face the face of the block interacted with
 */
// TODO: Make this resulted
public data class BlockInteractEvent(
    @get:JvmName("player") public val player: Player,
    @get:JvmName("hand") public val hand: Hand,
    @get:JvmName("block") public val block: Block,
    @get:JvmName("face") public val face: BlockFace
)
