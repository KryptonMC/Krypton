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
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.readVarIntArray
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarIntArray

@JvmRecord
data class PacketOutSetPassengers(override val entityId: Int, val passengers: IntArray) : EntityPacket {

    constructor(entity: KryptonEntity) : this(entity.id, entity.passengers.toIdArray())

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readVarIntArray())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeVarIntArray(passengers)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return entityId == (other as PacketOutSetPassengers).entityId && passengers.contentEquals(other.passengers)
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + entityId.hashCode()
        result = 31 * result + passengers.contentHashCode()
        return result
    }
}

private fun List<Entity>.toIdArray(): IntArray = IntArray(size) { get(it).id }
