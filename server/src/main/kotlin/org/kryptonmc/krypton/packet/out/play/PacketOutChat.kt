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
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeUUID
import java.util.UUID

/**
 * Display the specified [message] in chat.
 *
 * @param message the message to be sent
 * @param type the type of message (either CHAT or SYSTEM, GAME_INFO is not supported)
 * @param senderUUID the sender's UUID, used for chat filtering?
 */
class PacketOutChat(
    private val message: Component,
    private val type: MessageType,
    private val senderUUID: UUID
) : PlayPacket(0x0F) {

    override fun write(buf: ByteBuf) {
        buf.writeChat(message)
        buf.writeByte(type.id)
        buf.writeUUID(senderUUID)
    }

    private val MessageType.id: Int
        get() = when (this) {
            MessageType.CHAT -> 0
            MessageType.SYSTEM -> 1
        }
}
