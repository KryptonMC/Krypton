/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import org.kryptonmc.api.effect.particle.builder.BaseParticleEffectBuilder
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A particle type that scopes the return of the builder.
 */
@ImmutableType
public sealed interface ScopedParticleType<B : BaseParticleEffectBuilder<B>> : ParticleType {

    override fun builder(): B
}
