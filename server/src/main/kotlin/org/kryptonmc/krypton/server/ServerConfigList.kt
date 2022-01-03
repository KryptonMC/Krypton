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
package org.kryptonmc.krypton.server

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.kryptonmc.krypton.util.logger
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.reader
import kotlin.io.path.writeText
import kotlin.io.path.writer

abstract class ServerConfigList<K, V : ServerConfigEntry<K>>(val path: Path) : Iterable<V> {

    private val map = mutableMapOf<String, V>()
    val size: Int
        get() = map.values.size
    val values: Collection<V>
        get() = map.values

    abstract fun read(reader: JsonReader): ServerConfigEntry<K>?

    open fun key(key: K): String = key.toString()

    open operator fun get(key: K): V? = map[key(key)]

    open fun validatePath() {
        if (!path.exists()) {
            path.createFile()
            path.writeText("[]")
            return
        }
        load()
    }

    fun contains(key: K): Boolean = map.containsKey(key(key))

    fun isEmpty(): Boolean = map.isEmpty()

    fun add(entry: V) {
        if (contains(entry.key)) return
        map[key(entry.key)] = entry
        save()
    }

    fun remove(key: K) {
        if (!contains(key)) return
        map.remove(key(key))
        save()
    }

    fun save() {
        JsonWriter(path.writer()).use { writer ->
            writer.beginArray()
            map.values.forEach { it.write(writer) }
            writer.endArray()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun load() {
        map.clear()
        JsonReader(path.reader()).use { reader ->
            reader.beginArray()
            while (reader.hasNext()) {
                val entry = read(reader)
                if (entry == null) {
                    LOGGER.error("Failed to parse ${path.fileName}! Delete it to reset it.")
                    continue
                }
                map[entry.key.toString()] = entry as V
            }
            reader.endArray()
        }
    }

    override fun iterator(): Iterator<V> = map.values.iterator()

    companion object {

        private val LOGGER = logger<ServerConfigList<*, *>>()
    }
}
