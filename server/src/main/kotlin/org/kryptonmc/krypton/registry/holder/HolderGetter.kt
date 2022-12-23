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
