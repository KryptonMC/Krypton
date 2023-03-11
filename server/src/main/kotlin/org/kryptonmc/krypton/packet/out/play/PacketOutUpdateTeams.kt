/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.kryptonmc.krypton.util.readList
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

    constructor(buf: ByteBuf) : this(buf, buf.readString(MAX_NAME_LENGTH), Action.fromId(buf.readByte().toInt())!!)

    private constructor(buf: ByteBuf, name: String, action: Action) : this(name, action, readParameters(buf, action), readMembers(buf, action))

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
            buf.readString(MAX_VISIBILITY_LENGTH), KryptonAdventure.getColorFromId(buf.readVarInt()), buf.readComponent(), buf.readComponent())

        override fun write(buf: ByteBuf) {
            buf.writeComponent(displayName)
            buf.writeByte(options)
            buf.writeString(nameTagVisibility, MAX_VISIBILITY_LENGTH)
            buf.writeString(collisionRule, MAX_VISIBILITY_LENGTH)
            buf.writeVarInt(KryptonAdventure.getColorId(color))
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

        @JvmStatic
        private fun readParameters(buf: ByteBuf, action: Action): Parameters? =
            if (action == Action.CREATE || action == Action.UPDATE_INFO) Parameters(buf) else null

        @JvmStatic
        private fun readMembers(buf: ByteBuf, action: Action): Collection<Component> {
            if (action == Action.REMOVE || action == Action.UPDATE_INFO) return emptyList()
            return buf.readList { LegacyComponentSerializer.legacySection().deserialize(it.readString()) }
        }
    }
}
