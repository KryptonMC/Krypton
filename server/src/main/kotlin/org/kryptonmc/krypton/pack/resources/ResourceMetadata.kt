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
package org.kryptonmc.krypton.pack.resources

import org.kryptonmc.krypton.pack.metadata.MetadataSectionSerializer
import org.kryptonmc.krypton.util.gson.GsonHelper
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.function.Supplier

interface ResourceMetadata {

    fun <T> getSection(serializer: MetadataSectionSerializer<T>): T?

    companion object {

        @JvmField
        val EMPTY: ResourceMetadata = object : ResourceMetadata {
            override fun <T> getSection(serializer: MetadataSectionSerializer<T>): T? = null
        }
        @JvmField
        val EMPTY_SUPPLIER: Supplier<ResourceMetadata> = Supplier { EMPTY }

        @JvmStatic
        fun fromJsonStream(input: InputStream): ResourceMetadata = BufferedReader(InputStreamReader(input, Charsets.UTF_8)).use {
            val json = GsonHelper.parse(it)
            object : ResourceMetadata {
                override fun <T> getSection(serializer: MetadataSectionSerializer<T>): T? {
                    val name = serializer.metadataSectionName()
                    return if (json.has(name)) serializer.fromJson(json.getAsJsonObject(name)) else null
                }
            }
        }
    }
}
