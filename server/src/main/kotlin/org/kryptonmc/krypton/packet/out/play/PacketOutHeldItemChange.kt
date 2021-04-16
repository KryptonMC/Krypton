package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Tells the client to change the item it's currently holding to the specified [slot]
 *
 * @param slot the slot to change to
 *
 * @author Callum Seabrook
 */
class PacketOutHeldItemChange(private val slot: Int) : PlayPacket(0x3F) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(slot)
    }
}