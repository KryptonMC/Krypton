/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
package org.kryptonmc.api.effect.particle

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.builder.BlockParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.ColorParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.DirectionalParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.DustParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.DustTransitionParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.ItemParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.NoteParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.ParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.VibrationParticleEffectBuilder

@DslMarker
private annotation class ParticleDsl

/**
 * DSL to create simple [ParticleEffect]s.
 *
 * @param type the type of the simple particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@ParticleDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun particleEffect(
    type: SimpleParticleType,
    builder: ParticleEffectBuilder.() -> Unit = {}
): ParticleEffect = ParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create directional [ParticleEffect]s.
 *
 * @param type the type of the directional particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@ParticleDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun particleEffect(
    type: DirectionalParticleType,
    builder: DirectionalParticleEffectBuilder.() -> Unit = {}
): ParticleEffect = DirectionalParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create block [ParticleEffect]s.
 *
 * @param type the type of the block particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@ParticleDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun particleEffect(
    type: BlockParticleType,
    builder: BlockParticleEffectBuilder.() -> Unit = {}
): ParticleEffect = BlockParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create item [ParticleEffect]s.
 *
 * @param type the type of the item particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@ParticleDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun particleEffect(
    type: ItemParticleType,
    builder: ItemParticleEffectBuilder.() -> Unit = {}
): ParticleEffect = ItemParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create colored [ParticleEffect]s.
 *
 * @param type the type of the color particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@ParticleDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun particleEffect(
    type: ColorParticleType,
    builder: ColorParticleEffectBuilder.() -> Unit = {}
): ParticleEffect = ColorParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create dust [ParticleEffect]s.
 *
 * @param type the type of the dust particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@ParticleDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun particleEffect(
    type: DustParticleType,
    builder: DustParticleEffectBuilder.() -> Unit = {}
): ParticleEffect = DustParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create dust transition [ParticleEffect]s.
 *
 * @param type the type of the dust transition particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@ParticleDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun particleEffect(
    type: DustTransitionParticleType,
    builder: DustTransitionParticleEffectBuilder.() -> Unit = {}
): ParticleEffect = DustTransitionParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create note [ParticleEffect]s.
 *
 * @param type the type of the note particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@ParticleDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun particleEffect(
    type: NoteParticleType,
    builder: NoteParticleEffectBuilder.() -> Unit = {}
): ParticleEffect = NoteParticleEffectBuilder(type).apply(builder).build()

/**
 * DSL to create vibration [ParticleEffect]s.
 *
 * @param type the type of the vibration particle
 * @param builder the builder to configure the particle effect with
 * @return a new [ParticleEffect] based on the given settings
 */
@ParticleDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun particleEffect(
    type: VibrationParticleType,
    builder: VibrationParticleEffectBuilder.() -> Unit = {}
): ParticleEffect = VibrationParticleEffectBuilder(type).apply(builder).build()
