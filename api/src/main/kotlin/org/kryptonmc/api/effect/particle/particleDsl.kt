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
package org.kryptonmc.api.effect.particle

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.builder.BaseParticleEffectBuilder
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.dsl.ParticleDsl

/**
 * Creates a new particle effect with the given [type] and the result of
 * applying the given [builder].
 *
 * @param B the builder type
 * @param T the particle type
 * @param type the type
 * @param builder the builder to apply
 * @return a new particle effect
 */
@ParticleDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun <B : Base<B>, T : Scoped<B>> particleEffect(type: T, builder: B.() -> Unit): ParticleEffect =
    type.builder().apply(builder).build()

/**
 * Creates a new particle effect with the given [type] and the result of
 * applying the given [builder].
 *
 * @param B the builder type
 * @param T the particle type
 * @param type the type
 * @param builder the builder to apply
 * @return a new particle effect
 */
@ParticleDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun <B : Base<B>, T : Scoped<B>> particleEffect(type: RegistryReference<T>, builder: B.() -> Unit): ParticleEffect =
    type.get().builder().apply(builder).build()

private typealias Base<B> = BaseParticleEffectBuilder<B>
private typealias Scoped<B> = ScopedParticleType<B>
