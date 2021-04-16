package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Declares available crafting recipes. Or, it would if there were recipes to declare.
 *
 * @author Callum Seabrook
 */
// TODO: Add some recipes here
class PacketOutDeclareRecipes : PlayPacket(0x5A) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(0)
    }
}