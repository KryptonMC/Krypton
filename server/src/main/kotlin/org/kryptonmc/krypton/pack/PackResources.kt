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
package org.kryptonmc.krypton.pack

import net.kyori.adventure.key.Key
import java.io.InputStream

interface PackResources : AutoCloseable {

    val name: String
    val namespaces: Set<String>
    val metadata: PackMetadata?

    fun root(path: String): InputStream?

    fun resource(key: Key): InputStream?

    fun resources(namespace: String, value: String, maxDepth: Int, predicate: (String) -> Boolean): Collection<Key>

    operator fun contains(key: Key): Boolean

    companion object {

        const val METADATA_EXTENSION = ".mcmeta"
        const val PACK_META = "pack.mcmeta"
    }
}
