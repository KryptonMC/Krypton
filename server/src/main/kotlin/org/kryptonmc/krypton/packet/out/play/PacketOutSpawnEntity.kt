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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.util.toAngle
import org.kryptonmc.krypton.util.writeAngle
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutSpawnEntity(private val entity: KryptonEntity) : PlayPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entity.id)
        buf.writeUUID(entity.uuid)
        buf.writeVarInt(Registries.ENTITY_TYPE.idOf(entity.type))
        buf.writeDouble(entity.location.x)
        buf.writeDouble(entity.location.y)
        buf.writeDouble(entity.location.z)
        buf.writeAngle(entity.location.pitch.toAngle())
        buf.writeAngle(entity.location.yaw.toAngle())
        buf.writeInt(0)
        buf.writeShort((entity.velocity.x.clamp(-3.9, 3.9) * 8000.0).toInt())
        buf.writeShort((entity.velocity.y.clamp(-3.9, 3.9) * 8000.0).toInt())
        buf.writeShort((entity.velocity.z.clamp(-3.9, 3.9) * 8000.0).toInt())
    }
}
