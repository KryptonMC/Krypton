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
 * A builder for building dust transition particle effects.
 */
@ParticleDsl
public interface DustTransitionParticleEffectBuilder : BaseDustParticleEffectBuilder<DustTransitionParticleEffectBuilder> {

    /**
     * Sets the colour to transition the particle to to the given [color].
     *
     * @param color the colour
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun toColor(color: Color): DustTransitionParticleEffectBuilder
}
