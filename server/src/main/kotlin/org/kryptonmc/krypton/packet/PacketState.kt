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
package org.kryptonmc.krypton.packet

/**
 * Represents one of the four packet states in the Minecraft protocol. Each one of
 * these states has different packets and different purposes.
 */
enum class PacketState {

    /**
     * In the handshake state, the client informs the server of their intentions.
     */
    HANDSHAKE,

    /**
     * In the status state, the client requests information about the server, such
     * as the current player count, max player count, ping and MOTD.
     */
    STATUS,

    /**
     * In the login state, the client attempts to login to the server, and this
     * state establishes an encrypted connection with the client (when the server
     * is in online mode). The server will also set a compression threshold for
     * packet compression, if the threshold set in the config is > 0.
     */
    LOGIN,

    /**
     * The vast majority of packets that exist in the protocol are in the play state.
     * This state indicates that the client is playing the game.
     */
    PLAY
}
