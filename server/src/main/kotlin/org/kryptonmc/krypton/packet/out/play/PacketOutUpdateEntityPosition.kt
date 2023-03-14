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

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.util.Position
import org.kryptonmc.krypton.coordinate.Positioning
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.packet.MovementPacket
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutUpdateEntityPosition(
    override val entityId: Int,
    val deltaX: Short,
    val deltaY: Short,
    val deltaZ: Short,
    override val onGround: Boolean
) : EntityPacket, MovementPacket {

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readShort(), buf.readShort(), buf.readShort(), buf.readBoolean())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeShort(deltaX.toInt())
        buf.writeShort(deltaY.toInt())
        buf.writeShort(deltaZ.toInt())
        buf.writeBoolean(onGround)
    }

    companion object {

        @JvmStatic
        fun create(entityId: Int, oldPos: Position, newPos: Position, onGround: Boolean): PacketOutUpdateEntityPosition {
            val deltaX = Positioning.calculateDelta(newPos.x, oldPos.x)
            val deltaY = Positioning.calculateDelta(newPos.y, oldPos.y)
            val deltaZ = Positioning.calculateDelta(newPos.z, oldPos.z)
            return PacketOutUpdateEntityPosition(entityId, deltaX, deltaY, deltaZ, onGround)
        }
    }
}
