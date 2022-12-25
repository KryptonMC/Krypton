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
