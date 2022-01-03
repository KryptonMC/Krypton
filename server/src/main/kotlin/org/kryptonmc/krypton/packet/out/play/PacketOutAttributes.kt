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
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.attribute.KryptonAttribute
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.util.mapPersistentList
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutAttributes(
    override val entityId: Int,
    val attributes: Collection<AttributeSnapshot>
) : EntityPacket {

    constructor(
        id: Int,
        attributes: Iterable<KryptonAttribute>
    ) : this(id, attributes.mapPersistentList { AttributeSnapshot(it.type, it.baseValue, it.modifiersByOperation) })

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeCollection(attributes) { attribute ->
            buf.writeKey(Registries.ATTRIBUTE[attribute.type]!!)
            buf.writeDouble(attribute.base)
            buf.writeVarInt(attribute.modifiers.values.sumOf { it.size })
            attribute.modifiers.forEach { (operation, modifiers) ->
                modifiers.forEach {
                    buf.writeUUID(it.uuid)
                    buf.writeDouble(it.amount)
                    buf.writeByte(Registries.MODIFIER_OPERATIONS.idOf(operation))
                }
            }
        }
    }

    @JvmRecord
    data class AttributeSnapshot(
        val type: AttributeType,
        val base: Double,
        val modifiers: Map<ModifierOperation, Set<AttributeModifier>>
    )
}
