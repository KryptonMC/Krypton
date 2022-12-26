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
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutUpdateScore(val name: String, val action: Int, val objectiveName: String?, val score: Int) : Packet {

    constructor(buf: ByteBuf) : this(buf, buf.readString(MAX_NAME_LENGTH), buf.readVarInt(), buf.readString().ifEmpty { null })

    private constructor(buf: ByteBuf, name: String, action: Int, objectiveName: String?) : this(name, action, objectiveName,
        if (action != Actions.REMOVE) buf.readVarInt() else 0)

    override fun write(buf: ByteBuf) {
        buf.writeString(name, MAX_NAME_LENGTH)
        buf.writeVarInt(action)
        buf.writeString(objectiveName ?: "", MAX_OBJECTIVE_NAME_LENGTH)
        if (action != Actions.REMOVE) buf.writeVarInt(score)
    }

    object Actions {

        const val CREATE_OR_UPDATE: Int = 0
        const val REMOVE: Int = 1
    }

    companion object {

        private const val MAX_NAME_LENGTH = 40
        private const val MAX_OBJECTIVE_NAME_LENGTH = 16

        @JvmStatic
        fun createOrUpdate(score: Score): PacketOutUpdateScore =
            PacketOutUpdateScore(toLegacyString(score.name), Actions.CREATE_OR_UPDATE, score.objective?.name, score.score)

        @JvmStatic
        fun remove(member: Component, objectiveName: String?, score: Int): PacketOutUpdateScore =
            PacketOutUpdateScore(toLegacyString(member), Actions.REMOVE, objectiveName, score)

        @JvmStatic
        private fun toLegacyString(input: Component): String = LegacyComponentSerializer.legacySection().serialize(input)
    }
}
