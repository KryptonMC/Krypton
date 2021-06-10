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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Updates the client's time. Namely, the age of the world and the time of day. This is used to keep the client
 * in sync with the server and all other clients.
 *
 * @param worldAge the age of the world
 * @param timeOfDay the time of day of the server
 */
class PacketOutTimeUpdate(
    private val worldAge: Long,
    private val timeOfDay: Long
) : PlayPacket(0x58) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(worldAge)
        buf.writeLong(timeOfDay)
    }
}
