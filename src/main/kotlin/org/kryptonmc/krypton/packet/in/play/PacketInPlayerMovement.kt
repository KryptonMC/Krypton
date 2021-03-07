package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.Location

open class PacketInPlayerMovement(id: Int = 0x15) : PlayPacket(id) {

    var onGround: Boolean = false
        private set

    override fun read(buf: ByteBuf) {
        onGround = buf.readBoolean()
    }

    class PacketInPlayerPosition : PacketInPlayerMovement(0x12) {

        var x = 0.0
            private set
        var y = 0.0
            private set
        var z = 0.0
            private set

        override fun read(buf: ByteBuf) {
            x = buf.readDouble()
            y = buf.readDouble()
            z = buf.readDouble()

            super.read(buf)
        }
    }

    class PacketInPlayerRotation : PacketInPlayerMovement(0x14) {

        var yaw = 0.0f
            private set
        var pitch = 0.0f
            private set

        override fun read(buf: ByteBuf) {
            yaw = buf.readFloat()
            pitch = buf.readFloat()

            super.read(buf)
        }
    }

    class PacketInPlayerPositionAndRotation : PacketInPlayerMovement(0x13) {

        var x = 0.0
            private set
        var y = 0.0
            private set
        var z = 0.0
            private set
        var yaw = 0.0f
            private set
        var pitch = 0.0f
            private set

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