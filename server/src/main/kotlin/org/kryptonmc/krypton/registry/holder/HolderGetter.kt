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

/**
 * An object that holders and tag values (holder sets) can be retrieved from.
 */
interface HolderGetter<T> {

    fun get(key: ResourceKey<T>): Holder.Reference<T>?

    fun getOrThrow(key: ResourceKey<T>): Holder.Reference<T> = get(key) ?: error("Missing required element for key $key!")

    fun get(key: TagKey<T>): HolderSet.Named<T>?

    fun getOrThrow(key: TagKey<T>): HolderSet.Named<T> = get(key) ?: error("Missing required tag values for key $key!")

    /**
     * A provider for holder getters.
     */
    interface Provider {

        fun <T> lookup(key: ResourceKey<out Registry<out T>>): HolderGetter<T>?

        fun <T> lookupOrThrow(key: ResourceKey<out Registry<out T>>): HolderGetter<T> =
            lookup(key) ?: error("Required registry with key $key not found!")
    }
}
