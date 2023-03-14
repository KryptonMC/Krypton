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
