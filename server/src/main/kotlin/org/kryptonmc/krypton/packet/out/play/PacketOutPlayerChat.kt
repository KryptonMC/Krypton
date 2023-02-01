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
import org.kryptonmc.krypton.network.chat.FilterMask
import org.kryptonmc.krypton.network.chat.MessageSignature
import org.kryptonmc.krypton.network.chat.RichChatType
import org.kryptonmc.krypton.network.chat.SignedMessageBody
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import java.util.UUID

@JvmRecord
data class PacketOutPlayerChat(
    val sender: UUID,
    val index: Int,
    val signature: MessageSignature?,
    val body: SignedMessageBody.Packed,
    val unsignedContent: Component?,
    val filterMask: FilterMask,
    val chatType: RichChatType.BoundNetwork
) : Packet {

    constructor(buf: ByteBuf) : this(buf.readUUID(), buf.readVarInt(), buf.readNullable(MessageSignature::read), SignedMessageBody.Packed(buf),
        buf.readNullable(ByteBuf::readComponent), FilterMask.read(buf), RichChatType.BoundNetwork(buf))

    override fun write(buf: ByteBuf) {
        buf.writeUUID(sender)
        buf.writeVarInt(index)
        buf.writeNullable(signature, MessageSignature::write)
        body.write(buf)
        buf.writeNullable(unsignedContent, ByteBuf::writeComponent)
        FilterMask.write(buf, filterMask)
        chatType.write(buf)
    }
}
