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
package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.event.player.PerformActionEvent.Action
import org.kryptonmc.krypton.network.handlers.PlayPacketHandler
import org.kryptonmc.krypton.packet.InboundPacket
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketInPlayerCommand(val id: Int, val action: Action, val data: Int) : InboundPacket<PlayPacketHandler> {

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readEnum<Action>(), buf.readVarInt())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(id)
        buf.writeEnum(action)
        buf.writeVarInt(data)
    }

    override fun handle(handler: PlayPacketHandler) {
        handler.handlePlayerCommand(this)
    }
}
