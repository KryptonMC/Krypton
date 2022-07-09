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
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarIntByteArray
import java.util.UUID

@Suppress("ArrayInDataClass")
@JvmRecord
data class PacketOutPlayerChatMessage(
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

    constructor(buf: ByteBuf) : this(
        buf.readComponent(),
        buf.readNullable { buf.readComponent() },
        buf.readVarInt(),
        buf.readUUID(),
        buf.readComponent(),
        buf.readNullable { buf.readComponent() },
        buf.readLong(),
        buf.readLong(),
        buf.readVarIntByteArray()
    )

    override fun write(buf: ByteBuf) {
        buf.writeComponent(signedMessage)
        buf.writeNullable(unsignedMessage, ByteBuf::writeComponent)
        buf.writeVarInt(typeId)
        buf.writeUUID(senderUUID)
        buf.writeComponent(senderName)
        buf.writeNullable(senderTeamName, ByteBuf::writeComponent)
        buf.writeLong(timestamp)
        buf.writeLong(salt)
        buf.writeVarIntByteArray(signature)
    }
}
