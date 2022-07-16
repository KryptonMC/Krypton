/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
@file:Suppress("MatchingDeclarationName")
package org.kryptonmc.api.effect.particle

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.builder.BlockParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.ColorParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.DirectionalParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.DustParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.DustTransitionParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.ItemParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.NoteParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.SimpleParticleEffectBuilder
import org.kryptonmc.api.effect.particle.builder.VibrationParticleEffectBuilder

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class ParticleDsl

/**
 * Creates a new simple particle effect with the given [type] and the result
 * of applying the given [builder].
 *
 * @param type the type
 * @param builder the builder to apply
 * @return a new simple particle effect
 */
@ParticleDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun particleEffect(type: SimpleParticleType, builder: SimpleParticleEffectBuilder.() -> Unit = {}): ParticleEffect =
    type.builder().apply(builder).build()

/**
 * Creates a new directional particle effect with the given [type] and the
 * result of applying the given [builder].
 *
 * @param type the type
 * @param builder the builder to apply
 * @return a new directional particle effect
 */
@ParticleDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun particleEffect(type: DirectionalParticleType, builder: DirectionalParticleEffectBuilder.() -> Unit = {}): ParticleEffect =
    type.builder().apply(builder).build()

/**
 * Creates a new block particle effect with the given [type] and the result of
 * applying the given [builder].
 *
 * @param type the type
 * @param builder the builder to apply
 * @return a new block particle effect
 */
@ParticleDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun particleEffect(type: BlockParticleType, builder: BlockParticleEffectBuilder.() -> Unit = {}): ParticleEffect =
    type.builder().apply(builder).build()

/**
 * Creates a new item particle effect with the given [type] and the result of
 * applying the given [builder].
 *
 * @param type the type
 * @param builder the builder to apply
 * @return a new item particle effect
 */
@ParticleDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun particleEffect(type: ItemParticleType, builder: ItemParticleEffectBuilder.() -> Unit = {}): ParticleEffect =
    type.builder().apply(builder).build()

/**
 * Creates a new colour particle effect with the given [type] and the result
 * of applying the given [builder].
 *
 * @param type the type
 * @param builder the builder to apply
 * @return a new colour particle effect
 */
@ParticleDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun particleEffect(type: ColorParticleType, builder: ColorParticleEffectBuilder.() -> Unit = {}): ParticleEffect =
    type.builder().apply(builder).build()

/**
 * Creates a new dust particle effect with the given [type] and the result of
 * applying the given [builder].
 *
 * @param type the type
 * @param builder the builder to apply
 * @return a new dust particle effect
 */
@ParticleDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun particleEffect(type: DustParticleType, builder: DustParticleEffectBuilder.() -> Unit = {}): ParticleEffect =
    type.builder().apply(builder).build()

/**
 * Creates a new dust transition particle effect with the given [type] and the
 * result of applying the given [builder].
 *
 * @param type the type
 * @param builder the builder to apply
 * @return a new dust transition particle effect
 */
@ParticleDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun particleEffect(type: DustTransitionParticleType, builder: DustTransitionParticleEffectBuilder.() -> Unit = {}): ParticleEffect =
    type.builder().apply(builder).build()

/**
 * Creates a new note particle effect with the given [type] and the result of
 * applying the given [builder].
 *
 * @param type the type
 * @param builder the builder to apply
 * @return a new note particle effect
 */
@ParticleDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun particleEffect(type: NoteParticleType, builder: NoteParticleEffectBuilder.() -> Unit = {}): ParticleEffect =
    type.builder().apply(builder).build()

/**
 * Creates a new vibration particle effect with the given [type] and the
 * result of applying the given [builder].
 *
 * @param type the type
 * @param builder the builder to apply
 * @return a new vibration particle effect
 */
@ParticleDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun particleEffect(type: VibrationParticleType, builder: VibrationParticleEffectBuilder.() -> Unit = {}): ParticleEffect =
    type.builder().apply(builder).build()
