package org.kryptonmc.krypton.packet.out.play.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeUByte
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Sent when some form of animation should be played by the client for a specific entity it knows about.
 * Possible values are in [EntityAnimation].
 *
 * @param entityId the ID of the entity who we want the client to play an animation for
 * @param animation the animation we want played
 */
class PacketOutEntityAnimation(
    private val entityId: Int,
    private val animation: EntityAnimation
) : PlayPacket(0x05) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeUByte(animation.ordinal.toUByte())
    }
}

enum class EntityAnimation {

    SWING_MAIN_ARM,
    TAKE_DAMAGE,
    LEAVE_BED,
    SWING_OFFHAND,
    CRITICAL_EFFECT,
    MAGIC_CRITICAL_EFFECT
}
