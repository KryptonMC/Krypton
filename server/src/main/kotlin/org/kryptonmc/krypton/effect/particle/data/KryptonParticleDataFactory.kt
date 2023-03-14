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
package org.kryptonmc.krypton.effect.particle.data

import org.kryptonmc.api.block.BlockState
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
import org.kryptonmc.api.util.Color
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.world.block.state.downcast

object KryptonParticleDataFactory : ParticleData.Factory {

    override fun block(block: BlockState): BlockParticleData = KryptonBlockParticleData(block.downcast())

    override fun color(color: Color): ColorParticleData = KryptonColorParticleData(color)

    override fun directional(direction: Vec3d?, velocity: Float): DirectionalParticleData = KryptonDirectionalParticleData(direction, velocity)

    override fun dust(color: Color, scale: Float): DustParticleData = KryptonDustParticleData(color, scale)

    override fun item(item: ItemStack): ItemParticleData = KryptonItemParticleData(item.downcast())

    override fun note(note: Byte): NoteParticleData = KryptonNoteParticleData(note)

    override fun transition(from: Color, scale: Float, to: Color): DustTransitionParticleData = KryptonDustTransitionParticleData(from, scale, to)

    override fun vibration(destination: Vec3d, ticks: Int): VibrationParticleData = KryptonVibrationParticleData(destination, ticks)
}
