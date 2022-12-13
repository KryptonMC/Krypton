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
package org.kryptonmc.krypton.pack

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.pack.metadata.MetadataSerializer
import java.io.InputStream
import java.util.function.Predicate

interface PackResources : AutoCloseable {

    val namespaces: Set<String>

    fun name(): String

    fun hasResource(location: Key): Boolean

    fun getRootResource(fileName: String): InputStream?

    fun getResource(location: Key): InputStream

    fun getResources(namespace: String, path: String, predicate: Predicate<Key>): Collection<Key>

    fun <T> getMetadata(serializer: MetadataSerializer<T>): T?

    companion object {

        const val METADATA_EXTENSION: String = ".mcmeta"
        const val PACK_META: String = "pack$METADATA_EXTENSION"
        const val DATA_FOLDER_NAME: String = "data"
    }
}
