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
import org.kryptonmc.api.event.GenericResult
import org.kryptonmc.api.event.ResultedEvent

/**
 * Called when a block is damaged.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BlockDamageEvent : PlayerEvent, ResultedEvent<GenericResult> {

    /**
     * The block that is being broken by the player.
     */
    @get:JvmName("block")
    public val block: Block
}
