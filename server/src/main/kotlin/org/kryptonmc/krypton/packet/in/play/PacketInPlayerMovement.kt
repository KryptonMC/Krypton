package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sent when the client moves. Not actually sure what this base packet is used for, but we don't
 * use it in Krypton, it's only used as a base packet.
 *
 * @author Callum Seabrook
 */
open class PacketInPlayerMovement(id: Int = 0x15) : PlayPacket(id) {

    var onGround: Boolean = false; private set

    override fun read(buf: ByteBuf) {
        onGround = buf.readBoolean()
    }

    /**
     * Sent when the player changes their position without changing their rotation
     */
    class PacketInPlayerPosition : PacketInPlayerMovement(0x12) {

        var x = 0.0; private set
        var y = 0.0; private set
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

        var yaw = 0F; private set
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

        var x = 0.0; private set
        var y = 0.0; private set
        var z = 0.0; private set
        var yaw = 0F; private set
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