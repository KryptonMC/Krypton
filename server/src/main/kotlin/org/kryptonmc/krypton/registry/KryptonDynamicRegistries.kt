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

import com.google.common.collect.Collections2
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.DefaultedRegistry
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryHolder
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.network.chat.ChatTypes
import org.kryptonmc.krypton.network.chat.RichChatType
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.world.biome.KryptonBiomeRegistrar
import org.kryptonmc.krypton.world.dimension.KryptonDimensionTypes

object KryptonDynamicRegistries {

    private val LOADERS = LinkedHashMap<Key, Runnable>()
    private val WRITABLE_PARENT: WritableRegistry<WritableRegistry<*>> = KryptonSimpleRegistry.standard(KryptonResourceKeys.PARENT)
    @JvmField
    val PARENT: KryptonRegistry<out KryptonRegistry<*>> = WRITABLE_PARENT

    @JvmField
    val DIMENSION_TYPE: KryptonRegistry<DimensionType> = register(ResourceKeys.DIMENSION_TYPE) { KryptonDimensionTypes }
    @JvmField
    val BIOME: KryptonRegistry<Biome> = register(ResourceKeys.BIOME) { KryptonBiomeRegistrar.bootstrap() }
    @JvmField
    val CHAT_TYPE: KryptonRegistry<RichChatType> = register(KryptonResourceKeys.CHAT_TYPE, ChatTypes::bootstrap)

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    private fun <T> register(key: ResourceKey<out Registry<T>>, loader: Bootstrap<T>): KryptonRegistry<T> {
        val registry = KryptonSimpleRegistry.standard(key)
        LOADERS.put(key.location) { loader.run(registry) }
        WRITABLE_PARENT.register(key as ResourceKey<WritableRegistry<*>>, registry)
        return registry
    }

    @JvmStatic
    fun bootstrap() {
        WRITABLE_PARENT.freeze()
        runLoaders()
        WRITABLE_PARENT.forEach { it.freeze() }
        KryptonRegistries.validateAll(WRITABLE_PARENT)
    }

    @JvmStatic
    private fun runLoaders() {
        LOADERS.forEach { (key, action) ->
            try {
                action.run()
            } catch (exception: Exception) {
                throw RegistryInitializationException("Failed to bootstrap registry $key!", exception)
            }
            requireNotNull(WRITABLE_PARENT.get(key)) { "Cannot find registry for key $key in loading!" }.freeze()
        }
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> getRegistry(key: ResourceKey<out Registry<T>>): Registry<T>? {
        return WRITABLE_PARENT.get(key as ResourceKey<WritableRegistry<*>>) as? Registry<T>
    }

    private fun interface Bootstrap<T> {

        fun run(registry: KryptonRegistry<T>)
    }

    private class RegistryInitializationException(message: String, cause: Throwable) : RuntimeException(message, cause)

    object DynamicHolder : RegistryHolder {

        override val registries: Collection<Registry<*>>
            get() = Collections2.transform(PARENT.holders()) { it.value() }

        override fun <E> getRegistry(key: ResourceKey<out Registry<E>>): Registry<E>? = KryptonDynamicRegistries.getRegistry(key)

        @Suppress("UNCHECKED_CAST")
        override fun <E> getDefaultedRegistry(key: ResourceKey<out Registry<E>>): DefaultedRegistry<E>? {
            return WRITABLE_PARENT.get(key as ResourceKey<WritableRegistry<*>>) as? DefaultedRegistry<E>
        }
    }
}
