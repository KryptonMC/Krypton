package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.block.Block
import org.kryptonmc.krypton.packet.`in`.play.DiggingStatus
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVector
import org.spongepowered.math.vector.Vector3i

class PacketOutAcknowledgeDigging(
    private val position: Vector3i,
    private val block: Block,
    private val status: DiggingStatus,
    private val successful: Boolean
) : PlayPacket(0x08) {

    override fun write(buf: ByteBuf) {
        buf.writeVector(position)
        buf.writeVarInt(block.stateId)
        buf.writeEnum(status)
        buf.writeBoolean(successful)
    }
}
