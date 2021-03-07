package org.kryptonmc.krypton.packet.out.play.scoreboard

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.entities.Player
import org.kryptonmc.krypton.extension.writeChat
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.scoreboard.ScoreboardTeam

class PacketOutScoreboardTeam(
    private val action: TeamAction,
    private val team: ScoreboardTeam,
    private val addedMembers: List<Player> = emptyList(), // only applies for add players
    private val removedMembers: List<Player> = emptyList() // only applies for remove players
) : PlayPacket(0x4C) {

    override fun write(buf: ByteBuf) {
        buf.writeString(team.name, 16)
        buf.writeByte(action.id)

        when (action) {
            TeamAction.CREATE -> {
                buf.writeTeamInfo()
                buf.writeVarInt(team.members.size)
                for (member in team.members) buf.writeString(member.name, 40)
            }
            TeamAction.REMOVE -> Unit
            TeamAction.UPDATE_INFO -> buf.writeTeamInfo()
            TeamAction.ADD_PLAYERS -> {
                buf.writeVarInt(addedMembers.size)
                for (member in addedMembers) buf.writeString(member.name, 40)
            }
            TeamAction.REMOVE_PLAYERS -> {
                buf.writeVarInt(removedMembers.size)
                for (member in removedMembers) buf.writeString(member.name, 40)
            }
        }
    }

    private fun ByteBuf.writeTeamInfo() {
        writeChat(team.displayName)
        writeByte(team.flags.toProtocol())
        writeString(team.nametagVisibility.id, 32)
        writeString(team.collisionRule.id, 32)
        writeVarInt(team.color.id)
        writeChat(team.prefix)
        writeChat(team.suffix)
    }
}

enum class TeamAction(val id: Int) {

    CREATE(0),
    REMOVE(1),
    UPDATE_INFO(2),
    ADD_PLAYERS(3),
    REMOVE_PLAYERS(4)
}