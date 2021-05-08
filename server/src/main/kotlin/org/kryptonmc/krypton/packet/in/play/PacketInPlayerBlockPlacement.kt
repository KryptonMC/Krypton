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
import org.kryptonmc.krypton.api.entity.Hand
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.toVector
import org.kryptonmc.krypton.world.block.BlockFace

/**
 * Sent to indicate the player has placed a block.
 */
class PacketInPlayerBlockPlacement : PlayPacket(0x2E) {

    /**
     * The hand the player used to place the block
     */
    lateinit var hand: Hand private set

    /**
     * The location of the __block__ being placed
     */
    lateinit var location: Vector private set

    /**
     * The face of the block that has been placed that is facing the player
     */
    lateinit var face: BlockFace private set

    /**
     * The X, Y and Z positions of the crosshair on the block, from 0 to 1 increasing:
     * - West to east for X
     * - Bottom to top for Y
     * - North to south for Z
     */
    var cursorX = 0F; private set
    var cursorY = 0F; private set
    var cursorZ = 0F; private set

    /**
     * Whether the player's head is inside a block
     */
    var insideBlock = false; private set

    override fun read(buf: ByteBuf) {
        hand = buf.readEnum()
        location = buf.readLong().toVector()
        face = BlockFace.fromId(buf.readVarInt())
        cursorX = buf.readFloat()
        cursorY = buf.readFloat()
        cursorZ = buf.readFloat()
        insideBlock = buf.readBoolean()
    }
}
