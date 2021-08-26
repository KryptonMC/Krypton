/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet

/**
 * Represents one of the four packet states in the Minecraft protocol. Each one of these states has
 * different packets and different purposes.
 */
enum class PacketState {

    /**
     * In the handshake state, the client informs the server of their intentions
     */
    HANDSHAKE,

    /**
     * In the status state, the client requests information about the server, such as the current player
     * count, max player count, ping and MOTD.
     */
    STATUS,

    /**
     * In the login state, the client attempts to login to the server, and this state establishes an
     * encrypted connection with the client (when the server is in online mode). The server will also
     * set a compression threshold for packet compression, if the threshold set in the config is > 0
     */
    LOGIN,

    /**
     * The vast majority of packets that exist in the protocol are in the play state. This state
     * indicates that the client is playing the game.
     */
    PLAY;

    companion object {

        /**
         * Gets a packet state from its protocol ID.
         */
        fun fromId(id: Int) = values()[id]
    }
}
