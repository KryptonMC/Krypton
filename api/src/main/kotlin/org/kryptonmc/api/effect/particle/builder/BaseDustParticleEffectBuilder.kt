/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.builder

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.ParticleDsl

/**
 * The base builder for building dust particle effects.
 */
public interface BaseDustParticleEffectBuilder<B : BaseDustParticleEffectBuilder<B>> : BaseColorParticleEffectBuilder<B> {

    /**
     * Sets the scale of the dust particles.
     *
     * @param scale the scale of the particles
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun scale(scale: Float): B
}
