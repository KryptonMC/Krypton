/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.resource

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.pack.resources.Resource
import org.kryptonmc.krypton.pack.resources.ResourceManager

class FileToIdConverter(private val prefix: String, private val extension: String) {

    fun idToFile(key: Key): Key = Key.key(key.namespace(), "$prefix/${key.value()}$extension")

    fun fileToId(key: Key): Key {
        val value = key.value()
        return Key.key(value.substring(prefix.length + 1, value.length - extension.length))
    }

    fun listMatchingResources(resourceManager: ResourceManager): Map<Key, Resource> =
        resourceManager.listResources(prefix) { it.value().endsWith(extension) }

    fun listMatchingResourceStacks(resourceManager: ResourceManager): Map<Key, List<Resource>> =
        resourceManager.listResourceStacks(prefix) { it.value().endsWith(extension) }

    companion object {

        @JvmStatic
        fun json(prefix: String): FileToIdConverter = FileToIdConverter(prefix, ".json")
    }
}
