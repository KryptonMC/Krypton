/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.effect.particle

import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.particle.BlockParticleType
import org.kryptonmc.api.effect.particle.ColorParticleType
import org.kryptonmc.api.effect.particle.DirectionalParticleType
import org.kryptonmc.api.effect.particle.DustParticleType
import org.kryptonmc.api.effect.particle.DustTransitionParticleType
import org.kryptonmc.api.effect.particle.ItemParticleType
import org.kryptonmc.api.effect.particle.NoteParticleType
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.SimpleParticleType
import org.kryptonmc.api.effect.particle.VibrationParticleType

object KryptonParticleTypeFactory : ParticleType.Factory {

    override fun block(key: Key): BlockParticleType = KryptonBlockParticleType(key)

    override fun color(key: Key): ColorParticleType = KryptonColorParticleType(key)

    override fun directional(key: Key): DirectionalParticleType = KryptonDirectionalParticleType(key)

    override fun dust(key: Key): DustParticleType = KryptonDustParticleType(key)

    override fun dustTransition(key: Key): DustTransitionParticleType = KryptonDustTransitionParticleType(key)

    override fun item(key: Key): ItemParticleType = KryptonItemParticleType(key)

    override fun note(key: Key): NoteParticleType = KryptonNoteParticleType(key)

    override fun simple(key: Key): SimpleParticleType = KryptonSimpleParticleType(key)

    override fun vibration(key: Key): VibrationParticleType = KryptonVibrationParticleType(key)
}
