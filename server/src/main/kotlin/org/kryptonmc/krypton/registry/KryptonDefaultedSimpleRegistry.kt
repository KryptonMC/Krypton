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
import org.kryptonmc.krypton.registry.holder.Holder

/**
 * The defaulted registry implementation that stores a default key to search for when values are registered to store
 * the default value to return if no other value can be found.
 */
class KryptonDefaultedSimpleRegistry<T> private constructor(
    override val defaultKey: Key,
    key: ResourceKey<out Registry<T>>,
    intrusive: Boolean
) : KryptonSimpleRegistry<T>(key, intrusive), KryptonDefaultedRegistry<T> {

    private var defaultValue: Holder.Reference<T>? = null

    private fun defaultValue(): T = checkNotNull(defaultValue) { "The default value was not initialized!" }.value()

    override fun register(id: Int, key: ResourceKey<T>, value: T): Holder.Reference<T> {
        val holder = super.register(id, key, value)
        if (defaultKey == key.location) defaultValue = holder
        return holder
    }

    override fun get(key: Key): T = super.get(key) ?: defaultValue()

    override fun get(key: ResourceKey<T>): T = super.get(key) ?: defaultValue()

    override fun get(id: Int): T = super.get(id) ?: defaultValue()

    override fun getKey(value: T): Key = super.getKey(value) ?: defaultKey

    override fun getId(value: T): Int {
        val id = super.getId(value)
        return if (id == -1) super.getId(defaultValue()) else id
    }

    companion object {

        @JvmStatic
        fun <T> standard(key: ResourceKey<out Registry<T>>, defaultKey: Key): KryptonDefaultedSimpleRegistry<T> =
            KryptonDefaultedSimpleRegistry(defaultKey, key, false)

        @JvmStatic
        fun <T> intrusive(key: ResourceKey<out Registry<T>>, defaultKey: Key): KryptonDefaultedSimpleRegistry<T> =
            KryptonDefaultedSimpleRegistry(defaultKey, key, true)
    }
}
