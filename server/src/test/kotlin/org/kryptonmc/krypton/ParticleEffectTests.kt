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
package org.kryptonmc.krypton

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.krypton.effect.particle.data.KryptonNoteParticleData
import org.kryptonmc.krypton.util.Bootstrap
import kotlin.test.Test

class ParticleEffectTests {

    @Test
    fun `test bounds`() {
        assertThrows<IllegalArgumentException> { KryptonNoteParticleData(100) }
        assertDoesNotThrow { KryptonNoteParticleData(0) }
        assertDoesNotThrow { KryptonNoteParticleData(24) }
    }

    @Test
    fun `test note disallows values outside of range`() {
        val note = ParticleTypes.NOTE.builder()
        assertThrows<IllegalArgumentException> { note.note(25) }
        assertThrows<IllegalArgumentException> { note.note(-1) }
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `preload bootstrap`() {
            Bootstrap.preload()
        }
    }
}
