/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.krypton.adventure.id
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Tells the client to perform an action to a team on their current scoreboard
 */
@JvmRecord
data class PacketOutTeam(
    val action: Action,
    val name: String,
    val displayName: Component,
    val color: Int,
    val prefix: Component,
    val suffix: Component,
    val allowFriendlyFire: Boolean,
    val canSeeInvisibleMembers: Boolean,
    val nameTagVisibility: String,
    val collisionRule: String,
    val members: Collection<Component>,
    val addedMembers: Collection<Component> = emptySet() // only applies for add/remove players
) : Packet {

    constructor(action: Action, team: Team, addedMembers: Collection<Component> = emptySet()) : this(
        action,
        team.name,
        team.displayName,
        team.color.id(),
        team.prefix,
        team.suffix,
        team.allowFriendlyFire,
        team.canSeeInvisibleMembers,
        team.nameTagVisibility.serialized,
        team.collisionRule.serialized,
        team.members,
        addedMembers
    )

    override fun write(buf: ByteBuf) {
        buf.writeString(name, 16)
        buf.writeByte(action.ordinal)

        when (action) {
            Action.CREATE -> {
                writeTeamInfo(buf)
                buf.writeVarInt(members.size)
                for (member in members) {
                    buf.writeString(member.toLegacySectionText(), 40)
                }
            }
            Action.REMOVE -> Unit
            Action.UPDATE_INFO -> writeTeamInfo(buf)
            Action.ADD_MEMBERS -> {
                buf.writeVarInt(addedMembers.size)
                for (member in addedMembers) {
                    buf.writeString(member.toLegacySectionText(), 40)
                }
            }
            Action.REMOVE_MEMBERS -> {
                buf.writeVarInt(addedMembers.size)
                for (member in addedMembers) {
                    buf.writeString(member.toLegacySectionText(), 40)
                }
            }
        }
    }

    private fun writeTeamInfo(buf: ByteBuf) {
        buf.writeChat(displayName)
        buf.writeByte(flagsToProtocol(allowFriendlyFire, canSeeInvisibleMembers))
        buf.writeString(nameTagVisibility, 32)
        buf.writeString(collisionRule, 32)
        buf.writeVarInt(color)
        buf.writeChat(prefix)
        buf.writeChat(suffix)
    }

    enum class Action {

        CREATE,
        REMOVE,
        UPDATE_INFO,
        ADD_MEMBERS,
        REMOVE_MEMBERS;

        companion object {

            private val BY_ID = values()

            @JvmStatic
            fun fromId(id: Int): Action? = BY_ID.getOrNull(id)
        }
    }

    companion object {

        @JvmStatic
        private fun flagsToProtocol(allowFriendlyFire: Boolean, canSeeInvisibleMembers: Boolean): Int {
            var byte = 0x0
            if (allowFriendlyFire) byte += 0x01
            if (canSeeInvisibleMembers) byte += 0x02
            return byte
        }
    }
}
