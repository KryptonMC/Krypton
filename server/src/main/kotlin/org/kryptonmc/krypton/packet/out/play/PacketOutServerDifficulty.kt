package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.world.Difficulty
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sets the current difficulty of the server. Note that the difficulty is always locked in multiplayer.
 *
 * @param difficulty the difficulty of the server
 * @param isLocked if the difficulty is locked. Should always be true in multiplayer
 */
class PacketOutServerDifficulty(
    private val difficulty: Difficulty,
    private val isLocked: Boolean = true
) : PlayPacket(0x0D) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(difficulty.ordinal)
        buf.writeBoolean(isLocked)
    }
}
