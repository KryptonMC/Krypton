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
import org.spongepowered.math.vector.Vector3d

/**
 * A builder for building vibration particle effects.
 */
public interface VibrationParticleEffectBuilder : BaseParticleEffectBuilder<VibrationParticleEffectBuilder> {

    /**
     * Sets the destination location from the given [position].
     *
     * @param position the destination position
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun destination(position: Vector3d): VibrationParticleEffectBuilder

    /**
     * Sets the amount of ticks it will take to vibrate from the
     * origin to the destination.
     *
     * @param ticks the amount of ticks
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun ticks(ticks: Int): VibrationParticleEffectBuilder
}
