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
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.spongepowered.math.vector.Vector3d

/**
 * The base builder for building particle effects.
 */
@ParticleDsl
public interface BaseParticleEffectBuilder<B : BaseParticleEffectBuilder<B>> {

    /**
     * Sets the number of particles to be spawned by the particle effect.
     *
     * The quantity must be between 1 and 16384, inclusively.
     *
     * @param quantity the quantity
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun quantity(quantity: Int): B

    /**
     * Sets the offset the particles can be from the origin.
     *
     * @param offset the offset from the origin
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun offset(offset: Vector3d): B

    /**
     * Sets whether the particle effect can be viewed from a further distance
     * than normal.
     *
     * This value changes the maximum view distance, in blocks, that the
     * effect can be viewed from the location where the effect was spawned to
     * the following:
     * * If true, 65536 blocks
     * * If false, 256 blocks
     *
     * @param longDistance whether the effect can be viewed from a further
     * distance than normal
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun longDistance(longDistance: Boolean): B

    /**
     * Builds the particle effect.
     */
    @Contract("_ -> new", pure = true)
    public fun build(): ParticleEffect
}
