/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.util.provide

/**
 * Settings for ambient particles in biomes.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface AmbientParticleSettings : Buildable<AmbientParticleSettings, AmbientParticleSettings.Builder> {

    /**
     * The type of the particle for these settings.
     */
    @get:JvmName("type")
    public val type: ParticleType

    /**
     * The data for the particle. This may be null, in which case either the
     * particle type has no associated particle data, or there is no data for
     * the particle in these settings.
     */
    @get:JvmName("data")
    public val data: ParticleData?

    /**
     * The chance that particles of the above type will appear naturally in the
     * biome.
     */
    @get:JvmName("probability")
    public val probability: Float

    /**
     * A builder for ambient particle settings.
     *
     * Note: Attempting to call [Builder.build] on this without setting the
     * particle type will throw an [IllegalStateException].
     */
    @BiomeDsl
    public interface Builder : Buildable.Builder<AmbientParticleSettings> {

        /**
         * Sets the type of the particle for the ambient particle settings to
         * the given [type].
         *
         * @param type the type
         * @return this builder
         * @see AmbientParticleSettings.type
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun type(type: ParticleType): Builder

        /**
         * Sets the particle data for the ambient particle settings to the
         * given [data].
         *
         * @param data the data
         * @return this builder
         * @see AmbientParticleSettings.data
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun data(data: ParticleData?): Builder

        /**
         * Sets the particle type and data for the ambient particle settings
         * to the given [type] and [data].
         *
         * @param type the type
         * @param data the data
         * @return this builder
         * @see Builder.type
         * @see Builder.data
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun particle(type: ParticleType, data: ParticleData?): Builder

        /**
         * Sets the probability for the ambient particle settings to the given
         * [probability].
         *
         * @param probability the probability
         * @return this builder
         * @see AmbientParticleSettings.probability
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun probability(probability: Float): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun of(type: ParticleType, data: ParticleData?, probability: Float): AmbientParticleSettings

        public fun builder(type: ParticleType): Builder
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates new ambient particle settings with the given values.
         *
         * @param type the type of particle
         * @param data the data of the particle
         * @param probability the probability
         * @return new ambient particle settings
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(
            type: ParticleType,
            data: ParticleData?,
            probability: Float
        ): AmbientParticleSettings = FACTORY.of(type, data, probability)

        /**
         * Creates a new builder for ambient particle settings.
         *
         * @param type the particle type
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: ParticleType): Builder = FACTORY.builder(type)
    }
}
