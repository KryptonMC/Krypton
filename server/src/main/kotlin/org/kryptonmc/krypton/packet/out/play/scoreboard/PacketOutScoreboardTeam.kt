package org.kryptonmc.krypton.packet.out.play.scoreboard

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.world.scoreboard.Option
import org.kryptonmc.krypton.api.world.scoreboard.OptionApplication
import org.kryptonmc.krypton.api.world.scoreboard.Team
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Tells the client to perform an action to a team on their current scoreboard
 */
class PacketOutScoreboardTeam(
    private val action: TeamAction,
    private val team: Team,
    private val addedMembers: List<KryptonPlayer> = emptyList(), // only applies for add players
    private val removedMembers: List<KryptonPlayer> = emptyList() // only applies for remove players
) : PlayPacket(0x4C) {

    override fun write(buf: ByteBuf) {
        buf.writeString(team.name, 16)
        buf.writeByte(action.ordinal)

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
        writeByte(team.flagsToProtocol())
        writeString(team.nametagVisibility(), 32)
        writeString(team.collisionRule(), 32)
        writeVarInt(team.color.ordinal)
        writeChat(team.prefix)
        writeChat(team.suffix)
    }

    private fun Team.flagsToProtocol(): Int {
        var byte = 0x0
        if (allowFriendlyFire) byte += 0x01
        if (areInvisibleMembersVisible) byte += 0x02
        return byte
    }

    // These are functions because apparently you cannot use properties from the receiver type
    // in value extension properties.
    private fun Team.nametagVisibility() = when (options[Option.NAMETAG_VISIBILITY]) {
        OptionApplication.ALWAYS -> "always"
        OptionApplication.NEVER -> "never"
        OptionApplication.OWN_TEAM -> "hideForOwnTeam"
        OptionApplication.OTHER_TEAMS -> "hideForOtherTeams"
        else -> "always"
    }

    private fun Team.collisionRule() = when (options[Option.COLLISION_RULE]) {
        OptionApplication.ALWAYS -> "always"
        OptionApplication.NEVER -> "never"
        OptionApplication.OWN_TEAM -> "pushOwnTeam"
        OptionApplication.OTHER_TEAMS -> "pushOtherTeams"
        else -> "always"
    }
}

enum class TeamAction {

    CREATE,
    REMOVE,
    UPDATE_INFO,
    ADD_PLAYERS,
    REMOVE_PLAYERS
}
