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
package org.kryptonmc.krypton.registry

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
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
    fun <T> getRegistry(key: ResourceKey<out Registry<T>>): Registry<T>? =
        WRITABLE_PARENT.get(key as ResourceKey<WritableRegistry<*>>) as? Registry<T>

    private fun interface Bootstrap<T> {

        fun run(registry: KryptonRegistry<T>)
    }

    private class RegistryInitializationException(message: String, cause: Throwable) : RuntimeException(message, cause)
}
