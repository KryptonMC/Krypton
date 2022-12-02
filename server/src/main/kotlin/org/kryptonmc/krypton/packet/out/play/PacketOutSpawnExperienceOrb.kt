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
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.entity.KryptonExperienceOrb
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.readVec3d
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVec3d

@JvmRecord
data class PacketOutSpawnExperienceOrb(override val entityId: Int, val location: Vec3d, val count: Int) : EntityPacket {

    constructor(orb: KryptonExperienceOrb) : this(orb.id, orb.position, orb.count)

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readVec3d(), buf.readShort().toInt())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeVec3d(location)
        buf.writeShort(count)
    }
}
