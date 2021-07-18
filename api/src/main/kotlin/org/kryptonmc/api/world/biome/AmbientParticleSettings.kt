/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import org.kryptonmc.api.effect.particle.ParticleEffect

/**
 * Settings for the particle effect that may randomly be spawned when in a biome.
 *
 * @param particle the particle that may randomly spawn
 * @param probability the probability that the particle is spawned
 */
class AmbientParticleSettings(
    val particle: ParticleEffect,
    val probability: Float
)
