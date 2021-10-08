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
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.effect.particle.ParticleDsl
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.BlockParticleData
import org.spongepowered.math.vector.Vector3d

/**
 * Allows building a [ParticleEffect] for block particle effects using method
 * chaining.
 */
public class BlockParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector3d = Vector3d.ZERO,
    longDistance: Boolean = false,
    private var block: Block = Blocks.STONE
) : AbstractParticleEffectBuilder<BlockParticleEffectBuilder>(type, quantity, offset, longDistance) {

    /**
     * Sets the block state of the texture to be used.
     *
     * @param block the block
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun block(block: Block): BlockParticleEffectBuilder = apply { this.block = block }

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build(): ParticleEffect = ParticleEffect.of(type, quantity, offset, longDistance, BlockParticleData.of(block))
}
