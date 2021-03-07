package org.kryptonmc.krypton.packet.out.play.scoreboard

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutScoreboardScore(
    private val entityName: String,
    private val action: ScoreAction,
    private val objectiveName: String,
    private val value: Int = -1
) : PlayPacket(0x4D) {

    override fun write(buf: ByteBuf) {
        buf.writeString(entityName, 40)
        buf.writeByte(action.id)
        buf.writeString(objectiveName, 16)
        if (action != ScoreAction.REMOVE) buf.writeVarInt(value)
    }
}

enum class ScoreAction(val id: Int) {

    CREATE_OR_UPDATE(0),
    REMOVE(1)
}