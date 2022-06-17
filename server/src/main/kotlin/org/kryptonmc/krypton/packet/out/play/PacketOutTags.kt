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
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.tags.Tag
import org.kryptonmc.api.tags.TagType
import org.kryptonmc.krypton.packet.CachedPacket
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.tags.KryptonTagManager
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutTags(val tags: Map<TagType<*>, List<Tag<*>>>) : Packet {

    @Suppress("UNCHECKED_CAST")
    override fun write(buf: ByteBuf) {
        buf.writeVarInt(tags.size)
        tags.forEach { (type, tags) ->
            buf.writeKey(type.key())
            buf.writeVarInt(tags.size)
            tags.forEach { tag ->
                buf.writeKey(tag.key())
                val values = tag.values
                buf.writeVarInt(values.size)
                values.forEach { buf.writeVarInt((type.registry as Registry<Any>).idOf(it)) }
            }
        }
    }

    companion object {

        @JvmField
        val CACHED: CachedPacket = CachedPacket {  PacketOutTags(KryptonTagManager.tags) }
    }
}
