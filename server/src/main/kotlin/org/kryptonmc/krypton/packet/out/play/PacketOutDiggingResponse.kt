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
import org.kryptonmc.api.block.Block
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerDigging
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVector
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.spongepowered.math.vector.Vector3i

@JvmRecord
data class PacketOutDiggingResponse(
    val x: Int,
    val y: Int,
    val z: Int,
    val stateId: Int,
    val status: PacketInPlayerDigging.Status,
    val successful: Boolean
) : Packet {

    constructor(position: Vector3i, stateId: Int, status: PacketInPlayerDigging.Status, successful: Boolean) : this(
        position.x(),
        position.y(),
        position.z(),
        stateId,
        status,
        successful
    )

    constructor(position: Vector3i, block: KryptonBlock, status: PacketInPlayerDigging.Status, successful: Boolean) : this(
        position,
        block.stateId,
        status,
        successful
    )

    constructor(x: Int, y: Int, z: Int, block: KryptonBlock, status: PacketInPlayerDigging.Status, successful: Boolean) : this(
        x,
        y,
        z,
        block.stateId,
        status,
        successful
    )

    override fun write(buf: ByteBuf) {
        buf.writeVector(x, y, z)
        buf.writeVarInt(stateId)
        buf.writeVarInt(status.ordinal)
        buf.writeBoolean(successful)
    }
}
