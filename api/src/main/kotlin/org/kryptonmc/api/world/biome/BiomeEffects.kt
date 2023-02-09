/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.effect.Music
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.Color
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import org.kryptonmc.internal.annotations.dsl.BiomeDsl
import java.util.function.Consumer

/**
 * The effects for a biome. These control various things, including colouring,
 * ambient particles, sounds, and music.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
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

    /**
     * A builder for biome effects.
     */
    @BiomeDsl
    public interface Builder : AbstractBuilder<BiomeEffects> {

        /**
         * Sets the fog colour of the biome effects to the given [color].
         *
         * @param color the color
         * @return this builder
         * @see BiomeEffects.fogColor
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun fogColor(color: Color): Builder

        /**
         * Sets the water colour of the biome effects to the given [color].
         *
         * @param color the color
         * @return this builder
         * @see BiomeEffects.waterColor
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun waterColor(color: Color): Builder

        /**
         * Sets the water fog colour of the biome effects to the given [color].
         *
         * @param color the color
         * @return this builder
         * @see BiomeEffects.waterFogColor
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun waterFogColor(color: Color): Builder

        /**
         * Sets the sky colour of the biome effects to the given [color].
         *
         * @param color the color
         * @return this builder
         * @see BiomeEffects.skyColor
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun skyColor(color: Color): Builder

        /**
         * Sets the grass colour modifier of the biome effects to the given
         * [modifier].
         *
         * @param modifier the grass color modifier
         * @return this builder
         * @see BiomeEffects.grassColorModifier
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun grassColorModifier(modifier: GrassColorModifier): Builder

        /**
         * Sets the foliage colour of the biome effects to the given [color].
         *
         * @param color the color
         * @return this builder
         * @see BiomeEffects.foliageColor
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun foliageColor(color: Color?): Builder

        /**
         * Sets the grass colour of the biome effects to the given [color].
         *
         * @param color the color
         * @return this builder
         * @see BiomeEffects.grassColor
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun grassColor(color: Color?): Builder

        /**
         * Sets the ambient particle settings of the biome effects to the
         * given [settings].
         *
         * @param settings the settings
         * @return this builder
         * @see BiomeEffects.ambientParticleSettings
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun particles(settings: AmbientParticleSettings?): Builder

        /**
         * Applies the given [builder] to an ambient particle settings builder,
         * builds the result, and sets the ambient particle settings to the
         * built result.
         *
         * @param type the type
         * @param builder the builder
         * @return this builder
         * @see BiomeEffects.ambientParticleSettings
         */
        @BiomeDsl
        @Contract("_, _ -> this", mutates = "this")
        public fun particles(type: ParticleType, builder: Consumer<AmbientParticleSettings.Builder>): Builder =
            particles(AmbientParticleSettings.builder(type).apply { builder.accept(this) }.build())

        /**
         * Sets the ambient loop sound of the biome effects to the given
         * [sound].
         *
         * @param sound the sound
         * @return this builder
         * @see BiomeEffects.ambientLoopSound
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun loopSound(sound: SoundEvent?): Builder

        /**
         * Sets the ambient mood settings of the biome effects to the given
         * [settings].
         *
         * @param settings the settings
         * @return this builder
         * @see BiomeEffects.ambientMoodSettings
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun mood(settings: AmbientMoodSettings?): Builder

        /**
         * Applies the given [builder] to an ambient mood settings builder,
         * builds the result, and sets the ambient mood settings to the
         * built result.
         *
         * @param sound the sound
         * @param builder the builder
         * @return this builder
         * @see BiomeEffects.ambientMoodSettings
         */
        @BiomeDsl
        @Contract("_, _ -> this", mutates = "this")
        public fun mood(sound: SoundEvent, builder: Consumer<AmbientMoodSettings.Builder>): Builder =
            mood(AmbientMoodSettings.builder(sound).apply { builder.accept(this) }.build())

        /**
         * Sets the ambient additions settings of the biome effects to the
         * given [settings].
         *
         * @param settings the settings
         * @return this builder
         * @see BiomeEffects.ambientAdditionsSettings
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun additions(settings: AmbientAdditionsSettings?): Builder

        /**
         * Applies the given [builder] to an ambient additions settings
         * builder, builds the result, and sets the ambient additions settings
         * to the built result.
         *
         * @param sound the sound
         * @param builder the builder
         * @return this builder
         * @see BiomeEffects.ambientAdditionsSettings
         */
        @BiomeDsl
        @Contract("_, _ -> this", mutates = "this")
        public fun additions(sound: SoundEvent, builder: Consumer<AmbientAdditionsSettings.Builder>): Builder =
            additions(AmbientAdditionsSettings.builder(sound).apply { builder.accept(this) }.build())

        /**
         * Sets the background music of the biome effects to the given [music].
         *
         * @param music the music
         * @return this builder
         * @see BiomeEffects.ambientParticleSettings
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun backgroundMusic(music: Music?): Builder
    }

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun builder(): Builder
    }

    public companion object {

        /**
         * Creates a new builder for biome effects.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = Krypton.factory<Factory>().builder()
    }
}
