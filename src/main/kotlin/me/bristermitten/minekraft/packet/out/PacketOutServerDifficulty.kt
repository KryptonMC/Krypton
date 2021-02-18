package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.state.PlayPacket
import me.bristermitten.minekraft.world.Difficulty

class PacketOutServerDifficulty(
    val difficulty: Difficulty,
    val isLocked: Boolean = false
) : PlayPacket(0x0D) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(difficulty.id)
        buf.writeBoolean(isLocked)
    }
}