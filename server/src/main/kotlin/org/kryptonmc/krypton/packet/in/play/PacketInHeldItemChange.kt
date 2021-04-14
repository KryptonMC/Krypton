package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sent when the player changes the item that they are currently holding in their hand, such
 * as when the player scrolls across their hotbar, presses one of the number keys, or switches
 * out the item they have in their hand.
 *
 * @author Callum Seabrook
 */
class PacketInHeldItemChange : PlayPacket(0x25) {

    /**
     * The slot of the newly held item.
     */
    var slot: Short = 0; private set

    override fun read(buf: ByteBuf) {
        slot = buf.readShort()
    }
}