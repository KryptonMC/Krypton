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

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.krypton.adventure.KryptonAdventure
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.Packet

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

    init {
        require(name.length <= MAX_NAME_LENGTH) { "Team name too long! Max: $MAX_NAME_LENGTH" }
    }

    constructor(reader: BinaryReader) : this(reader, reader.readString(), Action.fromId(reader.readByte().toInt())!!)

    private constructor(reader: BinaryReader, name: String, action: Action) : this(name, action, readParameters(reader, action),
        readMembers(reader, action))

    override fun write(writer: BinaryWriter) {
        writer.writeString(name)
        writer.writeByte(action.ordinal.toByte())
        if (action == Action.CREATE || action == Action.UPDATE_INFO) {
            requireNotNull(parameters) { "Parameters must be present if action is CREATE or UPDATE_INFO!" }.write(writer)
        }
        if (action == Action.CREATE || action == Action.ADD_MEMBERS || action == Action.REMOVE_MEMBERS) {
            writer.writeCollection(members) { writer.writeString(LegacyComponentSerializer.legacySection().serialize(it)) }
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
        val options: Byte,
        val nameTagVisibility: String,
        val collisionRule: String,
        val color: Int,
        val prefix: Component,
        val suffix: Component
    ) : Writable {

        init {
            require(nameTagVisibility.length <= MAX_VISIBILITY_LENGTH) { "Name tag visibility too long! Max: $MAX_VISIBILITY_LENGTH" }
            require(collisionRule.length <= MAX_VISIBILITY_LENGTH) { "Collision rule too long! Max: $MAX_VISIBILITY_LENGTH" }
        }

        constructor(team: Team) : this(team.displayName, encodeOptions(team), team.nameTagVisibility.name.lowercase(),
            team.collisionRule.name.lowercase(), KryptonAdventure.getColorId(team.color), team.prefix, team.suffix)

        constructor(reader: BinaryReader) : this(reader.readComponent(), reader.readByte(), reader.readString(), reader.readString(),
            reader.readVarInt(), reader.readComponent(), reader.readComponent())

        override fun write(writer: BinaryWriter) {
            writer.writeComponent(displayName)
            writer.writeByte(options)
            writer.writeString(nameTagVisibility)
            writer.writeString(collisionRule)
            writer.writeVarInt(color)
            writer.writeComponent(prefix)
            writer.writeComponent(suffix)
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
        fun addOrRemoveMember(team: Team, member: Component, add: Boolean): PacketOutUpdateTeams {
            return PacketOutUpdateTeams(team.name, if (add) Action.ADD_MEMBERS else Action.REMOVE_MEMBERS, null, listOf(member))
        }

        @JvmStatic
        private fun encodeOptions(team: Team): Byte {
            var options = 0
            if (team.allowFriendlyFire) options = options or FLAG_FRIENDLY_FIRE
            if (team.canSeeInvisibleMembers) options = options or FLAG_SEE_INVISIBLES
            return options.toByte()
        }

        @JvmStatic
        private fun readParameters(reader: BinaryReader, action: Action): Parameters? {
            return if (action == Action.CREATE || action == Action.UPDATE_INFO) Parameters(reader) else null
        }

        @JvmStatic
        private fun readMembers(reader: BinaryReader, action: Action): Collection<Component> {
            if (action == Action.REMOVE || action == Action.UPDATE_INFO) return emptyList()
            return reader.readList { LegacyComponentSerializer.legacySection().deserialize(it.readString()) }
        }
    }
}
