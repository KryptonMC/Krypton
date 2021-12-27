/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.ComponentResult
import org.kryptonmc.api.event.ResultedEvent
import org.kryptonmc.api.item.ItemStack

/**
 * Called when a player attempts to drop an item.
 *
 * @param player the player dropping the item
 * @param item the item to be dropped
 */

public data class ItemDropEvent(
    @get:JvmName("player") public val player: Player,
    @get:JvmName("item") public val item: ItemStack
) : ResultedEvent<ComponentResult> {

    override var result: ComponentResult = ComponentResult.allowed()
}

