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
import org.kryptonmc.api.effect.particle.ParticleType
import org.spongepowered.math.vector.Vector3d

/**
 * The base class for all dust particle effect builders. Used to abstract away
 * messy recursive builder logic.
 */
@Suppress("UNCHECKED_CAST")
public sealed class AbstractDustParticleEffectBuilder<B : AbstractDustParticleEffectBuilder<B>>(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector3d = Vector3d.ZERO,
    longDistance: Boolean = false,
    red: Short = 255,
    green: Short = 0,
    blue: Short = 0,
    protected var scale: Float = 0F
) : AbstractColorParticleEffectBuilder<B>(type, quantity, offset, longDistance, red, green, blue) {

    /**
     * Sets the scale of the dust particles.
     * Clamped between 0.1 and 4.0.
     *
     * @param scale the scale of the particles
     */
    @Contract("_ -> this", mutates = "this")
    public fun scale(scale: Float): B = apply { this.scale = scale } as B
}
