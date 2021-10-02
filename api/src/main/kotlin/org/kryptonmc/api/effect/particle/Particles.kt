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
import org.kryptonmc.api.effect.particle.builder.BlockParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.ColorParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.DirectionalParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.DustParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.DustTransitionParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.ItemParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.NoteParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.ParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.VibrationParticleEffectBuilder
import org.kryptonmc.api.util.CataloguedBy

/**
 * A type of [ParticleEffect].
 */
@CataloguedBy(ParticleTypes::class)
public interface ParticleType : Buildable<ParticleEffect, Buildable.Builder<ParticleEffect>>, Keyed {

    /**
     * Constructs a new builder to build a new [ParticleEffect] of this type.
     */
    public fun builder(): Buildable.Builder<ParticleEffect>

    override fun toBuilder(): Buildable.Builder<ParticleEffect> = builder()
}

/**
 * Represents a particle that has basic options available.
 */
@JvmRecord
public data class SimpleParticleType(private val key: Key) : ParticleType {

    override fun key(): Key = key

    override fun builder(): ParticleEffectBuilder = ParticleEffectBuilder(this)
}

/**
 * Represents a particle that can have velocity applied in a direction.
 */
@JvmRecord
public data class DirectionalParticleType(private val key: Key) : ParticleType {

    override fun key(): Key = key

    override fun builder(): DirectionalParticleEffectBuilder = DirectionalParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a block texture for its appearance.
 */
@JvmRecord
public data class BlockParticleType(private val key: Key) : ParticleType {

    override fun key(): Key = key

    override fun builder(): BlockParticleEffectBuilder = BlockParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses an item texture for its appearance.
 */
@JvmRecord
public data class ItemParticleType(private val key: Key) : ParticleType {

    override fun key(): Key = key

    override fun builder(): ItemParticleEffectBuilder = ItemParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a color for its appearance.
 */
@JvmRecord
public data class ColorParticleType(private val key: Key) : ParticleType {

    override fun key(): Key = key

    override fun builder(): ColorParticleEffectBuilder = ColorParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a color and scale for its appearance.
 */
@JvmRecord
public data class DustParticleType(private val key: Key) : ParticleType {

    override fun key(): Key = key

    override fun builder(): DustParticleEffectBuilder = DustParticleEffectBuilder(this)
}
/**
 * Represents a particle that uses a color and scale for its appearance, and
 * transitions from one color to another.
 */
@JvmRecord
public data class DustTransitionParticleType(private val key: Key) : ParticleType {

    override fun key(): Key = key

    override fun builder(): DustTransitionParticleEffectBuilder = DustTransitionParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a specific note value for its color
 * appearance.
 */
@JvmRecord
public data class NoteParticleType(private val key: Key) : ParticleType {

    override fun key(): Key = key

    override fun builder(): NoteParticleEffectBuilder = NoteParticleEffectBuilder(this)
}

/**
 * Represents a particle that vibrates from one location to another in a
 * specified amount of ticks.
 */
@JvmRecord
public data class VibrationParticleType(private val key: Key) : ParticleType {

    override fun key(): Key = key

    override fun builder(): VibrationParticleEffectBuilder = VibrationParticleEffectBuilder(this)
}
