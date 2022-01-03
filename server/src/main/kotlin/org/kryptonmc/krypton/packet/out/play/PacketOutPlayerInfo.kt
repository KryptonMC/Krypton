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
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeString
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
data class PacketOutPlayerInfo(
    val action: Action,
    val players: List<PlayerData> = emptyList()
) : Packet {

    constructor(action: Action, vararg players: KryptonPlayer) : this(
        action,
        players.map { PlayerData(it.profile.uuid, it.profile.name, it.profile.properties, it.gameMode, it.session.latency, it.displayName) }
    )

    constructor(action: Action, players: Collection<KryptonPlayer>) : this(
        action,
        players.map { PlayerData(it.profile.uuid, it.profile.name, it.profile.properties, it.gameMode, it.session.latency, it.displayName) }
    )

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(action.ordinal)
        buf.writeVarInt(players.size)

        players.forEach { update ->
            buf.writeUUID(update.uuid)
            when (action) {
                Action.ADD_PLAYER -> {
                    buf.writeString(update.name)
                    buf.writeVarInt(update.properties.size)

                    update.properties.forEach {
                        buf.writeString(it.name)
                        buf.writeString(it.value)
                        val signature = it.signature
                        buf.writeBoolean(signature != null)
                        if (signature != null) buf.writeString(signature)
                    }

                    buf.writeEnum(update.gameMode)
                    buf.writeVarInt(update.latency)
                    buf.writeBoolean(update.displayName != null)
                    if (update.displayName != null) buf.writeChat(update.displayName)
                }
                Action.UPDATE_GAMEMODE -> buf.writeEnum(update.gameMode)
                Action.UPDATE_LATENCY -> buf.writeVarInt(update.latency)
                Action.UPDATE_DISPLAY_NAME -> {
                    buf.writeBoolean(update.displayName != null)
                    if (update.displayName != null) buf.writeChat(update.displayName)
                }
                Action.REMOVE_PLAYER -> Unit
            }
        }
    }

    @JvmRecord
    data class PlayerData(
        val uuid: UUID,
        val name: String,
        val properties: List<ProfileProperty>,
        val gameMode: GameMode,
        val latency: Int,
        val displayName: Component?
    )

    enum class Action {

        ADD_PLAYER,
        UPDATE_GAMEMODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER;

        companion object {

            private val BY_ID = values()

            @JvmStatic
            fun fromId(id: Int): Action? = BY_ID.getOrNull(id)
        }
    }
}
