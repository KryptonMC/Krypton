package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.extension.writeVarLong
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.WorldBorder

class PacketOutWorldBorder(
    val action: BorderAction,
    val border: WorldBorder
) : PlayPacket(0x3D) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(action.id)

        buf.writeDouble(border.centerX)
        buf.writeDouble(border.centerZ)
        buf.writeDouble(border.size)
        buf.writeDouble(border.size)
        buf.writeVarLong(0)
        buf.writeVarInt(29999984) // portal teleport boundary - generally this value
        buf.writeVarInt(border.warningBlocks.toInt())
        buf.writeVarInt(border.warningTime.toInt())
    }
}

enum class BorderAction(val id: Int) {

    INITIALIZE(3)
}