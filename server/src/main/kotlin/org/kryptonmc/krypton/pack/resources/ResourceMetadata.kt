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

import org.kryptonmc.krypton.pack.metadata.MetadataSerializer
import org.kryptonmc.krypton.util.GsonHelper
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

interface ResourceMetadata {

    fun <T> getMetadata(serializer: MetadataSerializer<T>): T?

    companion object {

        @JvmField
        val EMPTY: ResourceMetadata = object : ResourceMetadata {

            override fun <T> getMetadata(serializer: MetadataSerializer<T>): T? = null
        }

        @JvmStatic
        fun fromJsonStream(input: InputStream): ResourceMetadata = BufferedReader(InputStreamReader(input, Charsets.UTF_8)).use {
            val json = GsonHelper.parse(it)
            object : ResourceMetadata {

                override fun <T> getMetadata(serializer: MetadataSerializer<T>): T? {
                    if (json.has(serializer.name)) return serializer.fromJson(json.getAsJsonObject(serializer.name))
                    return null
                }
            }
        }
    }
}
