/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

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
