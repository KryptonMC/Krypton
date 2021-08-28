/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.player.Player

/**
 * Called when a plugin message is received from a client.
 *
 * @param player the player who's client sent the message
 * @param channel the channel the message came from
 * @param message the message received
 */
public class PluginMessageEvent(
    public val player: Player,
    public val channel: Key,
    public val message: ByteArray
)
