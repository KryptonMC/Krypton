/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.world.scoreboard.Option
import org.kryptonmc.api.world.scoreboard.OptionApplication
import org.kryptonmc.api.world.scoreboard.Team
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Tells the client to perform an action to a team on their current scoreboard
 */
@JvmRecord
data class PacketOutTeams(
    private val action: TeamAction,
    private val team: Team,
    private val addedMembers: List<KryptonPlayer> = emptyList(), // only applies for add players
    private val removedMembers: List<KryptonPlayer> = emptyList() // only applies for remove players
) : Packet {

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
        if (canSeeInvisibleMembers) byte += 0x02
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
