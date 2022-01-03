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
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.scoreboard.Score
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutUpdateScore(
    val action: Action,
    val name: Component,
    val objectiveName: String?,
    val score: Int,
) : Packet {

    constructor(action: Action, score: Score) : this(action, score.name, score.objective?.name, score.score)

    override fun write(buf: ByteBuf) {
        buf.writeString(LegacyComponentSerializer.legacySection().serialize(name), 40)
        buf.writeByte(action.ordinal)
        buf.writeString(objectiveName ?: "", 16)
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
}
