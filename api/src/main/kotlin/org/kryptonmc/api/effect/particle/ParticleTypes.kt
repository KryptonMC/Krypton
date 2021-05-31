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

/**
 * Interface for a particle. Contains the [Key] and internal ID of the particle type.
 */
sealed interface Particle {

    /**
     * The namespaced key of this particle.
     */
    val key: Key

    /**
     * The internal ID. Should not need to be used.
     */
    val id: Int
}

/**
 * Represents a particle that has basic options available.
 */
class SimpleParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    val builder: ParticleEffectBuilder
        get() = ParticleEffectBuilder(this)
}

/**
 * Represents a particle that can have velocity applied in a direction.
 */
class DirectionalParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    val builder: DirectionalParticleEffectBuilder
        get() = DirectionalParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a block texture for its appearance.
 */
class BlockParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    val builder: BlockParticleEffectBuilder
        get() = BlockParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses an item texture for its appearance.
 */
class ItemParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    val builder: ItemParticleEffectBuilder
        get() = ItemParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a color for its appearance.
 */
class ColorParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    val builder: ColorParticleEffectBuilder
        get() = ColorParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a color and scale for its appearance.
 */
class DustParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    val builder: DustParticleEffectBuilder
        get() = DustParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a specific note value for its color appearance.
 */
class NoteParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    /**
     * Create a new builder from this particle type.
     */
    val builder: NoteParticleEffectBuilder
        get() = NoteParticleEffectBuilder(this)
}
