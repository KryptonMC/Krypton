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

/**
 * All the built-in registry keys for various registries.
 */
object RegistryKeys {

    // @formatter:off
    @JvmField val SOUND_EVENT = create<SoundType>("sound_event")
    @JvmField val ENTITY_TYPE = create<EntityType<*>>("entity_type")
    @JvmField val PARTICLE_TYPE = create<Particle>("particle_type")
    // @formatter:on
    /**
     * Creates a new registry key with the given [key] as its base key.
     *
     * This will use [RegistryRoots.MINECRAFT] as its root.
     *
     * @param key the key
     * @return a new registry key
     */
    fun <T> create(key: String) = RegistryKey.of<T>(Key.key(key))
}
