/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.builder

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.ColorParticleData
import org.kryptonmc.api.space.Vector

/**
 * Allows building a [ParticleEffect] for colored particle effects using method
 * chaining.
 */
public class ColorParticleEffectBuilder(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    red: Short = 0,
    green: Short = 0,
    blue: Short = 0
) : AbstractColorParticleEffectBuilder<ColorParticleEffectBuilder>(type, quantity, offset, longDistance, red, green, blue) {

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build(): ParticleEffect = ParticleEffect.of(type, quantity, offset, longDistance, ColorParticleData.of(red, green, blue))
}
