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
package org.kryptonmc.krypton.effect.particle

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.krypton.effect.particle.data.KryptonNoteParticleData
import org.kryptonmc.krypton.testutil.Bootstrapping

class NoteParticleDataTest {

    @Test
    fun `ensure creating note particle data with note over 24 fails`() {
        assertThrows<IllegalArgumentException> { KryptonNoteParticleData(25) }
    }

    @Test
    fun `ensure creating note particle data with note under 0 fails`() {
        assertThrows<IllegalArgumentException> { KryptonNoteParticleData(-1) }
    }

    @Test
    fun `ensure creating note particle data with note of 0 succeeds`() {
        assertDoesNotThrow { KryptonNoteParticleData(0) }
    }

    @Test
    fun `ensure creating note particle data with note of 24 succeeds`() {
        assertDoesNotThrow { KryptonNoteParticleData(24) }
    }

    @Test
    fun `ensure setting note of particle builder with note over 24 fails`() {
        val note = ParticleTypes.NOTE.get().builder()
        assertThrows<IllegalArgumentException> { note.note(25) }
    }

    @Test
    fun `ensure setting note of particle builder with note under 0 fails`() {
        val note = ParticleTypes.NOTE.get().builder()
        assertThrows<IllegalArgumentException> { note.note(-1) }
    }

    @Test
    fun `ensure setting note of particle builder with note of 0 succeeds`() {
        val note = ParticleTypes.NOTE.get().builder()
        assertDoesNotThrow { note.note(0) }
    }

    @Test
    fun `ensure setting note of particle builder with note of 24 succeeds`() {
        val note = ParticleTypes.NOTE.get().builder()
        assertDoesNotThrow { note.note(24) }
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `preload bootstrap`() {
            Bootstrapping.loadFactories()
            Bootstrapping.loadRegistries()
        }
    }
}
