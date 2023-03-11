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
package org.kryptonmc.krypton.registry.holder

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.tags.TagKey
import java.util.function.Predicate
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * A lookup for holders.
 */
interface HolderLookup<T> : HolderGetter<T> {

    fun listElements(): Stream<Holder.Reference<T>>

    fun listElementKeys(): Stream<ResourceKey<T>> = listElements().map { it.key() }

    fun listTags(): Stream<HolderSet.Named<T>>

    fun listTagKeys(): Stream<TagKey<T>> = listTags().map { it.key }

    fun filterElements(filter: Predicate<T>): HolderLookup<T> = object : Forwarding<T>() {
        override fun delegate(): HolderLookup<T> = this@HolderLookup

        override fun get(key: ResourceKey<T>): Holder.Reference<T>? = delegate().get(key)?.takeIf { filter.test(it.value()) }

        override fun listElements(): Stream<Holder.Reference<T>> = delegate().listElements().filter { filter.test(it.value()) }
    }

    abstract class Forwarding<T> : HolderLookup<T> {

        protected abstract fun delegate(): HolderLookup<T>

        override fun get(key: ResourceKey<T>): Holder.Reference<T>? = delegate().get(key)

        override fun listElements(): Stream<Holder.Reference<T>> = delegate().listElements()

        override fun get(key: TagKey<T>): HolderSet.Named<T>? = delegate().get(key)

        override fun listTags(): Stream<HolderSet.Named<T>> = delegate().listTags()
    }

    interface ForRegistry<T> : HolderLookup<T>, HolderOwner<T> {

        fun key(): ResourceKey<out Registry<out T>>

        abstract class Forwarding<T> : HolderLookup.Forwarding<T>(), ForRegistry<T> {

            abstract override fun delegate(): ForRegistry<T>

            override fun key(): ResourceKey<out Registry<out T>> = delegate().key()
        }
    }

    interface Provider {

        fun <T> lookup(key: ResourceKey<out Registry<out T>>): ForRegistry<T>?

        fun <T> lookupOrThrow(key: ResourceKey<out Registry<out T>>): ForRegistry<T> =
            lookup(key) ?: error("Required registry with key $key not found!")

        fun asGetterProvider(): HolderGetter.Provider = object : HolderGetter.Provider {
            override fun <T> lookup(key: ResourceKey<out Registry<out T>>): HolderGetter<T>? = this@Provider.lookup(key)
        }

        companion object {

            @JvmStatic
            fun create(lookups: Stream<ForRegistry<*>>): Provider {
                val lookupMap = lookups.collect(Collectors.toUnmodifiableMap({ it.key() }, { it }))
                return object : Provider {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T> lookup(key: ResourceKey<out Registry<out T>>): ForRegistry<T>? = lookupMap.get(key) as? ForRegistry<T>
                }
            }
        }
    }
}
