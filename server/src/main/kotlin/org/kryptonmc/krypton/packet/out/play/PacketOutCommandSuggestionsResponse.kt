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

import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.suggestion.Suggestion
import com.mojang.brigadier.suggestion.Suggestions
import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.AdventureMessage
import org.kryptonmc.krypton.adventure.KryptonAdventure
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readList
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Sent by the server as a response to the
 * [tab complete][org.kryptonmc.krypton.packet. in.play.PacketInCommandSuggestionsRequest].
 * Contains all of the matches that the server got for the command provided by
 * the request packet.
 *
 * @param id the unique ID sent by the client to identify this request
 * @param suggestions the suggestions for the client
 */
@JvmRecord
data class PacketOutCommandSuggestionsResponse(val id: Int, val suggestions: Suggestions) : Packet {

    constructor(buf: ByteBuf) : this(buf.readVarInt(), readSuggestions(buf))

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(id)
        buf.writeVarInt(suggestions.range.start)
        buf.writeVarInt(suggestions.range.length)
        buf.writeCollection(suggestions.list) {
            buf.writeString(it.text)
            buf.writeNullable(it.tooltip) { buf, tooltip ->
                val message = if (tooltip is AdventureMessage) tooltip.asComponent() else Component.text(tooltip.string)
                buf.writeComponent(message)
            }
        }
    }

    companion object {

        @JvmStatic
        private fun readSuggestions(buf: ByteBuf): Suggestions {
            val start = buf.readVarInt()
            val length = buf.readVarInt()
            val range = StringRange.between(start, start + length)
            val results = buf.readList { Suggestion(range, buf.readString(), buf.readNullable { KryptonAdventure.asMessage(buf.readComponent()) }) }
            return Suggestions(range, results)
        }
    }
}
