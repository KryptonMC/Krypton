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

        @JvmStatic
        fun builder(type: Particle) = ParticleEffectBuilder(type)
    }
}

/**
 * Interface used to denote that a class can be used as a [ParticleEffect]'s data.
 */
interface ParticleData

/**
 * Holds data for directional [ParticleEffect]s.
 */
data class DirectionalParticleData(
    val direction: Vector?, // If null, direction will be random
    val velocity: Float
) : ParticleData

/**
 * Holds data for item [ParticleEffect]s.
 */
data class ItemParticleData(val id: Int) : ParticleData // TODO: Item

/**
 * Holds data for block [ParticleEffect]s.
 */
data class BlockParticleData(val id: Int) : ParticleData // TODO: Block

/**
 * Holds data for colored [ParticleEffect]s.
 */
data class ColorParticleData(val red: UByte, val green: UByte, val blue: UByte) : ParticleData

/**
 * Holds data for dust [ParticleEffect]s.
 */
data class DustParticleData @JvmOverloads constructor(
    val color: ColorParticleData = ColorParticleData(0u, 0u, 0u),
    val scale: Float = 1.0F
) : ParticleData

/**
 * Holds data for note [ParticleEffect]s.
 */
data class NoteParticleData(val note: UByte) : ParticleData {

    init {
        require(note in 0u..24u) { "Note must be between 0 and 24!" }
    }
}
