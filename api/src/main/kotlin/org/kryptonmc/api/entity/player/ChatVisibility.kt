/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.player

/**
 * Settings for the visibility of chat messages for a player.
 */
public enum class ChatVisibility {

    /**
     * In this mode, the client wants to see all messages sent by the server.
     */
    FULL,

    /**
     * In this mode, the client only wants to see system messages, such as
     * those sent as an output to a command, or when a player joins or leaves,
     * not messages that originate from a player.
     */
    SYSTEM,

    /**
     * In this mode, the client does not want to see any chat messages. They
     * will still see game state updates however, which are action bars that
     * inform the player of certain things, such as "You may not rest now,
     * there are monsters nearby."
     */
    HIDDEN
}
