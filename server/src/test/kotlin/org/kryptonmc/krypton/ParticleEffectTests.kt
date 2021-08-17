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
package org.kryptonmc.krypton

import io.mockk.every
import io.mockk.mockk
import net.kyori.adventure.key.Key
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.api.effect.particle.NoteParticleData
import org.kryptonmc.api.effect.particle.Particle
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.space.Vector
import kotlin.test.Test

class ParticleEffectTests {

    @Test
    fun `test bounds`() {
        val particle = mockk<Particle> {
            every { key } returns Key.key("krypton", "test")
        }
        assertThrows<IllegalArgumentException> { ParticleEffect(particle, -1, Vector.ZERO, false) }
        assertThrows<IllegalArgumentException> { ParticleEffect(particle, 0, Vector.ZERO, false) }
        assertThrows<IllegalArgumentException> { NoteParticleData(100u) }
        assertDoesNotThrow { NoteParticleData(0u) }
        assertDoesNotThrow { NoteParticleData(24u) }
    }
}
