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
package org.kryptonmc.krypton.registry.loader

import net.kyori.adventure.key.Key

/**
 * A loader that is used to store values that will be registered to a registry.
 *
 * This is used to decouple the registration of values from the registry loading itself, which declutters the registry constants
 * class, and also, this replaces the need to create an implementation object to register all of the implementation values.
 */
class RegistryLoader<T> {

    private val mappings = HashMap<Key, T>()

    /**
     * This method exists to avoid having to store a separate Key object for each value to reuse it between the key in the
     * mappings and the key that may be required for the registry object, which would be a mess.
     */
    inline fun add(key: Key, function: (Key) -> T) {
        put(key, function(key))
    }

    /**
     * This method purely exists to avoid having to make mappings internal, as `add` is inline and needs something
     * public to call. The only place this method is called is in `add`.
     */
    fun put(key: Key, value: T) {
        mappings.put(key, value)
    }

    /**
     * Performs the action on every value present in the mappings for this loader.
     *
     * This method is used by the bootstrap loaders created by [KryptonRegistries] in order to register all the
     * registry values to the target registry from this loader, and avoids the need to directly access the mappings,
     * which encapsulates the internals of the loader.
     */
    internal inline fun forEach(action: (Key, T) -> Unit) {
        mappings.forEach { action(it.key, it.value) }
    }
}
