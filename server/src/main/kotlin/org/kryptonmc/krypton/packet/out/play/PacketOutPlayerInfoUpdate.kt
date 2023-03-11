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
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.chat.RemoteChatSession
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.util.enumhelper.GameModes
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readEnumSet
import org.kryptonmc.krypton.util.readList
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readProfileProperties
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeEnumSet
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeProfileProperties
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import java.util.EnumSet
import java.util.UUID

/**
 * Updates information on the tab list (called the player list by vanilla).
 */
@JvmRecord
data class PacketOutPlayerInfoUpdate(val actions: EnumSet<Action>, val entries: List<Entry>) : Packet {

    constructor(actions: EnumSet<Action>, players: Collection<KryptonPlayer>) : this(actions, players.map(::Entry))

    constructor(action: Action, player: KryptonPlayer) : this(EnumSet.of(action), ImmutableLists.of(Entry(player)))

    constructor(buf: ByteBuf) : this(buf, buf.readEnumSet())

    private constructor(buf: ByteBuf, actions: EnumSet<Action>) : this(actions, buf.readList {
        val builder = EntryBuilder(it.readUUID())
        actions.forEach { action -> action.reader.read(it, builder) }
        builder.build()
    })

    override fun write(buf: ByteBuf) {
        buf.writeEnumSet(actions)
        buf.writeCollection(entries) { entry ->
            buf.writeUUID(entry.profileId)
            actions.forEach { it.writer.write(buf, entry) }
        }
    }

    enum class Action(internal val reader: Reader, internal val writer: Writer) {

        ADD_PLAYER({ buf, builder ->
            builder.profile(KryptonGameProfile.full(builder.profileId, buf.readString(16), buf.readProfileProperties()))
        }, { buf, entry ->
            buf.writeString(entry.profile.name, 16)
            buf.writeProfileProperties(entry.profile.properties)
        }),
        INITIALIZE_CHAT({ buf, builder -> builder.chatSession(buf.readNullable(RemoteChatSession.Data::read)) },
            { buf, entry -> buf.writeNullable(entry.chatSession, RemoteChatSession.Data::write) }),
        UPDATE_GAME_MODE({ buf, builder -> builder.gameMode(GameModes.fromId(buf.readVarInt())!!) },
            { buf, entry -> buf.writeVarInt(entry.gameMode.ordinal) }),
        UPDATE_LISTED({ buf, builder -> builder.listed(buf.readBoolean()) }, { buf, entry -> buf.writeBoolean(entry.listed) }),
        UPDATE_LATENCY({ buf, builder -> builder.latency(buf.readVarInt()) }, { buf, entry -> buf.writeVarInt(entry.latency) }),
        UPDATE_DISPLAY_NAME({ buf, builder -> builder.displayName(buf.readNullable(ByteBuf::readComponent)) },
            { buf, entry -> buf.writeNullable(entry.displayName, ByteBuf::writeComponent) })
        ;

        internal fun interface Reader {

            fun read(buf: ByteBuf, builder: EntryBuilder)
        }

        internal fun interface Writer {

            fun write(buf: ByteBuf, entry: Entry)
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
