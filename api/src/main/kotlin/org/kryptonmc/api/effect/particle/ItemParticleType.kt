/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.builder.ItemParticleEffectBuilder

/**
 * A type of particle that uses an item texture for its appearance.
 */
public interface ItemParticleType : ScopedParticleType<ItemParticleEffectBuilder> {

    public companion object {

        /**
         * Creates a new item particle type with the given [key].
         *
         * @param key the key
         * @return a new item particle type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(key: Key): ItemParticleType = ParticleType.FACTORY.item(key)
    }
}
