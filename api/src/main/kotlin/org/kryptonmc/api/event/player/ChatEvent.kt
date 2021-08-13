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

/**
 * Called when a player sends a chat message (not a command).
 *
 * @param player the player sending the message
 * @param message the message sent
 */
class ChatEvent(
    val player: Player,
    val message: String
) : ResultedEvent<ComponentResult> {

    override var result = ComponentResult.allowed()
}
