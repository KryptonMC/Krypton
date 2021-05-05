/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import kotlin.random.Random

/**
 * Sent to tell the client its initial position. This is one of the last packets sent by the server in the play
 * sequence.
 *
 * @param location the client's location
 * @param flags strange flags that determine whether each of the components of the [location] are relative
 * @param teleportId the ID for teleportation. Unused by Krypton.
 */
class PacketOutPlayerPositionAndLook(
    private val location: Location,
    private val flags: PositionAndLookFlags = PositionAndLookFlags(),
    private val teleportId: Int = Random.nextInt(1000)
) : PlayPacket(0x34) {

    override fun write(buf: ByteBuf) {
        buf.writeDouble(location.x)
        buf.writeDouble(location.y)
        buf.writeDouble(location.z)
        buf.writeFloat(location.yaw)
        buf.writeFloat(location.pitch)
        buf.writeByte(flags.toProtocol())
        buf.writeVarInt(teleportId)
    }
}

data class PositionAndLookFlags(
    val isRelativeX: Boolean = false,
    val isRelativeY: Boolean = false,
    val isRelativeZ: Boolean = false,
    val isRelativeYaw: Boolean = false,
    val isRelativePitch: Boolean = false
) {

    fun toProtocol(): Int {
        var flagsByte = 0x00
        if (isRelativeX) flagsByte += 0x01
        if (isRelativeY) flagsByte += 0x02
        if (isRelativeZ) flagsByte += 0x04
        if (isRelativeYaw) flagsByte += 0x08
        if (isRelativePitch) flagsByte += 0x10
        return flagsByte
    }
}
