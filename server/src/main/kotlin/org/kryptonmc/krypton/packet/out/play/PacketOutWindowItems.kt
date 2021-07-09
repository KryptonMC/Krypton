/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeItem
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Set the items for an inventory with an ID. Currently only supports player inventories.
 *
 * @param inventory the inventory to get the items to send from
 */
class PacketOutWindowItems(private val inventory: KryptonInventory) : PlayPacket(0x14) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(inventory.id)
        val networkItems = inventory.networkItems
        buf.writeShort(networkItems.size)
        networkItems.forEach { buf.writeItem(it) }
    }
}
