package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Declares available crafting recipes. Or, it would if there were recipes to declare.
 */
// TODO: Add some recipes here
class PacketOutDeclareRecipes : PlayPacket(0x5A) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(0)
    }
}
