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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.toLegacySectionText
import org.kryptonmc.api.world.scoreboard.Team
import org.kryptonmc.krypton.adventure.ordinal
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.scoreboard.KryptonCollisionRule
import org.kryptonmc.krypton.world.scoreboard.KryptonVisibility

/**
 * Tells the client to perform an action to a team on their current scoreboard
 */
@JvmRecord
data class PacketOutTeams(
    private val action: Action,
    private val team: Team,
    private val addedMembers: Set<Component> = emptySet(), // only applies for add players
    private val removedMembers: Set<Component> = emptySet() // only applies for remove players
) : Packet {

    override fun write(buf: ByteBuf) {
        buf.writeString(team.name, 16)
        buf.writeByte(action.ordinal)

        when (action) {
            Action.CREATE -> {
                buf.writeTeamInfo()
                buf.writeVarInt(team.members.size)
                for (member in team.members) buf.writeString(member.toLegacySectionText(), 40)
            }
            Action.REMOVE -> Unit
            Action.UPDATE_INFO -> buf.writeTeamInfo()
            Action.ADD_PLAYERS -> {
                buf.writeVarInt(addedMembers.size)
                for (member in addedMembers) buf.writeString(member.toLegacySectionText(), 40)
            }
            Action.REMOVE_PLAYERS -> {
                buf.writeVarInt(removedMembers.size)
                for (member in removedMembers) buf.writeString(member.toLegacySectionText(), 40)
            }
        }
    }

    private fun ByteBuf.writeTeamInfo() {
        writeChat(team.displayName)
        writeByte(team.flagsToProtocol())
        writeString((team.nameTagVisibility as KryptonVisibility).name, 32)
        writeString((team.collisionRule as KryptonCollisionRule).name, 32)
        writeVarInt(team.color.ordinal())
        writeChat(team.prefix)
        writeChat(team.suffix)
    }

    private fun Team.flagsToProtocol(): Int {
        var byte = 0x0
        if (allowFriendlyFire) byte += 0x01
        if (canSeeInvisibleMembers) byte += 0x02
        return byte
    }

    enum class Action {

        CREATE,
        REMOVE,
        UPDATE_INFO,
        ADD_PLAYERS,
        REMOVE_PLAYERS
    }
}
