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
import org.kryptonmc.api.world.Location
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.toAngle
import org.kryptonmc.krypton.util.writeAngle
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutEntityTeleport(
    private val entityId: Int,
    private val location: Location,
    private val isOnGround: Boolean
) : PlayPacket(0x61) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeDouble(location.x)
        buf.writeDouble(location.y)
        buf.writeDouble(location.z)
        buf.writeAngle(location.yaw.toAngle())
        buf.writeAngle(location.pitch.toAngle())
        buf.writeBoolean(isOnGround)
    }
}
