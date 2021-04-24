package org.kryptonmc.krypton.packet.out.play.scoreboard

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.world.scoreboard.Score
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Tells the client to perform an action to the score of some entity
 */
class PacketOutScoreboardScore(
    private val score: Score,
    private val action: ScoreAction
) : PlayPacket(0x4D) {

    override fun write(buf: ByteBuf) {
        buf.writeString(score.player.name, 40)
        buf.writeByte(action.ordinal)
        buf.writeString(score.objective.name, 16)
        if (action != ScoreAction.REMOVE) buf.writeVarInt(score.score)
    }
}

enum class ScoreAction {

    CREATE_OR_UPDATE,
    REMOVE
}
