package org.kryptonmc.krypton.api.effect.particle

import org.kryptonmc.krypton.api.space.Vector

/**
 * @author Esophose
 */
data class ParticleEffect internal constructor(
    val type: Particle,
    val quantity: Int,
    val offset: Vector,
    val longDistance: Boolean,
    val data: ParticleData? = null
) {
    init {
        if (quantity < 0) throw IllegalArgumentException("quantity must be at least 1")
        // TODO: Do we want this check? It's here to prevent the server from freezing from a developer creating a few million particle packets
        if (quantity > 16384) throw IllegalArgumentException("quantity exceeds client maximum")
    }
}

interface ParticleData

data class DirectionalParticleData(
    val direction: Vector?, // If null, direction will be random
    val velocity: Float
) : ParticleData

data class ItemParticleData(val id: Int) : ParticleData // TODO: Item
data class BlockParticleData(val id: Int) : ParticleData // TODO: Block

data class ColorParticleData constructor(
    val r: UByte,
    val g: UByte,
    val b: UByte
) : ParticleData {
    init {
        if (0u > r || r > 255u) throw IllegalArgumentException("r must be between 0 and 255 inclusively")
        if (0u > g || g > 255u) throw IllegalArgumentException("g must be between 0 and 255 inclusively")
        if (0u > b || b > 255u) throw IllegalArgumentException("b must be between 0 and 255 inclusively")
    }
}

data class DustParticleData(
    val color: ColorParticleData = ColorParticleData(0u, 0u, 0u),
    val scale: Float = 1.0F
) : ParticleData

data class NoteParticleData(val note: UByte) : ParticleData {
    init {
        if (0u > note || note > 24u) throw IllegalArgumentException("note must be between 0 and 24 inclusively")
    }
}
