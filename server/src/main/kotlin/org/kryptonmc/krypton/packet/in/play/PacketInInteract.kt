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
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketInInteract(
    val entityId: Int,
    val type: Type,
    val x: Float,
    val y: Float,
    val z: Float,
    val hand: Hand?,
    val sneaking: Boolean
) : Packet {

    constructor(buf: ByteBuf) : this(
        buf,
        buf.readVarInt(),
        buf.readEnum<Type>()
    )

    private constructor(buf: ByteBuf, entityId: Int, type: Type) : this(
        entityId,
        type,
        if (type == Type.INTERACT_AT) buf.readFloat() else 0F,
        if (type == Type.INTERACT_AT) buf.readFloat() else 0F,
        if (type == Type.INTERACT_AT) buf.readFloat() else 0F,
        if (type == Type.INTERACT || type == Type.INTERACT_AT) buf.readEnum<Hand>() else null,
        buf.readBoolean()
    )

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeEnum(type)
        if (type != Type.ATTACK) {
            if (type == Type.INTERACT_AT) {
                buf.writeFloat(x)
                buf.writeFloat(y)
                buf.writeFloat(z)
            }
            buf.writeEnum(requireNotNull(hand) { "Hand must be present for non-attacking interactions!" })
        }
    }

    enum class Type {

        INTERACT,
        ATTACK,
        INTERACT_AT
    }
}
