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

import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.attribute.BasicModifierOperation
import org.kryptonmc.krypton.entity.attribute.KryptonAttribute
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeModifier
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.registry.KryptonRegistries
import java.util.Collections

@JvmRecord
data class PacketOutUpdateAttributes(override val entityId: Int, val attributes: Collection<AttributeSnapshot>) : EntityPacket {

    constructor(reader: BinaryReader) : this(reader.readVarInt(), reader.readList { _ ->
        val type = reader.readKey().let { requireNotNull(KryptonRegistries.ATTRIBUTE.get(it)) { "Cannot find attribute type with key $it!" } }
        val base = reader.readDouble()
        val modifiers = reader.readList {
            val uuid = it.readUUID()
            val amount = it.readDouble()
            val operation = KryptonAttributeModifier.getOperationById(it.readByte().toInt())
            KryptonAttributeModifier(uuid, "Unknown read attribute", amount, operation)
        }
        AttributeSnapshot(type, base, modifiers)
    })

    override fun write(writer: BinaryWriter) {
        writer.writeVarInt(entityId)
        writer.writeCollection(attributes) { attribute ->
            writer.writeKey(KryptonRegistries.ATTRIBUTE.getKey(attribute.type)!!)
            writer.writeDouble(attribute.base)
            writer.writeCollection(attribute.modifiers) inner@{
                val operation = it.operation
                if (operation !is BasicModifierOperation) return@inner
                writer.writeUUID(it.uuid)
                writer.writeDouble(it.amount)
                writer.writeByte(operation.ordinal.toByte())
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
