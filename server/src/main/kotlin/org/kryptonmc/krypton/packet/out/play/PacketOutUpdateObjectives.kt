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
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.Packet

/**
 * Tells the client to perform an action to an objective for a scoreboard.
 */
@JvmRecord
data class PacketOutUpdateObjectives(val name: String, val action: Byte, val displayName: Component, val renderType: Int) : Packet {

    init {
        require(name.length <= 16) { "Objective name too long! Max: 16" }
    }

    constructor(reader: BinaryReader) : this(reader, reader.readString(), reader.readByte())

    private constructor(reader: BinaryReader, name: String, action: Byte) : this(
        name,
        action,
        if (action != Actions.REMOVE) reader.readComponent() else Component.empty(),
        if (action != Actions.REMOVE) reader.readVarInt() else 0
    )

    override fun write(writer: BinaryWriter) {
        writer.writeString(name)
        writer.writeByte(action)
        if (action != Actions.REMOVE) {
            writer.writeComponent(displayName)
            writer.writeVarInt(renderType)
        }
    }

    object Actions {

        const val CREATE: Byte = 0
        const val REMOVE: Byte = 1
        const val UPDATE_TEXT: Byte = 2
    }

    companion object {

        @JvmStatic
        fun create(objective: Objective): PacketOutUpdateObjectives = createOrRemove(objective, Actions.CREATE)

        @JvmStatic
        fun remove(objective: Objective): PacketOutUpdateObjectives = createOrRemove(objective, Actions.REMOVE)

        @JvmStatic
        private fun createOrRemove(objective: Objective, action: Byte): PacketOutUpdateObjectives {
            return PacketOutUpdateObjectives(objective.name, action, objective.displayName, objective.renderType.ordinal)
        }

        @JvmStatic
        fun updateText(objective: Objective): PacketOutUpdateObjectives {
            return PacketOutUpdateObjectives(objective.name, Actions.UPDATE_TEXT, objective.displayName, objective.renderType.ordinal)
        }
    }
}
