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
package org.kryptonmc.krypton.pack.metadata

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.util.logger
import java.util.function.Predicate
import java.util.regex.Pattern

class ResourceFilterData(private val blockList: List<KeyPattern>) {

    fun isNamespaceFiltered(namespace: String): Boolean = blockList.any { it.namespacePredicate.test(namespace) }

    fun isPathFiltered(path: String): Boolean = blockList.any { it.pathPredicate.test(path) }

    class KeyPattern(namespacePattern: Pattern?, pathPattern: Pattern?) : Predicate<Key> {

        val namespacePredicate: Predicate<String> = namespacePattern?.asPredicate() ?: Predicate { true }
        val pathPredicate: Predicate<String> = pathPattern?.asPredicate() ?: Predicate { true }

        override fun test(t: Key): Boolean = namespacePredicate.test(t.namespace()) && pathPredicate.test(t.value())

        companion object {

            @JvmStatic
            fun fromJson(json: JsonElement): KeyPattern {
                if (json !is JsonObject) {
                    LOGGER.error("Invalid filter data! Expected object, got $json!")
                    throw RuntimeException("Invalid filter data! Expected object, got $json!")
                }
                val namespacePattern = if (json.has("namespace")) Pattern.compile(json.get("namespace").asString) else null
                val pathPattern = if (json.has("path")) Pattern.compile(json.get("path").asString) else null
                return KeyPattern(namespacePattern, pathPattern)
            }
        }
    }

    companion object {

        private val LOGGER = logger<ResourceFilterData>()
        @JvmField
        val SERIALIZER: MetadataSerializer<ResourceFilterData> = object : MetadataSerializer<ResourceFilterData> {

            override val name: String
                get() = "filter"

            override fun fromJson(json: JsonObject): ResourceFilterData {
                if (!json.has("blocks") || !json.get("blocks").isJsonArray) {
                    LOGGER.error("Invalid filter data! \"blocks\" must be present and must be an array! Data: $json")
                    throw RuntimeException("Invalid filter data! \"blocks\" must be present and must be an array! Data: $json")
                }
                return ResourceFilterData(json.get("blocks").asJsonArray.map(KeyPattern::fromJson))
            }
        }
    }
}
