package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.entity.Hand
import org.kryptonmc.krypton.extension.readEnum
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sent to indicate that the client swung their arm. I know, you would expect this to do more but no,
 * it really doesn't.
 *
 * @author Callum Seabrook
 */
class PacketInAnimation : PlayPacket(0x2C) {

    lateinit var hand: Hand private set

    override fun read(buf: ByteBuf) {
        hand = buf.readEnum(Hand::class)
    }
}