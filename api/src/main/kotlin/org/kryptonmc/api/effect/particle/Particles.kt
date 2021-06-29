/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed

/**
 * Interface for a particle. Contains the [Key] and internal ID of the particle type.
 */
sealed interface Particle : Keyed {

    /**
     * The namespaced key of this particle.
     */
    val key: Key

    /**
     * The builder to use to build a [ParticleEffect] of this type.
     */
    val builder: ParticleEffectBuilder

    override fun key() = key
}

/**
 * Represents a particle that has basic options available.
 */
class SimpleParticle internal constructor(override val key: Key) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    override val builder: ParticleEffectBuilder
        get() = ParticleEffectBuilder(this)
}

/**
 * Represents a particle that can have velocity applied in a direction.
 */
class DirectionalParticle internal constructor(override val key: Key) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    override val builder: DirectionalParticleEffectBuilder
        get() = DirectionalParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a block texture for its appearance.
 */
class BlockParticle internal constructor(override val key: Key) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    override val builder: BlockParticleEffectBuilder
        get() = BlockParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses an item texture for its appearance.
 */
class ItemParticle internal constructor(override val key: Key) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    override val builder: ItemParticleEffectBuilder
        get() = ItemParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a color for its appearance.
 */
class ColorParticle internal constructor(override val key: Key) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    override val builder: ColorParticleEffectBuilder
        get() = ColorParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a color and scale for its appearance.
 */
class DustParticle internal constructor(override val key: Key) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    override val builder: DustParticleEffectBuilder
        get() = DustParticleEffectBuilder(this)
}
/**
 * Represents a particle that uses a color and scale for its appearance, and
 * transitions from one color to another.
 */
class DustTransitionParticle internal constructor(override val key: Key) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    override val builder: DustTransitionParticleEffectBuilder
        get() = DustTransitionParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a specific note value for its color appearance.
 */
class NoteParticle internal constructor(override val key: Key) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    override val builder: NoteParticleEffectBuilder
        get() = NoteParticleEffectBuilder(this)
}

/**
 * Represents a particle that vibrates from one location to another in a specified
 * amount of ticks.
 */
class VibrationParticle internal constructor(override val key: Key) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    override val builder: VibrationParticleEffectBuilder
        get() = VibrationParticleEffectBuilder(this)
}
