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
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.decodeBlockPosition
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readString

class PacketInUpdateStructureBlock(buf: ByteBuf) : Packet {
    val location = buf.readLong().decodeBlockPosition()
    val action = buf.readEnum<UpdateStructureBlockAction>()
    val mode = buf.readEnum<UpdateStructureBlockMode>()
    val name = buf.readString()
    val offsetX = buf.readByte()
    val offsetY = buf.readByte()
    val offsetZ = buf.readByte()
    val sizeX = buf.readByte()
    val sizeY = buf.readByte()
    val sizeZ = buf.readByte()
    val mirror = buf.readEnum<UpdateStructureBlockMirror>()
    val rotation = buf.readEnum<UpdateStructureBlockRotation>()
    val metadata = buf.readString()
    val integrity = buf.readFloat()
    val seed = null // TODO: Requires readVarLong
    val flags = buf.readByte()

    enum class UpdateStructureBlockRotation {
        NONE,
        CLOCKWISE_90,
        CLOCKWISE_180,
        COUNTERCLOCKWISE_90
    }

    enum class UpdateStructureBlockMirror {
        NONE,
        LEFT_RIGHT,
        FRONT_BACK
    }

    enum class UpdateStructureBlockMode {
        SAVE,
        LOAD,
        CORNER,
        DATA
    }

    enum class UpdateStructureBlockAction {
        UPDATE_DATA,
        SAVE_STRUCTURE,
        LOAD_STRUCTURE,
        DETECT_SIZE
    }
}
