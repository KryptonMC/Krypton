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
package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt

/**
 * Sent when the client attempts to tab complete a command
 */
class PacketInTabComplete : PlayPacket(0x06) {

    /**
     * A unique ID for the tab complete session. Must be returned to the client.
     */
    var id = -1; private set

    /**
     * The command to tab complete. May or may not include '/'.
     */
    lateinit var command: String private set

    override fun read(buf: ByteBuf) {
        id = buf.readVarInt()
        command = buf.readString(32500)
    }
}
