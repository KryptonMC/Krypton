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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Tells the client to perform an action to an objective for a scoreboard
 */
@JvmRecord
data class PacketOutObjective(
    val action: Action,
    val name: String,
    val displayName: Component,
    val renderType: Int
) : Packet {

    constructor(action: Action, objective: Objective) : this(
        action,
        objective.name,
        objective.displayName,
        Registries.OBJECTIVE_RENDER_TYPES.idOf(objective.renderType)
    )

    override fun write(buf: ByteBuf) {
        buf.writeString(name, 16)
        buf.writeByte(action.ordinal)
        if (action != Action.REMOVE) {
            buf.writeChat(displayName)
            buf.writeVarInt(renderType)
        }
    }

    enum class Action {

        CREATE,
        REMOVE,
        UPDATE_TEXT;

        companion object {

            private val BY_ID = values()

            @JvmStatic
            fun fromId(id: Int): Action? = BY_ID.getOrNull(id)
        }
    }
}
