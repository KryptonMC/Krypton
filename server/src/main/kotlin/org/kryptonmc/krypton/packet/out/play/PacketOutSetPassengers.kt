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

    constructor(entity: KryptonEntity, passengers: List<Entity>) : this(entity.id, toIdArray(passengers))

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readVarIntArray())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeVarIntArray(passengers)
    }

    override fun equals(other: Any?): Boolean =
        this === other || other is PacketOutSetPassengers && entityId == other.entityId && passengers.contentEquals(other.passengers)

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + entityId.hashCode()
        result = 31 * result + passengers.contentHashCode()
        return result
    }

    companion object {

        @JvmStatic
        private fun toIdArray(entities: List<Entity>): IntArray = IntArray(entities.size) { entities.get(it).id }
    }
}
