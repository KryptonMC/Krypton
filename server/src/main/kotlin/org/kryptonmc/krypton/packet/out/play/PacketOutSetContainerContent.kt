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
import org.kryptonmc.krypton.inventory.KryptonInventory
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readItem
import org.kryptonmc.krypton.util.readList
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeItem
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutSetContainerContent(
    val id: Int,
    val stateId: Int,
    val items: List<KryptonItemStack>,
    val heldItem: KryptonItemStack
) : Packet {

    constructor(
        inventory: KryptonInventory,
        heldItem: KryptonItemStack
    ) : this(inventory.id, inventory.incrementStateId(), inventory.items, heldItem)

    constructor(buf: ByteBuf) : this(buf.readByte().toInt(), buf.readVarInt(), buf.readList(ByteBuf::readItem), buf.readItem())

    override fun write(buf: ByteBuf) {
        buf.writeByte(id)
        buf.writeVarInt(stateId)
        buf.writeCollection(items, buf::writeItem)
        buf.writeItem(heldItem)
    }
}
