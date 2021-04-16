package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.extension.writeVarLong
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.KryptonWorldBorder

/**
 * Sets the world border for the world. The only action supported for this at the moment is [INITIALIZE][BorderAction.INITIALIZE]
 *
 * @param action the action for this packet
 * @param border the world border
 *
 * @author Callum Seabrook
 */
class PacketOutWorldBorder(
    private val action: BorderAction,
    private val border: KryptonWorldBorder
) : PlayPacket(0x3D) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(action.id)

        buf.writeDouble(border.center.x)
        buf.writeDouble(border.center.z)
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