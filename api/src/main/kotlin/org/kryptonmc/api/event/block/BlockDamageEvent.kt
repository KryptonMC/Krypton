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
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.GenericResult
import org.kryptonmc.api.event.ResultedEvent

/**
 * Called when a block is damaged.
 *
 * @param player the player that damaged the block
 * @param block the block being broken
 */
class BlockDamageEvent(
    val player: Player,
    val block: Block
) : ResultedEvent<GenericResult> {

    override var result = GenericResult.allowed()
}
