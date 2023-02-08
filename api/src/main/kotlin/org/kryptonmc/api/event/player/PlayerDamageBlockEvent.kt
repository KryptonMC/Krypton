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
import org.kryptonmc.api.event.type.DeniableEvent
import org.kryptonmc.api.event.type.PlayerEvent

/**
 * Called when a block is damaged by a player.
 */
public interface PlayerDamageBlockEvent : PlayerEvent, DeniableEvent {

    /**
     * The block that is being broken by the player.
     */
    public val block: Block
}
