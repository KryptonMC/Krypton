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
