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
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Updates information on the tab list (called the player list by vanilla).
 *
 * @param action the action to perform
 * @param players a list of players, can be empty if not required by the [action]
 */
@JvmRecord
data class PacketOutPlayerInfo(
    private val action: PlayerAction,
    private val players: Collection<KryptonPlayer> = emptyList()
) : Packet {

    constructor(action: PlayerAction, vararg players: KryptonPlayer) : this(action, players.toList())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(action.ordinal)
        buf.writeVarInt(players.size)

        players.forEach { update ->
            buf.writeUUID(update.profile.uuid)
            when (action) {
                PlayerAction.ADD_PLAYER -> {
                    buf.writeString(update.profile.name)
                    buf.writeVarInt(update.profile.properties.size)

                    update.profile.properties.forEach {
                        buf.writeString(it.name)
                        buf.writeString(it.value)
                        val signature = it.signature
                        buf.writeBoolean(signature != null)
                        if (signature != null) buf.writeString(signature)
                    }

                    buf.writeVarInt(update.gamemode.ordinal)
                    buf.writeVarInt(update.session.latency)
                    buf.writeBoolean(true)
                    buf.writeChat(update.displayName)
                }
                PlayerAction.UPDATE_GAMEMODE -> buf.writeVarInt(update.gamemode.ordinal)
                PlayerAction.UPDATE_LATENCY -> buf.writeVarInt(update.session.latency)
                PlayerAction.UPDATE_DISPLAY_NAME -> {
                    buf.writeBoolean(true)
                    buf.writeChat(update.displayName)
                }
                PlayerAction.REMOVE_PLAYER -> Unit
            }
        }
    }

    enum class PlayerAction {

        ADD_PLAYER,
        UPDATE_GAMEMODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }
}
