/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.builder.BaseParticleEffectBuilder
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A type of particle effect.
 */
@CataloguedBy(ParticleTypes::class)
@ImmutableType
public interface ParticleType : Keyed {

    /**
     * Constructs a new builder to build a new [ParticleEffect] of this type.
     */
    @Contract("_ -> new", pure = true)
    public fun builder(): BaseParticleEffectBuilder<*>
}
