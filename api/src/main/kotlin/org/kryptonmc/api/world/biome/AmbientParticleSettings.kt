/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import org.kryptonmc.internal.annotations.dsl.BiomeDsl

/**
 * Settings for ambient particles in biomes.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface AmbientParticleSettings {

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
    public interface Builder : AbstractBuilder<AmbientParticleSettings> {

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
        @Contract("_, _ -> this", mutates = "this")
        public fun particle(type: ParticleType, data: ParticleData?): Builder = type(type).data(data)

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
    @TypeFactory
    public interface Factory {

        public fun of(type: ParticleType, data: ParticleData?, probability: Float): AmbientParticleSettings

        public fun builder(type: ParticleType): Builder
    }

    public companion object {

        /**
         * Creates new ambient particle settings with the given values.
         *
         * @param type the type of particle
         * @param data the data of the particle
         * @param probability the probability
         * @return new ambient particle settings
         */
        @JvmStatic
        @Contract("_, _, _ -> new", pure = true)
        public fun of(type: ParticleType, data: ParticleData?, probability: Float): AmbientParticleSettings =
            Krypton.factory<Factory>().of(type, data, probability)

        /**
         * Creates a new builder for ambient particle settings.
         *
         * @param type the particle type
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: ParticleType): Builder = Krypton.factory<Factory>().builder(type)
    }
}
