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

/**
 * Holder of all of the built-in registries
 */
object Registries {

    private val WRITABLE_REGISTRY: WritableRegistry<WritableRegistry<*>> = MappedRegistry(RegistryKeys.create("root"))

    /**
     * The root registry, which is the parent of all other registries.
     */
    @JvmField
    val REGISTRY: Registry<*> = WRITABLE_REGISTRY

    /**
     * The registry of all sound events in the game.
     */
    @JvmField
    val SOUND_EVENT = create(RegistryKeys.SOUND_EVENT)

    /**
     * The registry of all types of entities in the game.
     */
    @JvmField
    val ENTITY_TYPE = createDefaulted(RegistryKeys.ENTITY_TYPE, "pig")

    /**
     * The registry of all types of particles in the game.
     */
    @JvmField
    val PARTICLE_TYPE = create(RegistryKeys.PARTICLE_TYPE)

    /**
     * Registers a new entry to the given [registry], with the given [key] mapped to
     * the given [value].
     *
     * @param registry the registry to register to
     * @param key the key
     * @param value the value
     */
    fun <T> register(registry: Registry<T>, key: String, value: T): T = register(registry, Key.key(key), value)

    /**
     * Registers a new entry to the given [registry], with the given [key] mapped to
     * the given [value].
     *
     * @param registry the registry to register to
     * @param key the key
     * @param value the value
     */
    fun <T> register(registry: Registry<T>, key: Key, value: T): T =
        (registry as WritableRegistry<T>).register(RegistryKey.of(registry.key, key), value)

    /**
     * Creates a new registry with the given registry [key].
     *
     * @param key the registry key
     * @return a registry for the given [key]
     */
    @JvmStatic
    fun <T> create(key: RegistryKey<out Registry<T>>) = internalRegister(key, MappedRegistry(key))

    /**
     * Creates a new registry with the given registry [key], with a [defaultKey].
     *
     * @param key the registry key
     * @param defaultKey the default key
     * @return a defaulted registry for the given [key]
     */
    @JvmStatic
    fun <T> createDefaulted(
        key: RegistryKey<out Registry<T>>,
        defaultKey: String
    ): DefaultedRegistry<T> = internalRegister(key, DefaultedRegistry(defaultKey, key))

    @Suppress("UNCHECKED_CAST") // This is fine
    private fun <T, R : WritableRegistry<T>> internalRegister(
        key: RegistryKey<out Registry<T>>,
        registry: R
    ): R = (WRITABLE_REGISTRY as WritableRegistry<R>).register(key as RegistryKey<R>, registry)
}
