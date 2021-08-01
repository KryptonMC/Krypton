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
package org.kryptonmc.krypton.server

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import me.bardy.gsonkt.fromJson
import org.kryptonmc.krypton.util.logger
import java.nio.file.Path
import kotlin.io.path.bufferedReader
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.writeText

abstract class ServerConfigList<K, V : ServerConfigEntry<K>>(val path: Path) : Iterable<V> {

    private val map = hashMapOf<String, V>()
    private val gson = Gson()
    val size get() = map.values.size

    open operator fun get(key: K) = map[key.toString()]

    operator fun plusAssign(entry: V) = add(entry)

    operator fun minusAssign(key: K) = remove(key)

    override fun iterator(): Iterator<V> = map.values.iterator()

    fun contains(key: K) = map.containsKey(key.toString())

    fun isEmpty() = map.isEmpty()


    fun add(entry: V) {
        map[entry.key.toString()] = entry

        save()
    }

    fun remove(key: K) {
        map.remove(key.toString())

        save()
    }

    internal abstract fun fromJson(data: JsonObject): ServerConfigEntry<K>?

    fun save() {
        val players = JsonArray()

        for (value in map.values) {
            val data = JsonObject()
            value.writeToJson(data)
            players.add(data)
        }

        path.bufferedWriter(charset = Charsets.UTF_8).use {
            gson.toJson(players, it)
        }
    }

    fun load() {
        map.clear()
        path.bufferedReader(charset = Charsets.UTF_8).use {
            val data = try {
                gson.fromJson<JsonArray>(it)
            } catch(_: JsonSyntaxException) {
                LOGGER.error("Couldn't parse ${path.fileName}. Delete it to reset it")
                return
            }
            for (jsonEntry in data) {
                val entry = jsonEntry as JsonObject
                val serverConfigEntry = fromJson(entry)
                if(serverConfigEntry != null) {
                    map[serverConfigEntry.key.toString()] = serverConfigEntry as V
                } else {
                    LOGGER.error("Couldn't parse ${path.fileName}. Delete it to reset it")
                }
            }
        }
    }

    fun validatePath() {
        if (!path.exists()) {
            path.createFile()
            path.writeText("[]")
        } else {
            load()
        }
    }

    companion object {
        private val LOGGER = logger<ServerConfigList<*, *>>()
    }

}
