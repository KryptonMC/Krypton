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
import org.kryptonmc.api.effect.particle.builder.DirectionalParticleEffectBuilder

/**
 * A type of particle that can have velocity applied in a direction.
 */
public interface DirectionalParticleType : ScopedParticleType<DirectionalParticleEffectBuilder> {

    public companion object {

        /**
         * Creates a new directional particle type with the given [key].
         *
         * @param key the key
         * @return a new directional particle type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(key: Key): DirectionalParticleType = ParticleType.FACTORY.directional(key)
    }
}
