/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
@file:Suppress("MatchingDeclarationName", "MethodOverloading")
package org.kryptonmc.api.effect.particle

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.builder.BaseParticleEffectBuilder

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class ParticleDsl

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
public inline fun <B : BaseParticleEffectBuilder<B>, T : ScopedParticleType<B>> particleEffect(type: T, builder: B.() -> Unit): ParticleEffect =
    type.builder().apply(builder).build()
