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
package org.kryptonmc.krypton.registry.dynamic

import com.google.common.collect.Collections2
import org.kryptonmc.api.registry.DefaultedRegistry
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryHolder
import org.kryptonmc.api.resource.ResourceKey
import java.util.function.Predicate

class FilteredRegistryHolder(private val backing: RegistryHolder, private val filter: Predicate<ResourceKey<out Registry<*>>>) : RegistryHolder {

    override val registries: Collection<Registry<*>>
        get() = Collections2.filter(backing.registries) { filter.test(it.key) }

    override fun <E> getRegistry(key: ResourceKey<out Registry<E>>): Registry<E>? {
        if (!filter.test(key)) return null
        return backing.getRegistry(key)
    }

    override fun <E> getDefaultedRegistry(key: ResourceKey<out Registry<E>>): DefaultedRegistry<E>? {
        if (!filter.test(key)) return null
        return backing.getDefaultedRegistry(key)
    }
}
