package org.kryptonmc.krypton.packet.out.play.scoreboard

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.world.scoreboard.Objective
import org.kryptonmc.krypton.extension.writeChat
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutScoreboardObjective(
    private val action: ObjectiveAction,
    private val objective: Objective
) : PlayPacket(0x4A) {

    override fun write(buf: ByteBuf) {
        buf.writeString(objective.name, 16)
        buf.writeByte(action.id)
        buf.writeChat(objective.displayName)
        if (action != ObjectiveAction.REMOVE) buf.writeVarInt(objective.renderType.id)
    }
}

enum class ObjectiveAction(val id: Int) {

    CREATE(0),
    REMOVE(1),
    UPDATE_TEXT(2);
}