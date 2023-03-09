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
import org.kryptonmc.api.entity.attribute.BasicModifierOperation
import org.kryptonmc.krypton.entity.attribute.KryptonAttribute
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeModifier
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.registry.KryptonRegistries
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

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readList { _ ->
        val type = buf.readKey().let { requireNotNull(KryptonRegistries.ATTRIBUTE.get(it)) { "Cannot find attribute type with key $it!" } }
        val base = buf.readDouble()
        val modifiers = buf.readList {
            val uuid = buf.readUUID()
            val amount = buf.readDouble()
            val operation = KryptonAttributeModifier.getOperationById(buf.readByte().toInt())
            KryptonAttributeModifier(uuid, "Unknown read attribute", amount, operation)
        }
        AttributeSnapshot(type, base, modifiers)
    })

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeCollection(attributes) { attribute ->
            buf.writeKey(KryptonRegistries.ATTRIBUTE.getKey(attribute.type)!!)
            buf.writeDouble(attribute.base)
            buf.writeCollection(attribute.modifiers) inner@{
                val operation = it.operation
                if (operation !is BasicModifierOperation) return@inner
                buf.writeUUID(it.uuid)
                buf.writeDouble(it.amount)
                buf.writeByte(operation.ordinal)
            }
        }
    }

    @JvmRecord
    data class AttributeSnapshot(val type: AttributeType, val base: Double, val modifiers: Collection<AttributeModifier>) {

        companion object {

            @JvmStatic
            fun from(attribute: KryptonAttribute): AttributeSnapshot {
                return AttributeSnapshot(attribute.type, attribute.baseValue, Collections.unmodifiableCollection(attribute.modifiers))
            }
        }
    }

    companion object {

        @JvmStatic
        fun create(id: Int, attributes: Iterable<KryptonAttribute>): PacketOutUpdateAttributes {
            return PacketOutUpdateAttributes(id, attributes.map { AttributeSnapshot.from(it) })
        }
    }
}
