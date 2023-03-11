/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
