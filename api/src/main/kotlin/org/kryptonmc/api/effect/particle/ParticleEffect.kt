/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.effect.particle.builder.BlockParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.ColorParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.DirectionalParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.DustParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.DustTransitionParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.ItemParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.NoteParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.ParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.VibrationParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.util.provide
import org.spongepowered.math.vector.Vector3d

/**
 * Holds information used to spawn particles for a player.
 *
 * This effect is entirely immutable, and so is safe for both storage and
 * reuse.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ParticleEffect {

    /**
     * The type of this effect.
     */
    @get:JvmName("type")
    public val type: ParticleType

    /**
     * The amount of this effect that should be spawned. Must be >= 1.
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
     * Type-specific data for this effect. May be null if this effect has no
     * type-specific data.
     */
    @get:JvmName("data")
    public val data: ParticleData?

    @ApiStatus.Internal
    public interface Factory {

        public fun of(
            type: ParticleType,
            quantity: Int,
            offset: Vector3d,
            longDistance: Boolean,
            data: ParticleData?
        ): ParticleEffect
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new particle effect with the given values.
         *
         * @param type the type of the effect
         * @param quantity the amount of the effect to be spawned
         * @param offset the offset of the effect from the centre
         * @param longDistance if the distance is long
         * @param data optional type-specific data
         * @throws IllegalArgumentException if the [quantity] is less than 1
         * @return a new particle effect
         */
        @JvmOverloads
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(
            type: ParticleType,
            quantity: Int,
            offset: Vector3d,
            longDistance: Boolean,
            data: ParticleData? = null
        ): ParticleEffect = FACTORY.of(type, quantity, offset, longDistance, data)

        /**
         * Creates a new particle effect builder for the given particle [type].
         *
         * @param type the type of the particle
         * @return a new particle effect builder for the given type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: ParticleType): ParticleEffectBuilder = ParticleEffectBuilder(type)

        /**
         * Creates a new directional particle effect builder for the given
         * directional particle [type].
         *
         * @param type the type of the directional particle
         * @return a new directional particle effect builder for the given type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: DirectionalParticleType): DirectionalParticleEffectBuilder = DirectionalParticleEffectBuilder(type)

        /**
         * Creates a new block particle effect builder for the given block
         * particle [type].
         *
         * @param type the type of the block particle
         * @return a new block particle effect builder for the given type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: BlockParticleType): BlockParticleEffectBuilder = BlockParticleEffectBuilder(type)

        /**
         * Creates a new item particle effect builder for the given item
         * particle [type].
         *
         * @param type the type of the item particle
         * @return a new item particle effect builder for the given type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: ItemParticleType): ItemParticleEffectBuilder = ItemParticleEffectBuilder(type)

        /**
         * Creates a new color particle effect builder for the given color
         * particle [type].
         *
         * @param type the type of the color particle
         * @return a new color particle effect builder for the given type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: ColorParticleType): ColorParticleEffectBuilder = ColorParticleEffectBuilder(type)

        /**
         * Creates a new dust particle effect builder for the given dust
         * particle [type].
         *
         * @param type the type of the directional particle
         * @return a new dust particle effect builder for the given type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: DustParticleType): DustParticleEffectBuilder = DustParticleEffectBuilder(type)

        /**
         * Creates a new dust transition particle effect builder for the given
         * dust transition particle [type].
         *
         * @param type the type of the dust transition particle
         * @return a new dust transition particle effect builder for the given
         * type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: DustTransitionParticleType): DustTransitionParticleEffectBuilder = DustTransitionParticleEffectBuilder(type)

        /**
         * Creates a new note particle effect builder for the given note
         * particle [type].
         *
         * @param type the type of the note particle
         * @return a new note particle effect builder for the given type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: NoteParticleType): NoteParticleEffectBuilder = NoteParticleEffectBuilder(type)

        /**
         * Creates a new vibration particle effect builder for the given
         * vibration particle [type].
         *
         * @param type the type of the vibration particle
         * @return a new vibration particle effect builder for the given type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: VibrationParticleType): VibrationParticleEffectBuilder = VibrationParticleEffectBuilder(type)
    }
}
