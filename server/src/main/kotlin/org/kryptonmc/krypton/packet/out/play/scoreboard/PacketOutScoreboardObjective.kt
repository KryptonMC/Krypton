package org.kryptonmc.krypton.packet.out.play.scoreboard

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.world.scoreboard.Objective
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Tells the client to perform an action to an objective for a scoreboard
 */
class PacketOutScoreboardObjective(
    private val action: ObjectiveAction,
    private val objective: Objective
) : PlayPacket(0x4A) {

    override fun write(buf: ByteBuf) {
        buf.writeString(objective.name, 16)
        buf.writeByte(action.id)
        buf.writeChat(objective.displayName)
        if (action != ObjectiveAction.REMOVE) buf.writeVarInt(objective.renderType.ordinal)
    }
}

enum class ObjectiveAction(val id: Int) {

    CREATE(0),
    REMOVE(1),
    UPDATE_TEXT(2);
}
