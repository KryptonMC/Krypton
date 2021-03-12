package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketInEntityAction : PlayPacket(0x1C) {

    var entityId = -1
        private set

    lateinit var action: EntityAction
        private set

    var jumpBoost = -1
        private set

    override fun read(buf: ByteBuf) {
        entityId = buf.readVarInt()
        action = EntityAction.fromId(buf.readVarInt())
        jumpBoost = buf.readVarInt()
    }
}

enum class EntityAction(val id: Int) {

    START_SNEAKING(0),
    STOP_SNEAKING(1),
    LEAVE_BED(2),
    START_SPRINTING(3),
    STOP_SPRINTING(4),
    START_JUMP_WITH_HORSE(5),
    STOP_JUMP_WITH_HORSE(6),
    OPEN_HORSE_INVENTORY(7),
    START_FLYING_WITH_ELYTRA(8);

    companion object {

        private val VALUES = values().associateBy { it.id }

        fun fromId(id: Int) = VALUES.getValue(id)
    }
}