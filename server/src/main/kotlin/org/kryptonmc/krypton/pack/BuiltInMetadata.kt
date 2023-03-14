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
package org.kryptonmc.krypton.pack

import org.kryptonmc.krypton.pack.metadata.MetadataSectionSerializer
import org.kryptonmc.krypton.util.ImmutableMaps

class BuiltInMetadata private constructor(private val values: Map<MetadataSectionSerializer<*>, *>) {

    @Suppress("UNCHECKED_CAST")
    fun <T> get(serializer: MetadataSectionSerializer<T>): T = values.get(serializer) as T

    companion object {

        private val EMPTY = BuiltInMetadata(ImmutableMaps.of<_, Any>())

        @JvmStatic
        fun of(): BuiltInMetadata = EMPTY

        @JvmStatic
        fun <T> of(serializer: MetadataSectionSerializer<T>, value: T): BuiltInMetadata = BuiltInMetadata(ImmutableMaps.of(serializer, value))

        @JvmStatic
        fun <T1, T2> of(s1: MetadataSectionSerializer<T1>, v1: T1, s2: MetadataSectionSerializer<T2>, v2: T2): BuiltInMetadata =
            BuiltInMetadata(ImmutableMaps.of(s1, v1, s2, v2))
    }
}
