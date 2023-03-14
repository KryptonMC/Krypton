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
package org.kryptonmc.krypton.effect.particle.builder

import org.kryptonmc.api.effect.particle.NoteParticleType
import org.kryptonmc.api.effect.particle.builder.NoteParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.krypton.effect.particle.data.KryptonNoteParticleData

class KryptonNoteParticleEffectBuilder(type: NoteParticleType) : AbstractParticleEffectBuilder<ApiNote>(type), ApiNote {

    private var note: Byte = 0

    override fun note(note: Int): ApiNote = apply {
        require(note in 0..24) { "Note must be between 0 and 24!" }
        this.note = note.toByte()
    }

    override fun buildData(): ParticleData = KryptonNoteParticleData(note)
}

private typealias ApiNote = NoteParticleEffectBuilder
