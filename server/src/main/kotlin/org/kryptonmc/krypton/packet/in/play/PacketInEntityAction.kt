package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readEnum
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Contrary to its name, this is only for players, as it is only sent by clients.
 * This is sent when the player performs one of the actions listed in the [EntityAction] enum.
 *
 * @author Callum Seabrook
 */
class PacketInEntityAction : PlayPacket(0x1C) {

    lateinit var action: EntityAction private set

    /**
     * This is a bit magic. It's the jump boost modifier, only used with the
     * [start jump with horse][EntityAction.START_JUMP_WITH_HORSE] action.
     */
    var data = -1; private set

    override fun read(buf: ByteBuf) {
        buf.readVarInt() // we already know the entity ID because we know where this came from, smh Mojang.
        action = buf.readEnum(EntityAction::class)
        data = buf.readVarInt()
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
    START_FLYING_WITH_ELYTRA(8)
}