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
import org.spongepowered.math.vector.Vector3d

/**
 * A builder for building directional particle effects.
 */
public interface DirectionalParticleEffectBuilder : BaseParticleEffectBuilder<DirectionalParticleEffectBuilder> {

    /**
     * Sets the direction that the particle effect will travel.
     *
     * @param direction the direction
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun direction(direction: Vector3d): DirectionalParticleEffectBuilder

    /**
     * Sets the velocity that the particle effect will travel with.
     *
     * The actual velocity tends to vary largely for each particle type, so
     * it's quite arbitrary what this means.
     *
     * @param velocity the velocity
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun velocity(velocity: Float): DirectionalParticleEffectBuilder
}
