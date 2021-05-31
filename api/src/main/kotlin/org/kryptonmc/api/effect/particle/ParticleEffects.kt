/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import org.kryptonmc.api.entity.entities.Player
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
data class ParticleEffect internal constructor(
    val type: Particle,
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
         * Create a new particle effect builder for the given particle [type].
         *
         * @param type the type of the particle
         * @return a new builder for this particle effect
         */
        @JvmStatic
        fun builder(type: Particle) = ParticleEffectBuilder(type)
    }
}

/**
 * Interface used to denote that a class can be used as a [ParticleEffect]'s data.
 */
sealed interface ParticleData

/**
 * Holds data for directional [ParticleEffect]s.
 *
 * @param direction the direction of the particle, random if null
 * @param velocity the velocity of this directional particle in the direction it's moving
 */
data class DirectionalParticleData(
    val direction: Vector?, // If null, direction will be random
    val velocity: Float
) : ParticleData

/**
 * Holds data for item [ParticleEffect]s.
 */
// TODO: Properly add support for item particles
data class ItemParticleData(val id: Int) : ParticleData

/**
 * Holds data for block [ParticleEffect]s.
 */
// TODO: Properly add support for block particles
data class BlockParticleData(val id: Int) : ParticleData

/**
 * Holds data for colored [ParticleEffect]s.
 *
 * @param red the red component of this RGB color
 * @param green the green component of this RGB color
 * @param blue the blue component of this RGB color
 */
data class ColorParticleData(val red: UByte, val green: UByte, val blue: UByte) : ParticleData

/**
 * Holds data for dust [ParticleEffect]s.
 *
 * @param color the color of the dust particle
 * @param scale the scale
 */
data class DustParticleData @JvmOverloads constructor(
    val color: ColorParticleData = ColorParticleData(0u, 0u, 0u),
    val scale: Float = 1.0F
) : ParticleData

/**
 * Holds data for note [ParticleEffect]s.
 *
 * @param note the note of this particle, must be between 0 and 24 (inclusive)
 * @throws IllegalArgumentException if the [note] is not between 0 and 24 (inclusive)
 */
data class NoteParticleData(val note: UByte) : ParticleData {

    init {
        require(note in 0u..24u) { "Note must be between 0 and 24!" }
    }
}
