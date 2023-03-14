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
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.readVarIntArray
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarIntArray

@JvmRecord
@Suppress("ArrayInDataClass")
data class PacketOutSetPassengers(override val entityId: Int, val passengers: IntArray) : EntityPacket {

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readVarIntArray())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeVarIntArray(passengers)
    }

    companion object {

        @JvmStatic
        fun fromEntity(entity: KryptonEntity, passengers: List<Entity>): PacketOutSetPassengers =
            PacketOutSetPassengers(entity.id, toIdArray(passengers))

        @JvmStatic
        private fun toIdArray(entities: List<Entity>): IntArray = IntArray(entities.size) { entities.get(it).id }
    }
}
