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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.registry.FileRegistries
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.tags.Tag
import org.kryptonmc.krypton.registry.tags.TagManager
import org.kryptonmc.krypton.registry.tags.TagType
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Tells the client all the tags present on the server. This is an object as it only needs to be instantiated once
 */
object PacketOutTags : PlayPacket(0x66) {

    private val blockRegistry: (Key) -> Int? = {
        Registries.BLOCK[it]?.id
    }
    private val fluidRegistry: (Key) -> Int? = {
        InternalRegistries.FLUID.idOf(InternalRegistries.FLUID[it])
    }
    private val itemRegistry: (Key) -> Int? = {
        Registries.ITEM.idOf(Registries.ITEM[it])
    }
    private val entityRegistry: (Key) -> Int? = {
        Registries.ENTITY_TYPE.idOf(Registries.ENTITY_TYPE[it])
    }
    private val gameEventRegistry: (Key) -> Int? = { key ->
        InternalRegistries.GAME_EVENT[key]?.let { InternalRegistries.GAME_EVENT.idOf(it) }
    }

    private val registries = mapOf(
        TagType.BLOCKS.identifier to blockRegistry,
        TagType.ITEMS.identifier to itemRegistry,
        TagType.FLUIDS.identifier to fluidRegistry,
        TagType.ENTITY_TYPES.identifier to entityRegistry,
        TagType.GAME_EVENTS.identifier to gameEventRegistry
    )

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(TagManager.tags.size)
        TagManager.tags.forEach { (id, tags) ->
            buf.writeString(id)
            buf.writeTags(tags, registries.getValue(id))
        }
    }

    private fun ByteBuf.writeTags(tags: List<Tag>, registry: (Key) -> Int?) {
        writeVarInt(tags.size)
        tags.forEach { tag ->
            writeString(tag.name.asString())

            val values = tag.values.filter { registry(it) != null }
            writeVarInt(values.size)
            values.forEach { writeVarInt(registry(it)!!) }
        }
    }
}
