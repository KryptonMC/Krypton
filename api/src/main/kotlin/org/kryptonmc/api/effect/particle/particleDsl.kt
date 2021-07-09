/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

@DslMarker
private annotation class ParticleDsl

/**
 * DSL to create simple [ParticleEffect]s.
 *
 * @param type the type of the simple particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmSynthetic
@ParticleDsl
inline fun particleEffect(
    type: SimpleParticle,
    builder: ParticleEffectBuilder.() -> Unit = {}
) = ParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create directional [ParticleEffect]s.
 *
 * @param type the type of the directional particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmSynthetic
@ParticleDsl
inline fun particleEffect(
    type: DirectionalParticle,
    builder: DirectionalParticleEffectBuilder.() -> Unit = {}
) = DirectionalParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create block [ParticleEffect]s.
 *
 * @param type the type of the block particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmSynthetic
@ParticleDsl
inline fun particleEffect(
    type: BlockParticle,
    builder: BlockParticleEffectBuilder.() -> Unit = {}
) = BlockParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create item [ParticleEffect]s.
 *
 * @param type the type of the item particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmSynthetic
@ParticleDsl
inline fun particleEffect(
    type: ItemParticle,
    builder: ItemParticleEffectBuilder.() -> Unit = {}
) = ItemParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create colored [ParticleEffect]s.
 *
 * @param type the type of the color particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmSynthetic
@ParticleDsl
inline fun particleEffect(
    type: ColorParticle,
    builder: ColorParticleEffectBuilder.() -> Unit = {}
) = ColorParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create dust [ParticleEffect]s.
 *
 * @param type the type of the dust particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmSynthetic
@ParticleDsl
inline fun particleEffect(
    type: DustParticle,
    builder: DustParticleEffectBuilder.() -> Unit = {}
) = DustParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create dust transition [ParticleEffect]s.
 *
 * @param type the type of the dust transition particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmSynthetic
@ParticleDsl
inline fun particleEffect(
    type: DustTransitionParticle,
    builder: DustTransitionParticleEffectBuilder.() -> Unit = {}
) = DustTransitionParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create note [ParticleEffect]s.
 *
 * @param type the type of the note particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmSynthetic
@ParticleDsl
inline fun particleEffect(
    type: NoteParticle,
    builder: NoteParticleEffectBuilder.() -> Unit = {}
) = NoteParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create vibration [ParticleEffect]s.
 *
 * @param type the type of the vibration particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmSynthetic
@ParticleDsl
inline fun particleEffect(
    type: VibrationParticle,
    builder: VibrationParticleEffectBuilder.() -> Unit = {}
) = VibrationParticleEffectBuilder(type).apply(builder).build()
