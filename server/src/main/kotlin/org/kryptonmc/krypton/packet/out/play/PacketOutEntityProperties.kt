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
import org.kryptonmc.krypton.entity.attribute.Attribute
import org.kryptonmc.krypton.entity.attribute.AttributeInstance
import org.kryptonmc.krypton.entity.attribute.AttributeModifier
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Set entity attributes. Note that this packet is not incremental, and sending it with data will reset
 * all of the existing attributes and reapply them.
 *
 * @param entityId the ID of the entity to set the attributes of
 * @param attributes the attributes to set
 */
class PacketOutEntityProperties(
    private val entityId: Int,
    attributes: Collection<AttributeInstance>
) : PlayPacket(0x63) {

    private val attributes = attributes.map { AttributeSnapshot(it.attribute, it.baseValue, it.modifiers) }

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeCollection(attributes) { attribute ->
            buf.writeString("minecraft:${attribute.attribute.description.removePrefix("attribute.name.")}") // TODO: Attribute registry
            buf.writeDouble(attribute.base)
            buf.writeCollection(attribute.modifiers) {
                buf.writeUUID(it.id)
                buf.writeDouble(it.amount)
                buf.writeByte(it.operation.ordinal)
            }
        }
    }

    data class AttributeSnapshot(val attribute: Attribute, val base: Double, val modifiers: Collection<AttributeModifier>)
}
