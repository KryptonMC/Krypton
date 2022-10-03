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
import org.kryptonmc.krypton.network.chat.ChatSender
import org.kryptonmc.krypton.network.chat.MessageSignature
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutPlayerChatMessage(
    val signedMessage: Component,
    val unsignedMessage: Component?,
    val typeId: Int,
    val sender: ChatSender,
    val signature: MessageSignature
) : Packet {

    constructor(buf: ByteBuf) : this(
        buf.readComponent(),
        buf.readNullable(ByteBuf::readComponent),
        buf.readVarInt(),
        // FIXME: When we fix chat, this needs to be sorted
        ChatSender.SYSTEM,
        MessageSignature(buf)
    )

    override fun write(buf: ByteBuf) {
        buf.writeComponent(signedMessage)
        buf.writeNullable(unsignedMessage, ByteBuf::writeComponent)
        buf.writeVarInt(typeId)
        // FIXME: When we fix chat, this needs to be sorted
        //sender.write(buf)
        signature.write(buf)
    }
}
