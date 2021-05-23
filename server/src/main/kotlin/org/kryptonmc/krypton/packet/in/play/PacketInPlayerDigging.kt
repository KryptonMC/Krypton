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
package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.space.Vector
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.toVector
import org.kryptonmc.krypton.world.block.BlockFace

class PacketInPlayerDigging : PlayPacket(0x1B) {

    lateinit var status: DiggingStatus private set
    lateinit var location: Vector private set
    lateinit var face: BlockFace private set

    override fun read(buf: ByteBuf) {
        status = buf.readEnum()
        location = buf.readLong().toVector()
        face = BlockFace.fromId(buf.readVarInt())
    }
}

enum class DiggingStatus {

    STARTED,
    CANCELLED,
    FINISHED,
    DROP_ITEM_STACK,
    DROP_ITEM,
    UPDATE_STATE,
    SWAP_ITEM_IN_HAND
}
