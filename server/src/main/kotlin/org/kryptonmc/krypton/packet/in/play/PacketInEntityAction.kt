package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readVarInt

/**
 * Contrary to its name, this is only for players, as it is only sent by clients.
 * This is sent when the player performs one of the actions listed in the [EntityAction] enum.
 */
class PacketInEntityAction : PlayPacket(0x1C) {

    /**
     * The action taken by the entity
     */
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

/**
 * Represents possible entity actions for the client
 */
enum class EntityAction {

    START_SNEAKING,
    STOP_SNEAKING,
    LEAVE_BED,
    START_SPRINTING,
    STOP_SPRINTING,
    START_JUMP_WITH_HORSE,
    STOP_JUMP_WITH_HORSE,
    OPEN_HORSE_INVENTORY,
    START_FLYING_WITH_ELYTRA
}
