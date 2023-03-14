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
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.entity.player.ChatVisibility
import org.kryptonmc.krypton.network.handlers.PlayPacketHandler
import org.kryptonmc.krypton.packet.InboundPacket
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeString

@JvmRecord
data class PacketInClientInformation(
    val locale: String,
    val viewDistance: Int,
    val chatVisibility: ChatVisibility,
    val chatColors: Boolean,
    val skinSettings: Short,
    val mainHand: MainHand,
    val filterText: Boolean,
    val allowsListing: Boolean
) : InboundPacket<PlayPacketHandler> {

    constructor(buf: ByteBuf) : this(buf.readString(16), buf.readByte().toInt(), buf.readEnum(), buf.readBoolean(), buf.readUnsignedByte(),
        buf.readEnum(), buf.readBoolean(), buf.readBoolean())

    override fun write(buf: ByteBuf) {
        buf.writeString(locale, 16)
        buf.writeByte(viewDistance)
        buf.writeEnum(chatVisibility)
        buf.writeBoolean(chatColors)
        buf.writeByte(skinSettings.toInt())
        buf.writeEnum(mainHand)
        buf.writeBoolean(filterText)
        buf.writeBoolean(allowsListing)
    }

    override fun handle(handler: PlayPacketHandler) {
        handler.handleClientInformation(this)
    }
}
