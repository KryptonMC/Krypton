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
package org.kryptonmc.krypton.tags

import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.IntArrayList
import kotlinx.collections.immutable.persistentMapOf
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.tags.TagType
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.registry.downcast
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.readMap
import org.kryptonmc.krypton.util.readVarIntArray
import org.kryptonmc.krypton.util.transform
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeMap
import org.kryptonmc.krypton.util.writeVarIntArray

object TagSerializer {

    @JvmStatic
    fun serialize(): Map<ResourceKey<TagType<*>>, NetworkPayload> = Registries.TAG_TYPES
        .transform { it.key to serializeTags(it.value) }
        .filter { !it.value.isEmpty() }

    @JvmStatic
    private fun <T : Any> serializeTags(type: TagType<T>): NetworkPayload {
        val tags = persistentMapOf<Key, IntArray>().builder()
        KryptonTagManager.get(type).forEach { tag ->
            val list = IntArrayList(tag.values.size)
            tag.values.forEach { list.add(type.registry.downcast().idOf(it)) }
            tags[tag.key()] = list.toIntArray()
        }
        return NetworkPayload(tags.build())
    }

    @JvmRecord
    data class NetworkPayload(val tags: Map<Key, IntArray>) : Writable {

        constructor(buf: ByteBuf) : this(buf.readMap(ByteBuf::readKey, ByteBuf::readVarIntArray))

        fun isEmpty(): Boolean = tags.isEmpty()

        override fun write(buf: ByteBuf) {
            buf.writeMap(tags, ByteBuf::writeKey, ByteBuf::writeVarIntArray)
        }
    }
}
