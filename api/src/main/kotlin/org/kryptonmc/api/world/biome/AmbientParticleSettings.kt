/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.provide

/**
 * Settings for ambient particles in biomes.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface AmbientParticleSettings {

    /**
     * The type of the particle for these settings.
     */
    @get:JvmName("particle")
    public val particle: ParticleType

    /**
     * The data for the above particle.
     */
    @get:JvmName("particleData")
    public val particleData: ParticleData?

    /**
     * The chance that particles of the above type will appear naturally in the
     * biome.
     */
    @get:JvmName("probability")
    public val probability: Float

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(particle: ParticleType, particleData: ParticleData?, probability: Float): AmbientParticleSettings
    }

    public companion object {

        private val FACTORY = FactoryProvider.INSTANCE.provide<Factory>()

        /**
         * Creates new ambient particle settings with the given values.
         *
         * @param particle the type of particle
         * @param particleData the data of the particle
         * @param probability the probability
         * @return new ambient particle settings
         */
        @JvmStatic
        public fun of(
            particle: ParticleType,
            particleData: ParticleData?,
            probability: Float
        ): AmbientParticleSettings = FACTORY.of(particle, particleData, probability)
    }
}
