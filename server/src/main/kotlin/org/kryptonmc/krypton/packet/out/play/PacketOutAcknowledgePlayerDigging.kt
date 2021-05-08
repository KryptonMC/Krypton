package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.space.Position
import org.kryptonmc.krypton.packet.`in`.play.DiggingStatus
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.toProtocol
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutAcknowledgePlayerDigging(
    private val location: Position,
    private val stateId: Int,
    private val status: DiggingStatus,
    private val successful: Boolean
) : PlayPacket(0x07) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(location.toProtocol())
        buf.writeVarInt(stateId)
        buf.writeVarInt(status.ordinal)
        buf.writeBoolean(successful)
    }
}
