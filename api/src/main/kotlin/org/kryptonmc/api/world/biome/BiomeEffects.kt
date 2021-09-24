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
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.effect.Music
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.provide
import java.awt.Color

/**
 * The effects for a biome. These control various things, including colouring,
 * ambient particles, sounds, and music.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BiomeEffects {

    /**
     * The colour that fog will appear when in the biome.
     */
    @get:JvmName("fogColor")
    public val fogColor: Color

    /**
     * The colour that water will appear when in the biome.
     */
    @get:JvmName("waterColor")
    public val waterColor: Color

    /**
     * The colour that water will appear when viewed through fog in the biome.
     */
    @get:JvmName("waterFogColor")
    public val waterFogColor: Color

    /**
     * The colour that the sky will appear when in the biome.
     */
    @get:JvmName("skyColor")
    public val skyColor: Color

    /**
     * The modifier for the colour of grass in the biome.
     */
    @get:JvmName("grassColorModifier")
    public val grassColorModifier: GrassColorModifier

    /**
     * The colour that foliage, such as tree leaves and vines, will appear when
     * in the biome.
     *
     * This may be null, in which case it will be calculated using the humidity
     * and temperature of the biome.
     */
    @get:JvmName("foliageColor")
    public val foliageColor: Color?

    /**
     * The colour that grass, such as grass blocks and tall grass, will appear
     * when in the biome.
     *
     * This may be null, in which case it will be calculated using the humidity
     * and temperature of the biome.
     */
    @get:JvmName("grassColor")
    public val grassColor: Color?

    /**
     * Settings for ambient particle effects that may appear randomly when in
     * the biome.
     *
     * This may be null, in which case no particles will appear.
     */
    @get:JvmName("ambientParticleSettings")
    public val ambientParticleSettings: AmbientParticleSettings?

    /**
     * The ambient sound that should be played and put on loop until the biome
     * is left.
     *
     * This may be null, in which case no sound will play.
     */
    @get:JvmName("ambientLoopSound")
    public val ambientLoopSound: SoundEvent?

    /**
     * Settings for the ambient mood sound that will start playing after a
     * fixed delay and in a specific range.
     *
     * This may be null, in which case no mood sound will play.
     */
    @get:JvmName("ambientMoodSettings")
    public val ambientMoodSettings: AmbientMoodSettings?

    /**
     * Settings for the ambient additions sound that may play randomly when in
     * the biome.
     *
     * This may be null, in which case no additions sound will play.
     */
    @get:JvmName("ambientAdditionsSettings")
    public val ambientAdditionsSettings: AmbientAdditionsSettings?

    /**
     * The music that will be played in the background when in the biome.
     *
     * This may be null, in which case no music will be played.
     */
    @get:JvmName("backgroundMusic")
    public val backgroundMusic: Music?

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(
            fogColor: Color,
            waterColor: Color,
            waterFogColor: Color,
            skyColor: Color,
            grassColorModifier: GrassColorModifier,
            foliageColor: Color?,
            grassColor: Color?,
            ambientParticleSettings: AmbientParticleSettings?,
            ambientLoopSound: SoundEvent?,
            ambientMoodSettings: AmbientMoodSettings?,
            ambientAdditionsSettings: AmbientAdditionsSettings?,
            backgroundMusic: Music?
        ): BiomeEffects
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates new biome effects with the given values.
         *
         * @param fogColor the colour of fog
         * @param waterColor the colour of water
         * @param waterFogColor the colour of foggy water
         * @param skyColor the colour of the sky
         * @param grassColorModifier the modifier for the grass colour
         * @param foliageColor the colour of foliage, will be calculated if not
         * present
         * @param grassColor the colour of grass, will be calculated if not
         * present
         * @param ambientParticleSettings the settings for ambient particles
         * @param ambientLoopSound the ambient sound that loops continuously
         * @param ambientMoodSettings the settings for the mood sound
         * @param ambientAdditionsSettings the settings for the addition sound
         * @param backgroundMusic the background music that plays continuously
         * @return new biome effects
         */
        @JvmStatic
        @JvmOverloads
        public fun of(
            fogColor: Color,
            waterColor: Color,
            waterFogColor: Color,
            skyColor: Color,
            grassColorModifier: GrassColorModifier,
            foliageColor: Color? = null,
            grassColor: Color? = null,
            ambientParticleSettings: AmbientParticleSettings? = null,
            ambientLoopSound: SoundEvent? = null,
            ambientMoodSettings: AmbientMoodSettings? = null,
            ambientAdditionsSettings: AmbientAdditionsSettings? = null,
            backgroundMusic: Music? = null
        ): BiomeEffects = FACTORY.of(
            fogColor,
            waterColor,
            waterFogColor,
            skyColor,
            grassColorModifier,
            foliageColor,
            grassColor,
            ambientParticleSettings,
            ambientLoopSound,
            ambientMoodSettings,
            ambientAdditionsSettings,
            backgroundMusic
        )
    }
}
