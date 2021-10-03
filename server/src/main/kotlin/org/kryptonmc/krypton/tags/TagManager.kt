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

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.kyori.adventure.key.Key
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

object TagManager {

    private val GSON = Gson()
    private val TAG_MAP = ConcurrentHashMap<TagType<out Any>, MutableList<Tag<out Any>>>()
    val TAGS: Map<TagType<out Any>, List<Tag<out Any>>>
        get() = Collections.unmodifiableMap(TAG_MAP)

    init {
        TagTypes.VALUES.forEach { type ->
            val json = ClassLoader.getSystemResourceAsStream(type.path)!!.reader().use {
                GSON.fromJson(it, JsonObject::class.java)
            }
            val identifierMap = TAG_MAP.getOrPut(type) { CopyOnWriteArrayList() }
            json.keySet().forEach {
                val tag = Tag(Key.key(it), type, keys(json, it))
                identifierMap.add(tag)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(type: TagType<T>, name: String): Tag<T>? = TAG_MAP[type]?.firstOrNull { it.name.asString() == name } as? Tag<T>

    private fun keys(main: JsonObject, value: String): Set<String> {
        val tagObject = main.getAsJsonObject(value)
        val tagValues = tagObject.getAsJsonArray("values")
        val result = HashSet<String>(tagValues.size())
        tagValues.forEach {
            val asString = it.asString
            if (asString.startsWith("#")) {
                result.addAll(keys(main, asString.substring(1)))
            } else {
                result.add(asString)
            }
        }
        return result
    }
}
