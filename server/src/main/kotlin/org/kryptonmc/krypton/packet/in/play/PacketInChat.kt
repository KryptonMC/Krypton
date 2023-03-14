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
package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.chat.LastSeenMessages
import org.kryptonmc.krypton.network.chat.MessageSignature
import org.kryptonmc.krypton.network.handlers.PlayPacketHandler
import org.kryptonmc.krypton.packet.InboundPacket
import org.kryptonmc.krypton.util.readInstant
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.writeInstant
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeString
import java.time.Instant

@JvmRecord
data class PacketInChat(
    val message: String,
    val timestamp: Instant,
    val salt: Long,
    val signature: MessageSignature?,
    val lastSeenMessages: LastSeenMessages.Update
) : InboundPacket<PlayPacketHandler> {

    constructor(buf: ByteBuf) : this(buf.readString(MAX_MESSAGE_LENGTH), buf.readInstant(), buf.readLong(),
        buf.readNullable(MessageSignature::read), LastSeenMessages.Update(buf))

    override fun write(buf: ByteBuf) {
        buf.writeString(message, MAX_MESSAGE_LENGTH)
        buf.writeInstant(timestamp)
        buf.writeLong(salt)
        buf.writeNullable(signature, MessageSignature::write)
        lastSeenMessages.write(buf)
    }

    override fun handle(handler: PlayPacketHandler) {
        handler.handleChat(this)
    }

    companion object {

        private const val MAX_MESSAGE_LENGTH = 256
    }
}
