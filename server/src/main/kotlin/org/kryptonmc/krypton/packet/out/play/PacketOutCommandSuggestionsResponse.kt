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
