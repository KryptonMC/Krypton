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

import org.kryptonmc.krypton.command.argument.ArgumentSignatures
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.network.handlers.PlayPacketHandler
import org.kryptonmc.krypton.network.chat.LastSeenMessages
import org.kryptonmc.krypton.packet.InboundPacket
import java.time.Instant

@JvmRecord
data class PacketInChatCommand(
    val command: String,
    val timestamp: Instant,
    val salt: Long,
    val argumentSignatures: ArgumentSignatures,
    val lastSeenMessages: LastSeenMessages.Update
) : InboundPacket<PlayPacketHandler> {

    init {
        require(command.length <= MAX_COMMAND_LENGTH) { "Command is too long! Max: $MAX_COMMAND_LENGTH" }
    }

    constructor(reader: BinaryReader) : this(reader.readString(), reader.readInstant(), reader.readLong(), ArgumentSignatures(reader),
        LastSeenMessages.Update(reader))

    override fun write(writer: BinaryWriter) {
        writer.writeString(command)
        writer.writeInstant(timestamp)
        writer.writeLong(salt)
        argumentSignatures.write(writer)
        lastSeenMessages.write(writer)
    }

    override fun handle(handler: PlayPacketHandler) {
        handler.handleChatCommand(this)
    }

    companion object {

        private const val MAX_COMMAND_LENGTH = 256
    }
}
