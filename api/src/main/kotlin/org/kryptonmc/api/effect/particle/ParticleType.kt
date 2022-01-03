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

        public fun block(key: Key): BlockParticleType

        public fun color(key: Key): ColorParticleType

        public fun directional(key: Key): DirectionalParticleType

        public fun dust(key: Key): DustParticleType

        public fun dustTransition(key: Key): DustTransitionParticleType

        public fun item(key: Key): ItemParticleType

        public fun note(key: Key): NoteParticleType

        public fun simple(key: Key): SimpleParticleType

        public fun vibration(key: Key): VibrationParticleType
    }

    public companion object {

        @JvmSynthetic
        internal val FACTORY = Krypton.factoryProvider.provide<Factory>()
    }
}
