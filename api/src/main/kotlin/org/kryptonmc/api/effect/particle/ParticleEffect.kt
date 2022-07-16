/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import org.kryptonmc.api.effect.particle.builder.BaseParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.spongepowered.math.vector.Vector3d

/**
 * Holds information used to spawn particles for a player.
 *
 * This effect is entirely immutable, and so is safe for both storage and
 * reuse.
 *
 * Effect instances can only be created through particle effect builders (any
 * subclass of [BaseParticleEffectBuilder]).
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ParticleEffect {

    /**
     * The type of this effect.
     */
    @get:JvmName("type")
    public val type: ParticleType

    /**
     * The amount of this effect that should be spawned.
     */
    @get:JvmName("quantity")
    public val quantity: Int

    /**
     * The offset vector from the spawn location of this effect.
     */
    @get:JvmName("offset")
    public val offset: Vector3d

    /**
     * If the distance of this effect is longer than usual.
     *
     * Specifically, the distance will increase from 256 to 65536.
     */
    @get:JvmName("longDistance")
    public val longDistance: Boolean

    /**
     * The extra data for this effect.
     *
     * May be null if this effect does not have any extra data.
     */
    @get:JvmName("data")
    public val data: ParticleData?
}
