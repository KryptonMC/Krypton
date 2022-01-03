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

import org.kryptonmc.api.effect.particle.NoteParticleType
import org.kryptonmc.api.effect.particle.builder.NoteParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.krypton.effect.particle.data.KryptonNoteParticleData
import org.spongepowered.math.vector.Vector3d

class KryptonNoteParticleEffectBuilder(
    type: NoteParticleType,
    quantity: Int = 1,
    offset: Vector3d = Vector3d.ZERO,
    longDistance: Boolean = false,
    private var note: Byte = 0
) : AbstractParticleEffectBuilder<NoteParticleEffectBuilder>(type, quantity, offset, longDistance), NoteParticleEffectBuilder {

    override fun note(note: Int): NoteParticleEffectBuilder = apply {
        require(note in 0..24) { "Note must be between 0 and 24!" }
        this.note = note.toByte()
    }

    override fun buildData(): ParticleData = KryptonNoteParticleData(note)
}
