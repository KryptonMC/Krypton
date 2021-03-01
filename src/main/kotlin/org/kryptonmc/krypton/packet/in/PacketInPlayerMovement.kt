package org.kryptonmc.krypton.packet.`in`

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

        lateinit var location: Location
            private set

        override fun read(buf: ByteBuf) {
            val x = buf.readDouble()
            val y = buf.readDouble()
            val z = buf.readDouble()
            location = Location(x, y, z)

            super.read(buf)
        }
    }

    class PacketInPlayerRotation : PacketInPlayerMovement(0x14) {

        var yaw: Float = 0.0f
            private set

        var pitch: Float = 0.0f
            private set

        override fun read(buf: ByteBuf) {
            yaw = buf.readFloat()
            pitch = buf.readFloat()

            super.read(buf)
        }
    }

    class PacketInPlayerPositionAndRotation : PacketInPlayerMovement(0x13) {

        lateinit var location: Location
            private set

        override fun read(buf: ByteBuf) {
            val x = buf.readDouble()
            val y = buf.readDouble()
            val z = buf.readDouble()
            val yaw = buf.readFloat()
            val pitch = buf.readFloat()
            location = Location(x, y, z, yaw, pitch)

            super.read(buf)
        }
    }
}