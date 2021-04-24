package org.kryptonmc.krypton.packet.out.play.scoreboard

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.world.scoreboard.ScoreboardPosition
import org.kryptonmc.krypton.api.world.scoreboard.Team
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard

/**
 * Tells the client to display the given scoreboard to the user
 */
class PacketOutDisplayScoreboard(
    private val scoreboard: KryptonScoreboard,
    private val team: Team? = null // used for team specific positioning
) : PlayPacket(0x43) {

    override fun write(buf: ByteBuf) {
        when (scoreboard.position) {
            ScoreboardPosition.TEAM_SPECIFIC -> {
                if (team == null) throw IllegalArgumentException("Team must be supplied if position is team specific!")
                buf.writeByte(3 + team.color.ordinal)
            }
            else -> buf.writeByte(scoreboard.position.id)
        }
        buf.writeString(scoreboard.name, 16)
    }
}
