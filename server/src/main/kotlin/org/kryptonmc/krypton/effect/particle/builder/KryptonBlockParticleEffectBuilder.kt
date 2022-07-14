/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.effect.particle.builder

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.effect.particle.BlockParticleType
import org.kryptonmc.api.effect.particle.builder.BlockParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.krypton.effect.particle.data.KryptonBlockParticleData
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.downcast

class KryptonBlockParticleEffectBuilder(type: BlockParticleType) : AbstractParticleEffectBuilder<BlockParticleEffectBuilder>(type),
    BlockParticleEffectBuilder {

    private var block: KryptonBlock = Blocks.STONE.downcast()

    override fun block(block: Block): BlockParticleEffectBuilder = apply {
        if (block !is KryptonBlock) throw IllegalArgumentException("Invalid block instance! You cannot provide custom Block implementations!")
        this.block = block
    }

    override fun buildData(): ParticleData = KryptonBlockParticleData(block)
}
