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
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.VibrationParticleData
import org.spongepowered.math.vector.Vector3d

/**
 * Allows building a [ParticleEffect] for vibration particle effects using
 * method chaining.
 */
public class VibrationParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector3d = Vector3d.ZERO,
    longDistance: Boolean = false,
    private var origin: Vector3d = Vector3d.ZERO,
    private var destination: Vector3d = Vector3d.ZERO,
    private var ticks: Int = 0
) : AbstractParticleEffectBuilder<VibrationParticleEffectBuilder>(type, quantity, offset, longDistance) {

    /**
     * Sets the origin location from the given [position].
     *
     * @param position the origin position
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun origin(position: Vector3d): VibrationParticleEffectBuilder = apply { origin = position }

    /**
     * Sets the destination location from the given [position].
     *
     * @param position the destination position
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun destination(position: Vector3d): VibrationParticleEffectBuilder = apply { origin = position }

    /**
     * Sets the amount of ticks it will take to vibrate from the
     * origin to the destination.
     *
     * @param ticks the amount of ticks
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun ticks(ticks: Int): VibrationParticleEffectBuilder = apply { this.ticks = ticks }

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build(): ParticleEffect = ParticleEffect.of(
        type,
        quantity,
        offset,
        longDistance,
        VibrationParticleData.of(origin, destination, ticks)
    )
}
