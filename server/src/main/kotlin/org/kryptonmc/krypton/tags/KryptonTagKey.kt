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
package org.kryptonmc.krypton.tags

import com.google.common.collect.Interners
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataResult

@JvmRecord
data class KryptonTagKey<T>(override val registry: ResourceKey<out Registry<T>>, override val location: Key) : TagKey<T> {

    object Factory : TagKey.Factory {

        override fun <T> of(registry: ResourceKey<out Registry<T>>, location: Key): TagKey<T> = KryptonTagKey.of(registry, location)
    }

    companion object {

        private val VALUES = Interners.newWeakInterner<KryptonTagKey<*>>()

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T> of(registry: ResourceKey<out Registry<T>>, location: Key): KryptonTagKey<T> =
            VALUES.intern(KryptonTagKey(registry, location)) as KryptonTagKey<T>

        @JvmStatic
        fun <T> hashedCodec(registry: ResourceKey<out Registry<T>>): Codec<TagKey<T>> = Codec.STRING.comapFlatMap(
            { input -> if (input.startsWith('#')) Keys.read(input).map { of(registry, it) } else DataResult.error("$input is not a valid tag ID!") },
            { "#${it.location}" }
        )
    }
}
