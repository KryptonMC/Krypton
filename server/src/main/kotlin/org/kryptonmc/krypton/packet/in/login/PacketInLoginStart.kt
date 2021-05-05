/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.`in`.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.LoginPacket
import org.kryptonmc.krypton.util.readString

/**
 * Sent by the client to inform the server of the client's username. This is either used to construct
 * the player's offline UUID if we are not in online mode, or sent to Mojang to attempt to authenticate
 * the user if we are in online mode.
 */
class PacketInLoginStart : LoginPacket(0x00) {

    /**
     * The username of the player logging in
     */
    lateinit var name: String private set

    override fun read(buf: ByteBuf) {
        name = buf.readString(16)
    }
}
