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
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sent when the client moves. Not actually sure what this base packet is used for, but we don't
 * use it in Krypton, it's only used as a base packet.
 */
open class PacketInPlayerMovement(id: Int = 0x15) : PlayPacket(id) {

    /**
     * If the client is on terra firma
     */
    var onGround: Boolean = false; private set

    override fun read(buf: ByteBuf) {
        onGround = buf.readBoolean()
    }

    /**
     * Sent when the player changes their position without changing their rotation
     */
    class PacketInPlayerPosition : PacketInPlayerMovement(0x12) {

        /**
         * The client's absolute X coordinate
         */
        var x = 0.0; private set

        /**
         * The client's absolute Y coordinate
         */
        var y = 0.0; private set

        /**
         * The client's absolute Z coordinate
         */
        var z = 0.0; private set

        override fun read(buf: ByteBuf) {
            x = buf.readDouble()
            y = buf.readDouble()
            z = buf.readDouble()

            super.read(buf)
        }
    }

    /**
     * Sent when the player changes their rotation without changing their position
     */
    class PacketInPlayerRotation : PacketInPlayerMovement(0x14) {

        /**
         * The client's current yaw (rotation on X axis), as an angle measured in degrees
         */
        var yaw = 0F; private set

        /**
         * The client's current pitch (rotation on Y axis), as an angle measured in degrees
         */
        var pitch = 0F; private set

        override fun read(buf: ByteBuf) {
            yaw = buf.readFloat()
            pitch = buf.readFloat()

            super.read(buf)
        }
    }

    /**
     * Sent when the player changes both their rotation and their position
     */
    class PacketInPlayerPositionAndRotation : PacketInPlayerMovement(0x13) {

        /**
         * The client's absolute X position
         */
        var x = 0.0; private set

        /**
         * The client's absolute X position
         */
        var y = 0.0; private set

        /**
         * The client's absolute X position
         */
        var z = 0.0; private set

        /**
         * The client's current yaw (rotation on X axis), as an angle measured in degrees
         */
        var yaw = 0F; private set

        /**
         * The client's current pitch (rotation on Y axis), as an angle measured in degrees
         */
        var pitch = 0F; private set

        override fun read(buf: ByteBuf) {
            x = buf.readDouble()
            y = buf.readDouble()
            z = buf.readDouble()
            yaw = buf.readFloat()
            pitch = buf.readFloat()

            super.read(buf)
        }
    }
}
