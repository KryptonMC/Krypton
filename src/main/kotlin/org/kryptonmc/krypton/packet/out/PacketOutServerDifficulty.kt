package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.Difficulty

class PacketOutServerDifficulty(
    val difficulty: Difficulty,
    val isLocked: Boolean = false
) : PlayPacket(0x0D) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(difficulty.id)
        buf.writeBoolean(isLocked)
    }
}