package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVector
import org.spongepowered.math.vector.Vector3i

class PacketOutBreakAnimation(
    private val id: Int,
    private val position: Vector3i,
    private val state: Int
) : PlayPacket(0x09) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(id)
        buf.writeVector(position)
        buf.writeByte(state)
    }
}
