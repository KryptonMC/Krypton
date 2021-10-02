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
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.DirectionalParticleData
import org.spongepowered.math.vector.Vector3d

/**
 * Allows building a [ParticleEffect] for directional particle effects using
 * method chaining.
 */
public class DirectionalParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector3d = Vector3d.ZERO,
    longDistance: Boolean = false,
    private var direction: Vector3d? = null,
    private var velocity: Float = 0.0F
) : AbstractParticleEffectBuilder<DirectionalParticleEffectBuilder>(type, quantity, offset, longDistance) {

    /**
     * Sets the direction of the particles.
     *
     * @param direction the direction of the particles
     */
    @Contract("_ -> this", mutates = "this")
    public fun direction(direction: Vector3d): DirectionalParticleEffectBuilder = apply { this.direction = direction }

    /**
     * Sets the velocity of the particles.
     *
     * The actual velocity tends to vary largely for each particle type, so
     * it's quite arbitrary what this means.
     *
     * @param velocity the velocity of the particles
     */
    @Contract("_ -> this", mutates = "this")
    public fun velocity(velocity: Float): DirectionalParticleEffectBuilder = apply { this.velocity = velocity }

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build(): ParticleEffect = ParticleEffect.of(
        type,
        quantity,
        offset,
        longDistance,
        DirectionalParticleData.of(direction, velocity)
    )
}
