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
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.registry.Registries
import org.kryptonmc.krypton.registry.tags.Tag
import org.kryptonmc.krypton.registry.tags.TagManager

/**
 * Tells the client all the tags present on the server. This is an object as it only needs to be instantiated once
 */
object PacketOutTags : PlayPacket(0x5B) {

    private val blockRegistry: (Key) -> Int? = {
        Registries.BLOCKS.idOf(it)
    }
    private val fluidRegistry: (Key) -> Int? = {
        Registries.FLUIDS.idOf(it)
    }
    private val itemRegistry: (Key) -> Int? = {
        Registries.ITEMS.idOf(it)
    }
    private val entityRegistry: (Key) -> Int? = {
        Registries.ENTITY_TYPES.idOf(it)
    }

    override fun write(buf: ByteBuf) {
        buf.writeTags(TagManager.blockTags, blockRegistry)
        buf.writeTags(TagManager.itemTags, itemRegistry)
        buf.writeTags(TagManager.fluidTags, fluidRegistry)
        buf.writeTags(TagManager.entityTags, entityRegistry)
    }

    private fun ByteBuf.writeTags(tags: Set<Tag>, registry: (Key) -> Int?) {
        writeVarInt(tags.size)
        tags.forEach { tag ->
            writeString(tag.name.asString())

            val values = tag.values.filter { registry(it) != null }
            writeVarInt(values.size)
            values.forEach { writeVarInt(registry(it)!!) }
        }
    }
}
