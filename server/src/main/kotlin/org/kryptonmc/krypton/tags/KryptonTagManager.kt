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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.tags.Tag
import org.kryptonmc.api.tags.TagManager
import org.kryptonmc.api.tags.TagType
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

@Suppress("UNCHECKED_CAST")
object KryptonTagManager : TagManager {

    private val GSON = Gson()
    private val TAG_MAP = ConcurrentHashMap<TagType<out Any>, MutableList<KryptonTag<out Any>>>()
    override val tags: Map<TagType<*>, List<Tag<*>>> = Collections.unmodifiableMap(TAG_MAP)

    @JvmStatic
    fun bootstrap() {
        Registries.TAG_TYPES.values.forEach { type ->
            if (type !is KryptonTagType) return@forEach
            val json = ClassLoader.getSystemResourceAsStream(type.path)!!.reader().use {
                GSON.fromJson(it, JsonObject::class.java)
            }
            val identifierMap = TAG_MAP.getOrPut(type) { CopyOnWriteArrayList() }
            json.keySet().forEach {
                val tag = KryptonTag(Key.key(it), type, keys(json, it))
                identifierMap.add(tag)
            }
        }
    }

    override fun <T : Any> get(type: TagType<T>): List<Tag<T>> = TAG_MAP[type] as? List<Tag<T>> ?: emptyList()

    override fun <T : Any> get(type: TagType<T>, name: String): KryptonTag<T>? {
        val tags = TAG_MAP[type] ?: return null
        return tags.firstOrNull { it.key().asString() == name } as? KryptonTag<T>
    }

    @JvmStatic
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
