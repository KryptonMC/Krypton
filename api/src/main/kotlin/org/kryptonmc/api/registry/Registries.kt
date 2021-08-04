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
import org.kryptonmc.api.util.FACTORY_PROVIDER
import org.kryptonmc.api.util.provide

/**
 * Holder of all of the built-in registries
 */
object Registries {

    /**
     * The parent registry. All registries are a child of this.
     */
    @JvmField val PARENT = MANAGER.parent

    /**
     * All built-in vanilla registries
     */
    @JvmField val SOUND_EVENT = create(ResourceKeys.SOUND_EVENT)
    @JvmField val ENTITY_TYPE = createDefaulted(ResourceKeys.ENTITY_TYPE, key("pig"))
    @JvmField val PARTICLE_TYPE = create(ResourceKeys.PARTICLE_TYPE)
    @JvmField val BLOCK = create(ResourceKeys.BLOCK)
    @JvmField val ITEM = createDefaulted(ResourceKeys.ITEM, key("air"))
    @JvmField val MENU = create(ResourceKeys.MENU)

    /**
     * Custom built-in registries
     */
    @JvmField val GAMERULES = create(ResourceKeys.GAMERULES)

    /**
     * The registry of all attribute types in the game.
     */
    @JvmField
    val ATTRIBUTE = create(ResourceKeys.ATTRIBUTE)

    /**
     * The registry of all attribute [org.kryptonmc.api.entity.attribute.ModifierOperation]s
     * in the game.
     */
    @JvmField
    val MODIFIER_OPERATIONS = create(ResourceKeys.MODIFIER_OPERATIONS)

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

private val MANAGER = FACTORY_PROVIDER.provide<RegistryManager>()
