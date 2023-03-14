/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
