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

import com.mojang.brigadier.suggestion.Suggestions
import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Sent by the server as a response to the [tab complete][org.kryptonmc.krypton.packet.in.play.PacketInTabComplete].
 * Contains all of the matches that the server got for the command provided by the request packet.
 *
 * @param id the unique ID sent by the client to identify this request
 * @param matches matches for the given request
 */
@JvmRecord
data class PacketOutTabComplete(
    val id: Int,
    val matches: Suggestions
) : Packet {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(id)
        buf.writeVarInt(matches.range.start)
        buf.writeVarInt(matches.range.length)

        buf.writeCollection(matches.list) {
            buf.writeString(it.text)
            buf.writeBoolean(it.tooltip != null)
            if (it.tooltip != null) buf.writeChat(Component.text(it.tooltip.string))
        }
    }
}
