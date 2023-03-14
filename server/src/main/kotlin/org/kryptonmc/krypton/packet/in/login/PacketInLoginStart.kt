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
package org.kryptonmc.krypton.packet.`in`.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.handlers.LoginPacketHandler
import org.kryptonmc.krypton.packet.InboundPacket
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import java.util.UUID

@JvmRecord
data class PacketInLoginStart(val name: String, val profileId: UUID?) : InboundPacket<LoginPacketHandler> {

    constructor(buf: ByteBuf) : this(buf.readString(16), buf.readNullable(ByteBuf::readUUID))

    override fun write(buf: ByteBuf) {
        buf.writeString(name, 16)
        buf.writeNullable(profileId, ByteBuf::writeUUID)
    }

    override fun handle(handler: LoginPacketHandler) {
        handler.handleLoginStart(this)
    }
}
