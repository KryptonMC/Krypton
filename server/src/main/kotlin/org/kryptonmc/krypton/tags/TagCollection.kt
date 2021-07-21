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
package org.kryptonmc.krypton.tags

import com.google.common.collect.ImmutableBiMap
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.krypton.util.writeIntList
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeMap

interface TagCollection<T : Any> {

    val allTags: Map<Key, Tag<T>>

    operator fun get(key: Key) = allTags[key]

    operator fun get(tag: Tag.Named<T>) = tag.name

    operator fun get(tag: Tag<T>): Key?

    fun getOrEmpty(key: Key): Tag<T>

    operator fun contains(key: Key) = allTags.containsKey(key)

    fun matching(value: T): Collection<Key> = allTags.entries.asSequence().filter { it.value.contains(value) }.map { it.key }.toList()

    fun write(registry: Registry<T>): NetworkPayload {
        val tags = allTags
        val output = mutableMapOf<Key, IntList>()
        tags.forEach { (key, value) ->
            val values = value.values
            val list = IntArrayList(values.size)
            values.forEach { list.add(registry.idOf(it)) }
            output[key] = list
        }
        return NetworkPayload(output)
    }

    val availableTags: Collection<Key>
        get() = allTags.keys

    class NetworkPayload(private val tags: Map<Key, IntList>) {

        fun write(buf: ByteBuf) = buf.writeMap(tags, ByteBuf::writeKey, ByteBuf::writeIntList)
    }

    companion object {

        fun <T : Any> empty() = of<T>(emptyMap())

        fun <T : Any> of(tags: Map<Key, Tag<T>>): TagCollection<T> {
            val map = ImmutableBiMap.copyOf(tags)
            return object : TagCollection<T> {
                private val empty = SetTag.empty<T>()
                override val allTags = map
                override fun getOrEmpty(key: Key) = map.getOrDefault(key, empty)
                override fun get(tag: Tag<T>) = if (tag is Tag.Named<*>) tag.name else map.inverse()[tag]
            }
        }
    }
}
