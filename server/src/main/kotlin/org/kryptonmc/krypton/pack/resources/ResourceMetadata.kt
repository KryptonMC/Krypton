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
