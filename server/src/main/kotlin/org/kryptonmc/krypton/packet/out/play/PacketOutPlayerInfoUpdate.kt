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
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.network.chat.RemoteChatSession
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.util.enumhelper.GameModes
import java.util.EnumSet
import java.util.UUID

/**
 * Updates information on the tab list (called the player list by vanilla).
 */
@JvmRecord
data class PacketOutPlayerInfoUpdate(val actions: EnumSet<Action>, val entries: List<Entry>) : Packet {

    constructor(actions: EnumSet<Action>, players: Collection<KryptonPlayer>) : this(actions, players.map(::Entry))

    constructor(action: Action, player: KryptonPlayer) : this(EnumSet.of(action), ImmutableLists.of(Entry(player)))

    constructor(reader: BinaryReader) : this(reader, reader.readEnumSet())

    private constructor(reader: BinaryReader, actions: EnumSet<Action>) : this(actions, reader.readList {
        val builder = EntryBuilder(reader.readUUID())
        actions.forEach { action -> action.reader.read(it, builder) }
        builder.build()
    })

    override fun write(writer: BinaryWriter) {
        writer.writeEnumSet(actions)
        writer.writeCollection(entries) { entry ->
            writer.writeUUID(entry.profileId)
            actions.forEach { it.writer.write(writer, entry) }
        }
    }

    enum class Action(internal val reader: Reader, internal val writer: Writer) {

        ADD_PLAYER({ buf, builder ->
            val name = buf.readString()
            require(name.length <= 16) { "Player name too long! Max: 16" }
            builder.profile(KryptonGameProfile.full(builder.profileId, name, buf.readProfileProperties()))
        }, { buf, entry ->
            require(entry.profile.name.length <= 16) { "Player name too long! Max: 16" }
            buf.writeString(entry.profile.name)
            buf.writeProfileProperties(entry.profile.properties)
        }),
        INITIALIZE_CHAT({ buf, builder -> builder.chatSession(buf.readNullable(RemoteChatSession.Data::read)) },
            { buf, entry -> buf.writeNullable(entry.chatSession, RemoteChatSession.Data::write) }),
        UPDATE_GAME_MODE({ buf, builder -> builder.gameMode(GameModes.fromId(buf.readVarInt())!!) },
            { buf, entry -> buf.writeVarInt(entry.gameMode.ordinal) }),
        UPDATE_LISTED({ buf, builder -> builder.listed(buf.readBoolean()) }, { buf, entry -> buf.writeBoolean(entry.listed) }),
        UPDATE_LATENCY({ buf, builder -> builder.latency(buf.readVarInt()) }, { buf, entry -> buf.writeVarInt(entry.latency) }),
        UPDATE_DISPLAY_NAME({ buf, builder -> builder.displayName(buf.readNullable(BinaryReader::readComponent)) },
            { buf, entry -> buf.writeNullable(entry.displayName, BinaryWriter::writeComponent) });

        internal fun interface Reader {

            fun read(reader: BinaryReader, builder: EntryBuilder)
        }

        internal fun interface Writer {

            fun write(writer: BinaryWriter, entry: Entry)
        }

        companion object {

            private val BY_ID = values()

            @JvmStatic
            fun fromId(id: Int): Action? = BY_ID.getOrNull(id)
        }
    }

    @JvmRecord
    data class Entry(val profileId: UUID, val profile: GameProfile, val listed: Boolean, val latency: Int, val gameMode: GameMode,
                     val displayName: Component?, val chatSession: RemoteChatSession.Data?) {

        constructor(player: KryptonPlayer) : this(player.uuid, player.profile, true, player.connection.latency(), player.gameMode, null,
            player.chatSession()?.asData())
    }

    internal class EntryBuilder(val profileId: UUID) {

        private var profile: GameProfile = KryptonGameProfile.partial(profileId)
        private var listed = false
        private var latency = 0
        private var gameMode = GameMode.SURVIVAL
        private var displayName: Component? = null
        private var chatSession: RemoteChatSession.Data? = null

        fun profile(profile: GameProfile): EntryBuilder = apply { this.profile = profile }

        fun listed(value: Boolean): EntryBuilder = apply { listed = value }

        fun latency(value: Int): EntryBuilder = apply { latency = value }

        fun gameMode(mode: GameMode): EntryBuilder = apply { gameMode = mode }

        fun displayName(name: Component?): EntryBuilder = apply { displayName = name }

        fun chatSession(session: RemoteChatSession.Data?): EntryBuilder = apply { chatSession = session }

        fun build(): Entry = Entry(profileId, profile, listed, latency, gameMode, displayName, chatSession)
    }

    companion object {

        @JvmStatic
        fun createPlayerInitializing(players: Collection<KryptonPlayer>): PacketOutPlayerInfoUpdate =
            PacketOutPlayerInfoUpdate(EnumSet.allOf(Action::class.java), players)
    }
}
