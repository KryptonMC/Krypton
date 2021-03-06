package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.Difficulty

class PacketOutServerDifficulty(
    private val difficulty: Difficulty,
    private val isLocked: Boolean = true
) : PlayPacket(0x0D) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(difficulty.id)
        buf.writeBoolean(isLocked)
    }
}