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
import kotlinx.collections.immutable.persistentListOf
import net.kyori.adventure.text.Component
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.player.PlayerPublicKey
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.GameModes
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readGameProfile
import org.kryptonmc.krypton.util.readList
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeGameProfile
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import java.util.UUID

/**
 * Updates information on the tab list (called the player list by vanilla).
 *
 * @param action the action to perform
 * @param players a list of players, can be empty if not required by the [action]
 */
@JvmRecord
data class PacketOutPlayerInfo(val action: Action, val players: List<PlayerData>) : Packet {

    constructor(action: Action, vararg players: KryptonPlayer) : this(action, players.map(PlayerData::from))

    constructor(buf: ByteBuf) : this(buf, buf.readEnum<Action>())

    private constructor(buf: ByteBuf, action: Action) : this(action, buf.readList(action::read))

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(action.ordinal)
        buf.writeCollection(players) { action.write(buf, it) }
    }

    @JvmRecord
    data class PlayerData(
        val profile: GameProfile,
        val gameMode: GameMode?,
        val latency: Int,
        val displayName: Component?,
        val publicKey: PlayerPublicKey.Data?
    ) {

        companion object {

            @JvmStatic
            fun from(player: KryptonPlayer): PlayerData =
                PlayerData(player.profile, player.gameMode, player.session.latency, player.displayName, player.publicKey?.data)
        }
    }

    enum class Action {

        ADD_PLAYER {

            override fun read(buf: ByteBuf): PlayerData =
                PlayerData(buf.readGameProfile(), buf.readGameMode(), buf.readVarInt(), buf.readNullableComponent(), buf.readNullableKey())

            override fun write(buf: ByteBuf, data: PlayerData) {
                buf.writeGameProfile(data.profile)
                buf.writeVarInt(data.gameMode!!.ordinal)
                buf.writeVarInt(data.latency)
                buf.writeNullable(data.displayName, ByteBuf::writeComponent)
                buf.writeNullable(data.publicKey) { _, key -> key.write(buf) }
            }
        },
        UPDATE_GAMEMODE {

            override fun read(buf: ByteBuf): PlayerData = createData(buf.readUUID(), buf.readGameMode())

            override fun write(buf: ByteBuf, data: PlayerData) {
                buf.writeUUID(data.profile.uuid)
                buf.writeVarInt(data.gameMode!!.ordinal)
            }
        },
        UPDATE_LATENCY {

            override fun read(buf: ByteBuf): PlayerData = createData(buf.readUUID(), latency = buf.readVarInt())

            override fun write(buf: ByteBuf, data: PlayerData) {
                buf.writeUUID(data.profile.uuid)
                buf.writeVarInt(data.latency)
            }
        },
        UPDATE_DISPLAY_NAME {

            override fun read(buf: ByteBuf): PlayerData = createData(buf.readUUID(), displayName = buf.readNullableComponent())

            override fun write(buf: ByteBuf, data: PlayerData) {
                buf.writeUUID(data.profile.uuid)
                buf.writeNullable(data.displayName, ByteBuf::writeComponent)
            }
        },
        REMOVE_PLAYER {

            override fun read(buf: ByteBuf): PlayerData = createData(buf.readUUID())

            override fun write(buf: ByteBuf, data: PlayerData) {
                buf.writeUUID(data.profile.uuid)
            }
        };

        abstract fun read(buf: ByteBuf): PlayerData

        abstract fun write(buf: ByteBuf, data: PlayerData)

        companion object {

            private val BY_ID = values()

            @JvmStatic
            fun fromId(id: Int): Action? = BY_ID.getOrNull(id)
        }
    }

    companion object {

        @JvmStatic
        private fun createData(
            uuid: UUID,
            gameMode: GameMode? = null,
            latency: Int = 0,
            displayName: Component? = null,
            key: PlayerPublicKey.Data? = null
        ): PlayerData = PlayerData(KryptonGameProfile("", uuid, persistentListOf()), gameMode, latency, displayName, key)
    }
}

private fun ByteBuf.readGameMode(): GameMode? = GameModes.fromId(readVarInt())

private fun ByteBuf.readNullableComponent(): Component? = readNullable(ByteBuf::readComponent)

private fun ByteBuf.readNullableKey(): PlayerPublicKey.Data? = readNullable(PlayerPublicKey::Data)
