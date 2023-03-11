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
package org.kryptonmc.krypton.registry.network

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.holder.HolderGetter
import org.kryptonmc.krypton.registry.holder.HolderOwner
import org.kryptonmc.krypton.util.serialization.DelegatingOps
import org.kryptonmc.serialization.DataOps

class RegistryOps<T> private constructor(delegate: DataOps<T>, private val lookupProvider: RegistryInfoLookup) : DelegatingOps<T>(delegate) {

    fun <E> owner(key: ResourceKey<out Registry<out E>>): HolderOwner<E>? = lookupProvider.lookup(key)?.owner

    fun <E> getter(key: ResourceKey<out Registry<out E>>): HolderGetter<E>? = lookupProvider.lookup(key)?.getter

    @JvmRecord
    data class RegistryInfo<T>(val owner: HolderOwner<T>, val getter: HolderGetter<T>)

    interface RegistryInfoLookup {

        fun <T> lookup(key: ResourceKey<out Registry<out T>>): RegistryInfo<T>?
    }
}
