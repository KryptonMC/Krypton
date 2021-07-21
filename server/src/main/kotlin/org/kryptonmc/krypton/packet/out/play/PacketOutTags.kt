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
import org.kryptonmc.krypton.tags.Tag
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.tags.KryptonTagManager
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Tells the client all the tags present on the server. This is an object as it only needs to be instantiated once
 */
object PacketOutTags : PlayPacket(0x66) {

    @Suppress("UNCHECKED_CAST")
    override fun write(buf: ByteBuf) {
        buf.writeVarInt(KryptonTagManager.tags.size)
        KryptonTagManager.tags.forEach { (id, tags) ->
            if (tags.isEmpty()) return@forEach
            buf.writeKey(id)
            buf.writeTags(tags as List<Tag<Any>>)
        }
    }

    private fun ByteBuf.writeTags(tags: List<Tag<Any>>) {
        writeVarInt(tags.size)
        val registry = tags.firstOrNull()?.type?.registry ?: return
        tags.forEach { tag ->
            writeKey(tag.name)
            writeCollection(tag.values) { writeVarInt(registry.idOf(it)) }
        }
    }
}
