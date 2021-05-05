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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * This is a message to the client to say "hey, I'm still here by the way", so the client doesn't just assume
 * we've timed out and disconnect.
 *
 * @param keepAliveId a unique ID for the keep alive. Vanilla calls this the challenge, and this is generally
 * [System.currentTimeMillis]
 */
class PacketOutKeepAlive(private val keepAliveId: Long) : PlayPacket(0x1F) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(keepAliveId)
    }
}
