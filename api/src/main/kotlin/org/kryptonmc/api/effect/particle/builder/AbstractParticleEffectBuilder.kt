/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.builder

import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.ParticleType
import org.spongepowered.math.vector.Vector3d

/**
 * The base class for all particle effect builders. Used to abstract away messy
 * recursive builder logic.
 */
@Suppress("UNCHECKED_CAST")
public sealed class AbstractParticleEffectBuilder<B : AbstractParticleEffectBuilder<B>>(
    protected val type: ParticleType,
    protected var quantity: Int = 1,
    protected var offset: Vector3d = Vector3d.ZERO,
    protected var longDistance: Boolean = false
) : Buildable.Builder<ParticleEffect> {

    /**
     * Sets the number of particles to be spawned by the [ParticleEffect].
     *
     * @param quantity the number of particles, must be between 1 and 16384
     * inclusively
     */
    @Contract("_ -> this", mutates = "this")
    public fun quantity(quantity: Int): B = apply { this.quantity = quantity } as B

    /**
     * Sets the offset the particles can be from the origin.
     *
     * @param offset the offset from the origin
     */
    @Contract("_ -> this", mutates = "this")
    public fun offset(offset: Vector3d): B = apply { this.offset = offset } as B

    /**
     * Sets if particles can be viewed from a further distance than normal.
     *
     * When false, the view distance is 256.
     * When true, the view distance is 65536.
     *
     * @param longDistance true for long view distance, false for normal view
     * distance
     */
    @Contract("_ -> this", mutates = "this")
    public fun longDistance(longDistance: Boolean): B = apply { this.longDistance = longDistance } as B
}
