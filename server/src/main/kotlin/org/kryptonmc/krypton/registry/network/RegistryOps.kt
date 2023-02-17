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
