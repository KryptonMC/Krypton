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
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.attribute.KryptonAttribute
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeModifier
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.mapPersistentList
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.readList
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import java.util.Collections

@JvmRecord
data class PacketOutUpdateAttributes(override val entityId: Int, val attributes: Collection<AttributeSnapshot>) : EntityPacket {

    constructor(id: Int, attributes: Iterable<KryptonAttribute>) : this(id, attributes.mapPersistentList(AttributeSnapshot::from))

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readList {
        val key = buf.readKey()
        val type = requireNotNull(Registries.ATTRIBUTE[key]) { "Cannot find attribute type with key $key!" }
        val base = buf.readDouble()
        val modifiers = buf.readList {
            val uuid = buf.readUUID()
            val amount = buf.readDouble()
            val operationId = buf.readByte().toInt()
            val operation = requireNotNull(KryptonRegistries.MODIFIER_OPERATIONS.get(operationId)) {
                "Cannot find modifier operation with ID $operationId!"
            }
            KryptonAttributeModifier("Unknown read attribute", uuid, amount, operation)
        }
        AttributeSnapshot(type, base, modifiers)
    })

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeCollection(attributes) { attribute ->
            buf.writeKey(Registries.ATTRIBUTE[attribute.type]!!)
            buf.writeDouble(attribute.base)
            buf.writeCollection(attribute.modifiers) {
                buf.writeUUID(it.uuid)
                buf.writeDouble(it.amount)
                buf.writeByte(KryptonRegistries.MODIFIER_OPERATIONS.idOf(it.operation))
            }
        }
    }

    @JvmRecord
    data class AttributeSnapshot(val type: AttributeType, val base: Double, val modifiers: Collection<AttributeModifier>) {

        companion object {

            @JvmStatic
            fun from(attribute: KryptonAttribute): AttributeSnapshot =
                AttributeSnapshot(attribute.type, attribute.baseValue, Collections.unmodifiableCollection(attribute.modifiers))
        }
    }
}
