package org.kryptonmc.krypton.packet.out.play.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeUByte
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sent when some form of animation should be played by the client for a specific entity it knows about.
 * Possible values are in [EntityAnimation].
 *
 * @param entityId the ID of the entity who we want the client to play an animation for
 * @param animation the animation we want played
 *
 * @author Callum Seabrook
 */
class PacketOutEntityAnimation(
    private val entityId: Int,
    private val animation: EntityAnimation
) : PlayPacket(0x05) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeUByte(animation.id)
    }
}

enum class EntityAnimation(val id: UByte) {

    SWING_MAIN_ARM(0u),
    TAKE_DAMAGE(1u),
    LEAVE_BED(2u),
    SWING_OFFHAND(3u),
    CRITICAL_EFFECT(4u),
    MAGIC_CRITICAL_EFFECT(5u)
}