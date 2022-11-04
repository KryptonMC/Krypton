/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.builder

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.ParticleDsl
import org.kryptonmc.api.util.Color

/**
 * The base builder for building colour particle effects.
 */
@ParticleDsl
public interface BaseColorParticleEffectBuilder<B : BaseColorParticleEffectBuilder<B>> : BaseParticleEffectBuilder<B> {

    /**
     * Sets the color of the particle to the given [color].
     *
     * @param color the color
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun color(color: Color): B
}
