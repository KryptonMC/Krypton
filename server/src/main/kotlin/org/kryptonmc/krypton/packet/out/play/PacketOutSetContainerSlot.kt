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
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readItem
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeItem
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutSetContainerSlot(val id: Int, val stateId: Int, val slot: Int, val item: KryptonItemStack) : Packet {

    constructor(buf: ByteBuf) : this(buf.readByte().toInt(), buf.readVarInt(), buf.readShort().toInt(), buf.readItem())

    override fun write(buf: ByteBuf) {
        buf.writeByte(id)
        buf.writeVarInt(stateId)
        buf.writeShort(slot)
        buf.writeItem(item)
    }
}
