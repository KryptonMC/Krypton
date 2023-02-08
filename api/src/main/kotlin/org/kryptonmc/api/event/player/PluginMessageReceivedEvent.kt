/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import net.kyori.adventure.key.Key
import org.kryptonmc.api.event.type.PlayerEvent

/**
 * Called when a plugin message is received from a client.
 */
public interface PluginMessageReceivedEvent : PlayerEvent {

    /**
     * The channel that the message was received on.
     */
    public val channel: Key

    /**
     * The message that was received from the sender.
     */
    public val message: ByteArray
}
