/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.provide
import org.kryptonmc.api.world.biome.Biome

/**
 * Holder of all of the built-in registries.
 */
object Registries {

    private val MANAGER = FactoryProvider.INSTANCE.provide<RegistryManager>()

    /**
     * The parent registry. All registries should be a child of this registry.
     */
    @JvmField val PARENT = MANAGER.parent

    /**
     * All built-in vanilla registries.
     */
    @JvmField val SOUND_EVENT = get(ResourceKeys.SOUND_EVENT)!!
    @JvmField val ENTITY_TYPE = getDefaulted(ResourceKeys.ENTITY_TYPE)!!
    @JvmField val PARTICLE_TYPE = get(ResourceKeys.PARTICLE_TYPE)!!
    @JvmField val BLOCK = get(ResourceKeys.BLOCK)!!
    @JvmField val ITEM = getDefaulted(ResourceKeys.ITEM)!!
    @JvmField val MENU = get(ResourceKeys.MENU)!!
    @JvmField val ATTRIBUTE = get(ResourceKeys.ATTRIBUTE)!!
    @JvmField val BIOME = get(ResourceKeys.minecraft<Biome>("worldgen/biome"))!!
    @JvmField val STATISTIC_TYPE = get(ResourceKeys.STATISTIC_TYPE)!!
    @JvmField val CUSTOM_STATISTIC = create(ResourceKeys.CUSTOM_STATISTIC)
    @JvmField val CANVAS = getDefaulted(ResourceKeys.CANVAS)!!
    @JvmField val FLUID = get(ResourceKeys.FLUID)!!

    /**
     * Custom built-in registries.
     */
    @JvmField val GAMERULES = create(ResourceKeys.GAMERULES)
    @JvmField val MODIFIER_OPERATIONS = create(ResourceKeys.MODIFIER_OPERATIONS)
    @JvmField val CRITERIA = create(ResourceKeys.CRITERIA)

    /**
     * Gets the existing registry with the given resource [key], or returns null
     * if there is no existing registry with the given resource [key].
     *
     * @param key the key
     * @return the existing registry, or null if not present
     */
    @JvmStatic
    operator fun <T : Any> get(key: ResourceKey<out Registry<T>>): Registry<T>? = MANAGER[key]

    /**
     * Gets the existing defaulted registry with the given resource [key], or
     * returns null if there is no existing defaulted registry with the given
     * resource [key].
     *
     * @param key the key
     * @return the existing defaulted registry, or null if not present
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : Any> getDefaulted(key: ResourceKey<out Registry<T>>): DefaultedRegistry<T>? = MANAGER.getDefaulted(key)

    /**
     * Registers a new entry to the given [registry], with the given [key] mapped to
     * the given [value].
     *
     * @param registry the registry to register to
     * @param key the key
     * @param value the value
     */
    @JvmStatic
    fun <T : Any> register(registry: Registry<T>, key: String, value: T): T = register(registry, key(key), value)

    /**
     * Registers a new entry to the given [registry], with the given [key] mapped to
     * the given [value].
     *
     * @param registry the registry to register to
     * @param key the key
     * @param value the value
     */
    @JvmStatic
    fun <T : Any> register(registry: Registry<T>, key: Key, value: T): T = MANAGER.register(registry, key, value)

    /**
     * Registers a new entry to the given [registry], with the given [key] mapped to
     * the given [value].
     *
     * @param registry the registry to register to
     * @param id the ID of the entry in the registry
     * @param key the key
     * @param value the value
     */
    @JvmStatic
    fun <T : Any> register(registry: Registry<T>, id: Int, key: String, value: T): T = register(registry, id, key, value)

    /**
     * Registers a new entry to the given [registry], with the given [key] mapped to
     * the given [value].
     *
     * @param registry the registry to register to
     * @param id the ID of the entry in the registry
     * @param key the key
     * @param value the value
     */
    @JvmStatic
    fun <T : Any> register(registry: Registry<T>, id: Int, key: Key, value: T): T = MANAGER.register(registry, id, key, value)

    /**
     * Creates a new registry with the given registry [key].
     *
     * @param key the registry key
     * @return a registry for the given [key]
     */
    @JvmStatic
    fun <T : Any> create(key: ResourceKey<out Registry<T>>) = MANAGER.create(key)

    /**
     * Creates a new registry with the given registry [key], with a [defaultKey].
     *
     * The default value for this registry will be the first value registered that has
     * a key that matches the given [defaultKey].
     *
     * @param key the registry key
     * @param defaultKey the default key
     * @return a defaulted registry for the given [key]
     */
    @JvmStatic
    fun <T : Any> createDefaulted(key: ResourceKey<out Registry<T>>, defaultKey: Key) = MANAGER.createDefaulted(key, defaultKey)
}
