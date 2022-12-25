/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.packet.out.status

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.writeString

/**
 * Response to the client's earlier [status request][org.kryptonmc.krypton.packet.in.status.PacketInStatusRequest] packet.
 */
@JvmRecord
data class PacketOutStatusResponse(val response: String) : Packet {

    constructor(buf: ByteBuf) : this(buf.readString())

    override fun write(buf: ByteBuf) {
        buf.writeString(response)
    }

    companion object {

        private val GSON = GsonComponentSerializer.gson().serializer().newBuilder()
            .registerTypeAdapter(ServerStatus.Players::class.java, ServerStatus.Players)
            .registerTypeAdapter(ServerStatus::class.java, ServerStatus)
            .create()

        @JvmStatic
        fun create(status: ServerStatus): PacketOutStatusResponse = PacketOutStatusResponse(GSON.toJson(status))
    }
}
