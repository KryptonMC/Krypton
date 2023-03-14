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
package org.kryptonmc.krypton.registry

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.tags.TagSet
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.registry.holder.HolderLookup
import org.kryptonmc.krypton.registry.holder.HolderOwner
import org.kryptonmc.krypton.registry.holder.HolderSet
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.tags.KryptonTagSet
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.util.map.IntBiMap
import org.kryptonmc.krypton.util.successOrError
import org.kryptonmc.serialization.Codec
import java.util.Optional
import java.util.stream.Stream
import java.util.stream.StreamSupport

/**
 * The base registry class that specifies the public API of the backend registries.
 */
interface KryptonRegistry<T> : Registry<T>, IntBiMap<T> {

    override fun containsKey(key: Key): Boolean

    override fun containsKey(key: ResourceKey<T>): Boolean

    override fun getKey(value: T): Key?

    override fun getResourceKey(value: T): ResourceKey<T>?

    override fun getId(value: T): Int

    override fun get(key: Key): T?

    override fun get(key: ResourceKey<T>): T?

    fun getOrThrow(key: ResourceKey<T>): T = checkNotNull(get(key)) { "Could not find key $key in registry ${this.key}!" }

    fun holders(): Collection<Holder.Reference<T>>

    fun getHolder(id: Int): Holder.Reference<T>?

    fun getHolder(key: ResourceKey<T>): Holder.Reference<T>?

    fun getHolderOrThrow(key: ResourceKey<T>): Holder.Reference<T> =
        checkNotNull(getHolder(key)) { "Could not find key $key in registry ${this.key}!" }

    fun wrapAsHolder(value: T & Any): Holder<T>

    fun createIntrusiveHolder(value: T): Holder.Reference<T>

    fun getTag(key: TagKey<T>): HolderSet.Named<T>?

    fun getTagOrEmpty(key: TagKey<T>): Iterable<Holder<T>> = getTag(key) ?: ImmutableLists.of()

    override fun getTagValues(key: TagKey<T>): TagSet<T>? = getTag(key)?.let { KryptonTagSet(this, it) }

    fun getOrCreateTag(key: TagKey<T>): HolderSet.Named<T>

    fun isKnownTagKey(key: TagKey<T>): Boolean

    fun resetTags()

    fun bindTags(tags: Map<TagKey<T>, List<Holder<T>>>)

    fun tagEntries(): Map<TagKey<T>, HolderSet.Named<T>>

    fun tagNames(): Stream<TagKey<T>>

    fun freeze(): KryptonRegistry<T>

    fun holderOwner(): HolderOwner<T>

    fun asLookup(): HolderLookup.ForRegistry<T>

    fun asTagAddingLookup(): HolderLookup.ForRegistry<T> = object : HolderLookup.ForRegistry.Forwarding<T>() {
        override fun delegate(): HolderLookup.ForRegistry<T> = asLookup()

        override fun get(key: TagKey<T>): HolderSet.Named<T> = getOrThrow(key)

        override fun getOrThrow(key: TagKey<T>): HolderSet.Named<T> = getOrCreateTag(key)
    }

    fun asHolderIdMap(): IntBiMap<Holder<T>> = object : IntBiMap<Holder<T>> {

        override fun size(): Int = this@KryptonRegistry.size()

        override fun get(id: Int): Holder<T>? = getHolder(id)

        override fun getId(value: Holder<T>): Int = this@KryptonRegistry.getId(value.value())

        override fun iterator(): Iterator<Holder<T>> = holders().iterator()
    }

    fun byNameCodec(): Codec<T> = Keys.CODEC.flatXmap(
        { Optional.ofNullable(get(it)).successOrError { "Unknown registry key $it in $key!" } },
        { value -> Optional.ofNullable(getResourceKey(value)).map { it.location }.successOrError { "Unknown registry element $value in $key!" } }
    )

    fun holderByNameCodec(): Codec<Holder<T>> = Keys.CODEC.flatXmap(
        { Optional.ofNullable(getHolder(KryptonResourceKey.of(key, it))).successOrError { "Unknown registry key $it in $key!" } },
        { holder -> holder.unwrapKey().map { it.location }.successOrError { "Unknown registry element $holder in $key!" } }
    )

    override fun stream(): Stream<T> = StreamSupport.stream(spliterator(), false)
}
