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
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Tells the client to perform an action to an objective for a scoreboard.
 */
@JvmRecord
data class PacketOutUpdateObjectives(val name: String, val action: Int, val displayName: Component, val renderType: Int) : Packet {

    constructor(buf: ByteBuf) : this(buf, buf.readString(), buf.readByte().toInt())

    private constructor(buf: ByteBuf, name: String, action: Int) : this(
        name,
        action,
        if (action != Actions.REMOVE) buf.readComponent() else Component.empty(),
        if (action != Actions.REMOVE) buf.readVarInt() else 0
    )

    override fun write(buf: ByteBuf) {
        buf.writeString(name, 16)
        buf.writeByte(action)
        if (action != Actions.REMOVE) {
            buf.writeComponent(displayName)
            buf.writeVarInt(renderType)
        }
    }

    object Actions {

        const val CREATE: Int = 0
        const val REMOVE: Int = 1
        const val UPDATE_TEXT: Int = 2
    }

    companion object {

        @JvmStatic
        fun updateText(objective: Objective): PacketOutUpdateObjectives =
            PacketOutUpdateObjectives(objective.name, Actions.UPDATE_TEXT, objective.displayName, objective.renderType.ordinal)
    }
}
