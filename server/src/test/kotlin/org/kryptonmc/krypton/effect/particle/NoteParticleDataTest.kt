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
