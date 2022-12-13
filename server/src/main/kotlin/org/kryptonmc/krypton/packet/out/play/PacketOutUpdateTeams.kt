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
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.krypton.adventure.KryptonAdventure
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Tells the client to perform an action to a team on their current scoreboard.
 */
@JvmRecord
data class PacketOutUpdateTeams(
    val name: String,
    val action: Action,
    val parameters: Parameters?,
    val members: Collection<Component>
) : Packet {

    override fun write(buf: ByteBuf) {
        buf.writeString(name, MAX_NAME_LENGTH)
        buf.writeByte(action.ordinal)
        if (action == Action.CREATE || action == Action.UPDATE_INFO) {
            requireNotNull(parameters) { "Parameters must be present if action is CREATE or UPDATE_INFO!" }.write(buf)
        }
        if (action == Action.CREATE || action == Action.ADD_MEMBERS || action == Action.REMOVE_MEMBERS) {
            buf.writeCollection(members) { buf.writeString(LegacyComponentSerializer.legacySection().serialize(it)) }
        }
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

    @JvmRecord
    data class Parameters(
        val displayName: Component,
        val options: Int,
        val nameTagVisibility: String,
        val collisionRule: String,
        val color: NamedTextColor,
        val prefix: Component,
        val suffix: Component
    ) : Writable {

        constructor(team: Team) : this(team.displayName, encodeOptions(team), team.nameTagVisibility.name.lowercase(),
            team.collisionRule.name.lowercase(), team.color, team.prefix, team.suffix)

        constructor(buf: ByteBuf) : this(buf.readComponent(), buf.readByte().toInt(), buf.readString(MAX_VISIBILITY_LENGTH),
            buf.readString(MAX_VISIBILITY_LENGTH), KryptonAdventure.colorFromId(buf.readVarInt()), buf.readComponent(), buf.readComponent())

        override fun write(buf: ByteBuf) {
            buf.writeComponent(displayName)
            buf.writeByte(options)
            buf.writeString(nameTagVisibility, MAX_VISIBILITY_LENGTH)
            buf.writeString(collisionRule, MAX_VISIBILITY_LENGTH)
            buf.writeVarInt(KryptonAdventure.colorId(color))
            buf.writeComponent(prefix)
            buf.writeComponent(suffix)
        }
    }

    companion object {

        private const val FLAG_FRIENDLY_FIRE = 1
        private const val FLAG_SEE_INVISIBLES = 2
        private const val MAX_NAME_LENGTH = 16
        private const val MAX_VISIBILITY_LENGTH = 40

        @JvmStatic
        fun create(team: Team): PacketOutUpdateTeams = createOrUpdate(team, true)

        @JvmStatic
        fun update(team: Team): PacketOutUpdateTeams = createOrUpdate(team, false)

        @JvmStatic
        private fun createOrUpdate(team: Team, create: Boolean): PacketOutUpdateTeams {
            val action = if (create) Action.CREATE else Action.UPDATE_INFO
            val members = if (create) team.members else emptySet()
            return PacketOutUpdateTeams(team.name, action, Parameters(team), members)
        }

        @JvmStatic
        fun remove(team: Team): PacketOutUpdateTeams = PacketOutUpdateTeams(team.name, Action.REMOVE, null, emptySet())

        @JvmStatic
        fun addOrRemoveMember(team: Team, member: Component, add: Boolean): PacketOutUpdateTeams =
            PacketOutUpdateTeams(team.name, if (add) Action.ADD_MEMBERS else Action.REMOVE_MEMBERS, null, listOf(member))

        @JvmStatic
        private fun encodeOptions(team: Team): Int {
            var options = 0
            if (team.allowFriendlyFire) options = options or FLAG_FRIENDLY_FIRE
            if (team.canSeeInvisibleMembers) options = options or FLAG_SEE_INVISIBLES
            return options
        }
    }
}
