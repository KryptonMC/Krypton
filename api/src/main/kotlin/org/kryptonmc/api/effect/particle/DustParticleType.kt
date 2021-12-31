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
import org.kryptonmc.api.effect.particle.builder.DustParticleEffectBuilder

/**
 * A type of particle that uses a colour and scale for its appearance.
 */
public interface DustParticleType : ScopedParticleType<DustParticleEffectBuilder> {

    public companion object {

        /**
         * Creates a new dust particle type with the given [key].
         *
         * @param key the key
         * @return a new dust particle type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(key: Key): DustParticleType = ParticleType.FACTORY.dust(key)
    }
}
