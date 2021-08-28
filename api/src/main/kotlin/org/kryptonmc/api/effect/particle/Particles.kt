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
import net.kyori.adventure.util.Buildable

/**
 * A type of [ParticleEffect].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface ParticleType : Buildable<ParticleEffect, ParticleEffectBuilder>, Keyed {

    /**
     * The namespaced key of this particle.
     */
    val key: Key

    /**
     * Constructs a new builder to build a new [ParticleEffect] of this type.
     */
    fun builder(): ParticleEffectBuilder

    override fun key() = key

    override fun toBuilder() = builder()
}

/**
 * Represents a particle that has basic options available.
 */
class SimpleParticleType(override val key: Key) : ParticleType {

    /**
     * Create a new builder from this particle type.
     */
    override fun builder() = ParticleEffectBuilder(this)
}

/**
 * Represents a particle that can have velocity applied in a direction.
 */
class DirectionalParticleType(override val key: Key) : ParticleType {

    /**
     * Create a new builder from this particle type.
     */
    override fun builder() = DirectionalParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a block texture for its appearance.
 */
class BlockParticleType(override val key: Key) : ParticleType {

    /**
     * Create a new builder from this particle type.
     */
    override fun builder() = BlockParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses an item texture for its appearance.
 */
class ItemParticleType(override val key: Key) : ParticleType {

    /**
     * Create a new builder from this particle type.
     */
    override fun builder() = ItemParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a color for its appearance.
 */
class ColorParticleType(override val key: Key) : ParticleType {

    /**
     * Create a new builder from this particle type.
     */
    override fun builder() = ColorParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a color and scale for its appearance.
 */
class DustParticleType(override val key: Key) : ParticleType {

    /**
     * Create a new builder from this particle type.
     */
    override fun builder() = DustParticleEffectBuilder(this)
}
/**
 * Represents a particle that uses a color and scale for its appearance, and
 * transitions from one color to another.
 */
class DustTransitionParticleType(override val key: Key) : ParticleType {

    /**
     * Create a new builder from this particle type.
     */
    override fun builder() = DustTransitionParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a specific note value for its color appearance.
 */
class NoteParticleType(override val key: Key) : ParticleType {

    /**
     * Create a new builder from this particle type.
     */
    override fun builder() = NoteParticleEffectBuilder(this)
}

/**
 * Represents a particle that vibrates from one location to another in a specified
 * amount of ticks.
 */
class VibrationParticleType(override val key: Key) : ParticleType {

    /**
     * Create a new builder from this particle type.
     */
    override fun builder() = VibrationParticleEffectBuilder(this)
}
