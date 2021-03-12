package org.kryptonmc.krypton.packet.out.play.scoreboard

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeChat
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.scoreboard.KryptonObjective

class PacketOutScoreboardObjective(
    private val action: ObjectiveAction,
    private val objective: KryptonObjective
) : PlayPacket(0x4A) {

    override fun write(buf: ByteBuf) {
        buf.writeString(objective.name, 16)
        buf.writeByte(action.id)
        if (objective.value != null) buf.writeChat(objective.value)
        if (action != ObjectiveAction.REMOVE) buf.writeVarInt(objective.type.id)
    }
}

enum class ObjectiveAction(val id: Int) {

    CREATE(0),
    REMOVE(1),
    UPDATE_TEXT(2);
}