package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.effect.Effect
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeVector
import org.spongepowered.math.vector.Vector3i

class PacketOutEffect(
    private val effect: Effect,
    private val position: Vector3i,
    private val data: Int,
    private val isGlobal: Boolean
) : PlayPacket(0x23) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(effect.id)
        buf.writeVector(position)
        buf.writeInt(data)
        buf.writeBoolean(isGlobal)
    }
}
