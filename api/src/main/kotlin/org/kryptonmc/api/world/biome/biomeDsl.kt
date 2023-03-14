/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:JvmSynthetic
package org.kryptonmc.api.world.biome

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.internal.annotations.dsl.BiomeDsl

/**
 * Creates a new biome by applying the given [builder] to a new builder, then
 * building the result.
 *
 * @param builder the builder
 * @return a new biome
 */
@BiomeDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun biome(builder: Biome.Builder.() -> Unit): Biome = Biome.builder().apply(builder).build()

/**
 * Applies the given [builder] to a new climate builder and sets the
 * climate to the built instance.
 *
 * @param builder the builder
 * @return this builder
 */
@BiomeDsl
@JvmSynthetic
@Contract("_ -> this", mutates = "this")
public inline fun Biome.Builder.climate(builder: Climate.Builder.() -> Unit): Biome.Builder = climate(Climate.builder().apply(builder).build())

/**
 * Applies the given [builder] to a new effects settings builder and
 * sets the effects settings to the built instance.
 *
 * @param builder the builder
 * @return this builder
 */
@BiomeDsl
@JvmSynthetic
@Contract("_ -> this", mutates = "this")
public inline fun Biome.Builder.effects(builder: BiomeEffects.Builder.() -> Unit): Biome.Builder =
    effects(BiomeEffects.builder().apply(builder).build())

/**
 * Applies the given [builder] to an ambient particle settings builder,
 * builds the result, and sets the ambient particle settings to the
 * built result.
 *
 * @param type the type
 * @param builder the builder
 * @return this builder
 * @see BiomeEffects.ambientParticleSettings
 */
@BiomeDsl
@JvmSynthetic
@Contract("_, _ -> this", mutates = "this")
public inline fun BiomeEffects.Builder.particles(type: ParticleType, builder: AmbientParticleSettings.Builder.() -> Unit): BiomeEffects.Builder =
    particles(AmbientParticleSettings.builder(type).apply(builder).build())

/**
 * Applies the given [builder] to an ambient mood settings builder,
 * builds the result, and sets the ambient mood settings to the
 * built result.
 *
 * @param sound the sound
 * @param builder the builder
 * @return this builder
 * @see BiomeEffects.ambientMoodSettings
 */
@BiomeDsl
@JvmSynthetic
@Contract("_, _ -> this", mutates = "this")
public inline fun BiomeEffects.Builder.mood(sound: SoundEvent, builder: AmbientMoodSettings.Builder.() -> Unit): BiomeEffects.Builder =
    mood(AmbientMoodSettings.builder(sound).apply(builder).build())

/**
 * Applies the given [builder] to an ambient additions settings
 * builder, builds the result, and sets the ambient additions settings
 * to the built result.
 *
 * @param sound the sound
 * @param builder the builder
 * @return this builder
 * @see BiomeEffects.ambientAdditionsSettings
 */
@BiomeDsl
@JvmSynthetic
@Contract("_, _ -> this", mutates = "this")
public inline fun BiomeEffects.Builder.additions(sound: SoundEvent, builder: AmbientAdditionsSettings.Builder.() -> Unit): BiomeEffects.Builder =
    additions(AmbientAdditionsSettings.builder(sound).apply(builder).build())
