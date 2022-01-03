/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.player

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.MessageType

/**
 * Settings for the visibility of chat messages for a player.
 *
 * @param messageType the highest message type that can be sent with the
 * visibility, with the priority being determined by the ordinal low to high,
 * for example, [MessageType.CHAT] is higher than [MessageType.SYSTEM], and so
 * the [FULL] type that has [MessageType.CHAT] also includes
 * [MessageType.SYSTEM]
 */
public enum class ChatVisibility(public val messageType: MessageType?) {

    /**
     * In this mode, the client wants to see all messages sent by the server.
     */
    FULL(MessageType.CHAT),

    /**
     * In this mode, the client only wants to see system messages. That is,
     * messages sent with the type [MessageType.SYSTEM].
     */
    SYSTEM(MessageType.SYSTEM),

    /**
     * In this mode, the client does not want to see any chat messages. Vanilla
     * will still send action bars using it's GAME_INFO message type, however
     * Adventure does not support this due to the existence of
     * [Audience.sendActionBar], and its corresponding separate packet, so
     * Krypton does not support this either, meaning the client will receive
     * no chat messages in this mode.
     */
    HIDDEN(null)
}
