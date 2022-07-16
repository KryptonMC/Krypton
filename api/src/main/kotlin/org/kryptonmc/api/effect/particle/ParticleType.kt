/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.effect.particle.builder.BaseParticleEffectBuilder
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * A type of particle effect.
 */
@CataloguedBy(ParticleTypes::class)
public interface ParticleType : Keyed {

    /**
     * Constructs a new builder to build a new [ParticleEffect] of this type.
     */
    @Contract("_ -> new", pure = true)
    public fun builder(): BaseParticleEffectBuilder<*>

    @ApiStatus.Internal
    public interface Factory {

        public fun <T : ParticleType> of(type: Class<T>, key: Key): T
    }

    public companion object {

        @JvmSynthetic
        internal val FACTORY = Krypton.factoryProvider.provide<Factory>()
    }
}

@JvmSynthetic
internal inline fun <reified T : ParticleType> ParticleType.Companion.of(key: Key): T = FACTORY.of(T::class.java, key)
