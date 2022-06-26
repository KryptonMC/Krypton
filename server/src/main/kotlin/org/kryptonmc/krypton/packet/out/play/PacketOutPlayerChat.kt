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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarIntByteArray
import java.util.UUID

@Suppress("ArrayInDataClass")
@JvmRecord
data class PacketOutPlayerChat(
    val signedMessage: Component,
    val unsignedMessage: Component?,
    val typeId: Int,
    val senderUUID: UUID,
    val senderName: Component,
    val senderTeamName: Component?,
    val timestamp: Long,
    val salt: Long,
    val signature: ByteArray
) : Packet {

    override fun write(buf: ByteBuf) {
        buf.writeChat(signedMessage)
        buf.writeBoolean(unsignedMessage != null)
        if (unsignedMessage != null) buf.writeChat(unsignedMessage)
        buf.writeVarInt(typeId)
        buf.writeUUID(senderUUID)
        buf.writeChat(senderName)
        buf.writeBoolean(senderTeamName != null)
        if (senderTeamName != null) buf.writeChat(senderTeamName)
        buf.writeLong(timestamp)
        buf.writeLong(salt)
        buf.writeVarIntByteArray(signature)
    }
}
