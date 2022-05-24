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
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.spongepowered.math.vector.Vector3d

object KryptonParticleDataFactory : ParticleData.Factory {

    override fun block(block: Block): BlockParticleData {
        if (block !is KryptonBlock) throw IllegalArgumentException("Invalid block instance! You cannot provide custom Block implementations!")
        return KryptonBlockParticleData(block)
    }

    override fun color(red: Short, green: Short, blue: Short): ColorParticleData = KryptonColorParticleData(red, green, blue)

    override fun directional(direction: Vector3d?, velocity: Float): DirectionalParticleData = KryptonDirectionalParticleData(direction, velocity)

    override fun dust(red: Short, green: Short, blue: Short, scale: Float): DustParticleData = KryptonDustParticleData(red, green, blue, scale)

    override fun item(item: ItemType): ItemParticleData = KryptonItemParticleData(item)

    override fun note(note: Byte): NoteParticleData = KryptonNoteParticleData(note)

    override fun transition(
        fromRed: Short,
        fromGreen: Short,
        fromBlue: Short,
        scale: Float,
        toRed: Short,
        toGreen: Short,
        toBlue: Short
    ): DustTransitionParticleData = KryptonDustTransitionParticleData(fromRed, fromGreen, fromBlue, scale, toRed, toGreen, toBlue)

    override fun vibration(origin: Vector3d, destination: Vector3d, ticks: Int): VibrationParticleData =
        KryptonVibrationParticleData(origin, destination, ticks)
}
