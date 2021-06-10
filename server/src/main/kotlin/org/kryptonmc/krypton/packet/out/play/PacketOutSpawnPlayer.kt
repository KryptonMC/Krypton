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
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.toAngle
import org.kryptonmc.krypton.util.writeAngle
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Spawn a player for the client.
 *
 * @param player the player to spawn.
 */
class PacketOutSpawnPlayer(private val player: KryptonPlayer) : PlayPacket(0x04) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(player.session.id)
        buf.writeUUID(player.uuid)

        buf.writeDouble(player.location.x)
        buf.writeDouble(player.location.y)
        buf.writeDouble(player.location.z)
        buf.writeAngle(player.location.yaw.toAngle())
        buf.writeAngle(player.location.pitch.toAngle())
    }
}
