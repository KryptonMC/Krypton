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
import org.kryptonmc.api.scoreboard.Score
import org.kryptonmc.krypton.adventure.toLegacySectionText
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutUpdateScore(val name: String, val action: Action, val objectiveName: String?, val score: Int) : Packet {

    constructor(name: Component, action: Action, objectiveName: String?, score: Int) : this(name.toLegacySectionText(), action, objectiveName, score)

    constructor(action: Action, score: Score) : this(score.name.toLegacySectionText(), action, score.objective?.name, score.score)

    override fun write(buf: ByteBuf) {
        buf.writeString(name, MAX_NAME_LENGTH)
        buf.writeByte(action.ordinal)
        buf.writeString(objectiveName ?: "", MAX_OBJECTIVE_NAME_LENGTH)
        if (action != Action.REMOVE) buf.writeVarInt(score)
    }

    enum class Action {

        CREATE_OR_UPDATE,
        REMOVE;

        companion object {

            private val BY_ID = values()

            @JvmStatic
            fun fromId(id: Int): Action? = BY_ID.getOrNull(id)
        }
    }

    companion object {

        private const val MAX_NAME_LENGTH = 40
        private const val MAX_OBJECTIVE_NAME_LENGTH = 16
    }
}
