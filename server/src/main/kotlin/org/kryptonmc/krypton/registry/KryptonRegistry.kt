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
package org.kryptonmc.krypton.registry

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.tags.TagSet
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.util.IntBiMap
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.successOrError
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataResult
import java.util.Optional
import java.util.stream.Stream
import java.util.stream.StreamSupport

/**
 * The base registry class that specifies the public API of the backend registries.
 */
abstract class KryptonRegistry<T>(final override val key: ResourceKey<out Registry<T>>) : Registry<T>, IntBiMap<T> {

    abstract override val keys: Set<Key>
    abstract override val registryKeys: Set<ResourceKey<T>>
    abstract override val entries: Set<Map.Entry<ResourceKey<T>, T>>
    abstract override val tagKeys: Set<TagKey<T>>
    abstract override val tags: Map<TagKey<T>, TagSet<T>>

    abstract override fun containsKey(key: Key): Boolean

    abstract override fun containsKey(key: ResourceKey<T>): Boolean

    abstract override fun getKey(value: T): Key?

    abstract override fun getResourceKey(value: T): ResourceKey<T>?

    abstract override fun getId(value: T): Int

    abstract override fun get(key: Key): T?

    abstract override fun get(key: ResourceKey<T>): T?

    fun getOrThrow(key: ResourceKey<T>): T = checkNotNull(get(key)) { "Could not find key $key in registry ${this.key}!" }

    abstract fun holders(): Stream<Holder.Reference<T>>

    abstract fun getHolder(id: Int): Holder<T>?

    abstract fun getHolder(key: ResourceKey<T>): Holder<T>?

    fun getHolderOrThrow(key: ResourceKey<T>): Holder<T> = checkNotNull(getHolder(key)) { "Could not find key $key in registry ${this.key}!" }

    abstract fun getOrCreateHolder(key: ResourceKey<T>): DataResult<Holder<T>>

    abstract fun getOrCreateHolderOrThrow(key: ResourceKey<T>): Holder<T>

    abstract fun createIntrusiveHolder(value: T): Holder.Reference<T>

    abstract override fun getTag(key: TagKey<T>): TagSet<T>?

    abstract fun getOrCreateTag(key: TagKey<T>): TagSet<T>

    abstract fun isKnownTagKey(key: TagKey<T>): Boolean

    abstract fun resetTags()

    abstract fun bindTags(tags: Map<TagKey<T>, List<Holder<T>>>)

    abstract fun tagNames(): Stream<TagKey<T>>

    fun asHolderIdMap(): IntBiMap<Holder<T>> = object : IntBiMap<Holder<T>> {

        override val size: Int
            get() = this@KryptonRegistry.size

        override fun get(id: Int): Holder<T>? = getHolder(id)

        override fun getId(value: Holder<T>): Int = this@KryptonRegistry.getId(value.value())

        override fun iterator(): Iterator<Holder<T>> = holders().iterator()
    }

    fun byNameCodec(): Codec<T> = Codecs.KEY.flatXmap(
        { Optional.ofNullable(get(it)).successOrError { "Unknown registry key $it in $key!" } },
        { value -> Optional.ofNullable(getResourceKey(value)).map { it.location }.successOrError { "Unknown registry element $value in $key!" } }
    )

    fun holderByNameCodec(): Codec<Holder<T>> = Codecs.KEY.flatXmap(
        { Optional.ofNullable(getHolder(KryptonResourceKey.of(key, it))).successOrError { "Unknown registry key $it in $key!" } },
        { holder -> holder.unwrapKey().map { it.location }.successOrError { "Unknown registry element $holder in $key!" } }
    )

    override fun stream(): Stream<T> = StreamSupport.stream(spliterator(), false)

    override fun toString(): String = "Registry[$key]"
}
