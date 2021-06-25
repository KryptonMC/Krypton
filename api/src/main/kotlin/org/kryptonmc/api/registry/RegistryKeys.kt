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
import org.kryptonmc.api.effect.particle.Particle
import org.kryptonmc.api.effect.sound.SoundType
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.world.rule.GameRule

/**
 * All the built-in registry keys for various registries.
 */
object RegistryKeys {

    // @formatter:off
    /**
     * Built-in vanilla registries
     */
    @JvmField val SOUND_EVENT = minecraft<SoundType>("sound_event")
    @JvmField val ENTITY_TYPE = minecraft<EntityType<*>>("entity_type")
    @JvmField val PARTICLE_TYPE = minecraft<Particle>("particle_type")

    /**
     * Custom built-in registries
     */
    @JvmField val GAMERULES = krypton<GameRule<Any>>("gamerules")
    // @formatter:on

    /**
     * Creates a new registry key with the given [key] as its base key.
     *
     * This will use [RegistryRoots.MINECRAFT] as its root.
     *
     * @param key the key
     * @return a new registry key
     */
    @JvmStatic
    fun <T : Any> minecraft(key: String): RegistryKey<out Registry<T>> = RegistryKey(Key.key(key))

    /**
     * Creates a new registry key with the given [key] as its base key.
     *
     * This will use [RegistryRoots.KRYPTON] as its root.
     *
     * @param key the key
     * @return a new registry key
     */
    @JvmStatic
    fun <T : Any> krypton(key: String): RegistryKey<out Registry<T>> = RegistryKey(RegistryRoots.KRYPTON, Key.key("krypton", key))
}
