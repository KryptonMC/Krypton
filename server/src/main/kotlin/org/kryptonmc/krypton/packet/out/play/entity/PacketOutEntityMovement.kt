package org.kryptonmc.krypton.packet.out.play.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeAngle
import org.kryptonmc.krypton.extension.writeShort
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.space.Angle

/**
 * This is the base class for movement packets. Entity movement is a packet in itself, but its use is
 * flawed. The wiki claims it is used by vanilla to indicate that a player has not moved, but the client
 * assumes this behaviour anyway if it gets no packets indicating otherwise.
 *
 * @param entityId the ID of the entity who moved
 * @param id the packet ID
 *
 * @author Callum Seabrook
 */
sealed class PacketOutEntityMovement(private val entityId: Int, id: Int = 0x2A) : PlayPacket(id) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
    }

    /**
     * Updates the position of the entity with the specified [entityId]
     *
     * Now, this is where the protocol gets a bit whack. Unlike [player position][org.kryptonmc.krypton.packet.in.play.PacketInPlayerMovement.PacketInPlayerPosition],
     * the positions in this are relative, not absolute, which makes for some *very interesting* conversions.
     *
     * @param entityId the ID of the entity who moved
     * @param deltaX the change in X from the entity's current position
     * @param deltaY the change in Y from the entity's current position
     * @param deltaZ the change in Z from the entity's current position
     * @param onGround if the entity is on terra firma (solid ground)
     */
    class PacketOutEntityPosition(
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

    /**
     * Updates the position and rotation of the entity with the specified [entityId]
     *
     * Now, if you thought that [PacketOutEntityPosition] was whack, this one introduces a new concept: [Angle]s.
     * These angles are not standard degrees or radians or any existing unit of measurement, these are measured in
     * 256ths of a full turn. In addition, the [yaw] and [pitch] are absolute, not relative.
     * [Mind blown](https://tenor.com/view/mind-blown-head-explode-amazing-shocking-wow-gif-19681784).
     *
     * @param entityId the ID of the entity who moved
     * @param deltaX the change in X from the entity's current position
     * @param deltaY the change in Y from the entity's current position
     * @param deltaZ the change in Z from the entity's current position
     * @param yaw the new yaw of the entity
     * @param pitch the new pitch of the entity
     * @param onGround if the entity is on terra firma (solid ground)
     */
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

    /**
     * Updates the rotation of the entity with the specified [entityId]
     *
     * @param entityId the ID of the entity who rotated
     * @param yaw the new yaw of the entity
     * @param pitch the new pitch of the entity
     * @param onGround if the entity is on terra firma (solid ground)
     */
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

    /**
     * Updates the head yaw of the entity with the specified [entityId]
     *
     * @param entityId the ID of the entity who rotated their head
     * @param headYaw the new head yaw of the entity
     */
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