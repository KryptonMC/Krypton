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
import org.kryptonmc.krypton.inventory.KryptonPlayerInventory
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.collection.FixedList
import org.kryptonmc.krypton.util.readCollection
import org.kryptonmc.krypton.util.readItem
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeItem
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutSetContainerContent(val id: Int, val stateId: Int, val items: List<KryptonItemStack>, val heldItem: KryptonItemStack) : Packet {

    constructor(buf: ByteBuf) : this(buf.readUnsignedByte().toInt(), buf.readVarInt(),
        buf.readCollection({ FixedList(it, KryptonItemStack.EMPTY) }, ByteBuf::readItem), buf.readItem())

    override fun write(buf: ByteBuf) {
        buf.writeByte(id)
        buf.writeVarInt(stateId)
        buf.writeCollection(items, buf::writeItem)
        buf.writeItem(heldItem)
    }

    companion object {

        @JvmStatic
        fun fromPlayerInventory(inventory: KryptonPlayerInventory): PacketOutSetContainerContent =
            PacketOutSetContainerContent(inventory.id, inventory.incrementStateId(), inventory.items, inventory.mainHand)
    }
}
