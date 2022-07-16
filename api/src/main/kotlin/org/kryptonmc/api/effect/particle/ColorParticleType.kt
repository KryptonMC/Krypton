/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.builder.ColorParticleEffectBuilder

/**
 * A type of particle that uses a colour for its appearance.
 */
public interface ColorParticleType : ScopedParticleType<ColorParticleEffectBuilder> {

    public companion object {

        /**
         * Creates a new colour particle type with the given [key].
         *
         * @param key the key
         * @return a new colour particle type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(key: Key): ColorParticleType = ParticleType.of(key)
    }
}
