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

import me.bardy.gsonkt.fromJson
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.GSON
import java.io.FileNotFoundException
import java.io.Reader
import java.util.concurrent.ConcurrentHashMap

object KryptonTagManager : TagManager {

    private val cache = ConcurrentHashMap<Key, Tag<out Any>>()
    override val tags = mutableMapOf<Key, MutableList<Tag<out Any>>>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(key: Key): List<Tag<T>> = tags[key] as? List<Tag<T>> ?: emptyList()

    fun <T : Any> load(name: Key, type: TagType<T>) = load(name, type) {
        Thread.currentThread().contextClassLoader.getResourceAsStream("registries/tags/${type.name}/${name.value()}.json")!!.reader()
    }.apply { tags.getOrPut(type.key) { mutableListOf() }.add(this) }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> load(name: Key, type: TagType<T>, reader: () -> Reader): Tag<T> {
        val previous = cache.getOrDefault(name, KryptonTag.empty(type)) as Tag<T>
        return cache.getOrPut(name) { create(previous, name, type, reader) } as Tag<T>
    }

    private fun <T : Any> create(previous: Tag<T>, name: Key, type: TagType<T>, reader: () -> Reader): Tag<T> {
        val data = GSON.fromJson<TagData>(reader())
        return try {
            KryptonTag(this, name, type, previous, data)
        } catch (exception: FileNotFoundException) {
            KryptonTag.empty(type)
        }
    }
}
