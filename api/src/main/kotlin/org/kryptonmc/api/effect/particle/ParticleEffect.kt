/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.space.Vector

/**
 * Holds information used to spawn particles for a [Player].
 * These effects can be reused.
 *
 * @param type the type of particle
 * @param quantity the amount of particles
 * @param offset the offset vector from the spawn location
 * @param longDistance if true, the distance increases from 256 to 65536
 * @param data optional data for this particle effect
 * @throws IllegalArgumentException if the [quantity] is less than 1
 */
data class ParticleEffect @JvmOverloads constructor(
    val type: ParticleType,
    val quantity: Int,
    val offset: Vector,
    val longDistance: Boolean,
    val data: ParticleData? = null
) {

    init {
        require(quantity > 0) { "Quantity must be at least 1!" }
    }

    companion object {

        /**
         * Creates a new particle effect builder for the given particle [type].
         *
         * @param type the type of the particle
         * @return a new particle effect builder for the given type
         */
        @JvmStatic
        fun builder(type: ParticleType) = ParticleEffectBuilder(type)

        /**
         * Creates a new directional particle effect builder for the given directional
         * particle [type].
         *
         * @param type the type of the directional particle
         * @return a new directional particle effect builder for the given type
         */
        @JvmStatic
        fun builder(type: DirectionalParticleType) = DirectionalParticleEffectBuilder(type)

        /**
         * Creates a new block particle effect builder for the given block particle
         * [type].
         *
         * @param type the type of the block particle
         * @return a new block particle effect builder for the given type
         */
        @JvmStatic
        fun builder(type: BlockParticleType) = DirectionalParticleEffectBuilder(type)

        /**
         * Creates a new item particle effect builder for the given item particle
         * [type].
         *
         * @param type the type of the item particle
         * @return a new item particle effect builder for the given type
         */
        @JvmStatic
        fun builder(type: ItemParticleType) = ItemParticleEffectBuilder(type)

        /**
         * Creates a new color particle effect builder for the given color particle
         * [type].
         *
         * @param type the type of the color particle
         * @return a new color particle effect builder for the given type
         */
        @JvmStatic
        fun builder(type: ColorParticleType) = ColorParticleEffectBuilder(type)

        /**
         * Creates a new dust particle effect builder for the given dust particle
         * [type].
         *
         * @param type the type of the directional particle
         * @return a new dust particle effect builder for the given type
         */
        @JvmStatic
        fun builder(type: DustParticleType) = DustParticleEffectBuilder(type)

        /**
         * Creates a new dust transition particle effect builder for the given dust
         * transition particle [type].
         *
         * @param type the type of the dust transition particle
         * @return a new dust transition particle effect builder for the given type
         */
        @JvmStatic
        fun builder(type: DustTransitionParticleType) = DustTransitionParticleEffectBuilder(type)

        /**
         * Creates a new note particle effect builder for the given note particle
         * [type].
         *
         * @param type the type of the note particle
         * @return a new note particle effect builder for the given type
         */
        @JvmStatic
        fun builder(type: NoteParticleType) = NoteParticleEffectBuilder(type)

        /**
         * Creates a new vibration particle effect builder for the given vibration
         * particle [type].
         *
         * @param type the type of the vibration particle
         * @return a new vibration particle effect builder for the given type
         */
        @JvmStatic
        fun builder(type: VibrationParticleType) = VibrationParticleEffectBuilder(type)
    }
}
