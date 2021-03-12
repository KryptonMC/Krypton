package org.kryptonmc.krypton.packet.out.play.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeAngle
import org.kryptonmc.krypton.extension.writeShort
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.space.Angle

open class PacketOutEntityMovement(private val entityId: Int, id: Int = 0x2A) : PlayPacket(id) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
    }

    open class PacketOutEntityPosition(
        entityId: Int,
        private val deltaX: Short,
        private val deltaY: Short,
        private val deltaZ: Short,
        private val onGround: Boolean
    ) : PacketOutEntityMovement(entityId, 0x27) {

        override fun write(buf: ByteBuf) {
            super.write(buf)

            buf.writeShort(deltaX)
            buf.writeShort(deltaY)
            buf.writeShort(deltaZ)
            buf.writeBoolean(onGround)
        }
    }

    class PacketOutEntityPositionAndRotation(
        entityId: Int,
        private val deltaX: Short,
        private val deltaY: Short,
        private val deltaZ: Short,
        private val yaw: Angle,
        private val pitch: Angle,
        private val onGround: Boolean
    ) : PacketOutEntityMovement(entityId, 0x28) {

        override fun write(buf: ByteBuf) {
            super.write(buf)

            buf.writeShort(deltaX)
            buf.writeShort(deltaY)
            buf.writeShort(deltaZ)
            buf.writeAngle(yaw)
            buf.writeAngle(pitch)
            buf.writeBoolean(onGround)
        }
    }

    class PacketOutEntityRotation(
        entityId: Int,
        private val yaw: Angle,
        private val pitch: Angle,
        private val onGround: Boolean
    ) : PacketOutEntityMovement(entityId, 0x29) {

        override fun write(buf: ByteBuf) {
            super.write(buf)

            buf.writeAngle(yaw)
            buf.writeAngle(pitch)
            buf.writeBoolean(onGround)
        }
    }

    class PacketOutEntityHeadLook(
        entityId: Int,
        private val headYaw: Angle
    ) : PacketOutEntityMovement(entityId, 0x3A) {

        override fun write(buf: ByteBuf) {
            super.write(buf)

            buf.writeAngle(headYaw)
        }
    }
}