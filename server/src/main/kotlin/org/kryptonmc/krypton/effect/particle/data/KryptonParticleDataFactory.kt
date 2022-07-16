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
package org.kryptonmc.krypton.effect.particle.data

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.particle.data.BlockParticleData
import org.kryptonmc.api.effect.particle.data.ColorParticleData
import org.kryptonmc.api.effect.particle.data.DirectionalParticleData
import org.kryptonmc.api.effect.particle.data.DustParticleData
import org.kryptonmc.api.effect.particle.data.DustTransitionParticleData
import org.kryptonmc.api.effect.particle.data.ItemParticleData
import org.kryptonmc.api.effect.particle.data.NoteParticleData
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.effect.particle.data.VibrationParticleData
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.util.Color
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.spongepowered.math.vector.Vector3d

object KryptonParticleDataFactory : ParticleData.Factory {

    override fun block(block: Block): BlockParticleData {
        if (block !is KryptonBlock) throw IllegalArgumentException("Invalid block instance! You cannot provide custom Block implementations!")
        return KryptonBlockParticleData(block)
    }

    override fun color(color: Color): ColorParticleData = KryptonColorParticleData(color)

    override fun directional(direction: Vector3d?, velocity: Float): DirectionalParticleData = KryptonDirectionalParticleData(direction, velocity)

    override fun dust(color: Color, scale: Float): DustParticleData = KryptonDustParticleData(color, scale)

    override fun item(item: ItemStack): ItemParticleData = KryptonItemParticleData(item.downcast())

    override fun note(note: Byte): NoteParticleData = KryptonNoteParticleData(note)

    override fun transition(from: Color, scale: Float, to: Color): DustTransitionParticleData = KryptonDustTransitionParticleData(from, scale, to)

    override fun vibration(destination: Vector3d, ticks: Int): VibrationParticleData = KryptonVibrationParticleData(destination, ticks)
}
