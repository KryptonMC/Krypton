/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.plugin

import net.kyori.adventure.key.Key

/**
 * A recipient of plugin messages
 */
interface PluginMessageRecipient {

    /**
     * Send a plugin message to this recipient on the specified [channel] with the specified
     * [message] content.
     *
     * @param channel the channel to send the message on
     * @param message the message to send
     */
    fun sendPluginMessage(channel: Key, message: ByteArray)
}
