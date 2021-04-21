package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.util.writePosition
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.palette.GlobalPalette

class PacketOutBlockChange(private val block: KryptonBlock) : PlayPacket(0x0B) {

    override fun write(buf: ByteBuf) {
        buf.writePosition(block.location)
        buf.writeVarInt(GlobalPalette[block.type.key].states.firstOrNull()?.id ?: 0)
    }
}
