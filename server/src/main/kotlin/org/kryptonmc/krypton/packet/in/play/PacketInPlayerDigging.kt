package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.space.Vector
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.toVector
import org.kryptonmc.krypton.world.block.BlockFace

class PacketInPlayerDigging : PlayPacket(0x1B) {

    lateinit var status: DiggingStatus private set
    lateinit var location: Vector private set
    lateinit var face: BlockFace private set

    override fun read(buf: ByteBuf) {
        status = buf.readEnum()
        location = buf.readLong().toVector()
        face = BlockFace.fromId(buf.readVarInt())
    }
}

enum class DiggingStatus {

    STARTED,
    CANCELLED,
    FINISHED,
    DROP_ITEM_STACK,
    DROP_ITEM,
    UPDATE_STATE,
    SWAP_ITEM_IN_HAND
}
