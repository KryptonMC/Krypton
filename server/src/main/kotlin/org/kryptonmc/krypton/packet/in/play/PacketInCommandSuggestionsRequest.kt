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
import org.kryptonmc.krypton.network.handlers.PlayPacketHandler
import org.kryptonmc.krypton.packet.InboundPacket
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketInCommandSuggestionsRequest(val id: Int, val command: String) : InboundPacket<PlayPacketHandler> {

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readString(COMMAND_MAX_LENGTH))

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(id)
        buf.writeString(command, COMMAND_MAX_LENGTH)
    }

    override fun handle(handler: PlayPacketHandler) {
        handler.handleCommandSuggestionsRequest(this)
    }

    companion object {

        private const val COMMAND_MAX_LENGTH = 32500
    }
}
