package org.kryptonmc.krypton.api.effect.particle

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.space.Vector

/**
 * Holds information used to spawn particles for a [Player].
 * These effects can be reused.
 *
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
        require(quantity >= 0) { "Quantity must be at least 1!" }
    }
}

/**
 * Interface used to denote that a class can be used as a [ParticleEffect]'s data.
 *
 * @author Esophose
 */
interface ParticleData

/**
 * Holds data for directional [ParticleEffect]s.
 *
 * @author Esophose
 */
data class DirectionalParticleData(
    val direction: Vector?, // If null, direction will be random
    val velocity: Float
) : ParticleData

/**
 * Holds data for item [ParticleEffect]s.
 *
 * @author Esophose
 */
data class ItemParticleData(val id: Int) : ParticleData // TODO: Item

/**
 * Holds data for block [ParticleEffect]s.
 *
 * @author Esophose
 */
data class BlockParticleData(val id: Int) : ParticleData // TODO: Block

/**
 * Holds data for colored [ParticleEffect]s.
 *
 * @author Esophose
 */
data class ColorParticleData(val red: UByte, val green: UByte, val blue: UByte) : ParticleData

/**
 * Holds data for dust [ParticleEffect]s.
 *
 * @author Esophose
 */
data class DustParticleData @JvmOverloads constructor(
    val color: ColorParticleData = ColorParticleData(0u, 0u, 0u),
    val scale: Float = 1.0F
) : ParticleData

/**
 * Holds data for note [ParticleEffect]s.
 *
 * @author Esophose
 */
data class NoteParticleData(val note: UByte) : ParticleData {

    init {
        require(note in 0u..24u) { "Note must be between 0 and 24!" }
    }
}