/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import org.kryptonmc.api.effect.particle.builder.DirectionalParticleEffectBuilder
import javax.annotation.concurrent.Immutable

/**
 * A type of particle that can have velocity applied in a direction.
 */
@Immutable
public interface DirectionalParticleType : ScopedParticleType<DirectionalParticleEffectBuilder>
