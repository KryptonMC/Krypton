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
import org.kryptonmc.api.effect.particle.BlockParticleData
import org.kryptonmc.api.effect.particle.ColorParticleData
import org.kryptonmc.api.effect.particle.DirectionalParticleData
import org.kryptonmc.api.effect.particle.DirectionalParticleEffectBuilder
import org.kryptonmc.api.effect.particle.DustParticleData
import org.kryptonmc.api.effect.particle.ItemParticleData
import org.kryptonmc.api.effect.particle.NoteParticleData
import org.kryptonmc.api.effect.particle.Particle
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.ParticleEffectBuilder
import org.kryptonmc.api.space.Vector
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParticleEffectTests {

    private val particle = mockk<Particle> {
        every { key } returns Key.key("krypton", "test")
        every { id } returns 0
    }

    @Test
    fun `test particle effect builder calls with true long distance`() {
        val builder = ParticleEffectBuilder(particle, 10, longDistance = true).build()
        assertEquals(10, builder.quantity)
        assertEquals(true, builder.longDistance)
    }

    @Test
    fun `test particle effect builder calls with false long distance`() {
        val builder = ParticleEffectBuilder(particle, 10, longDistance = false).build()
        assertEquals(10, builder.quantity)
        assertEquals(false, builder.longDistance)
    }

    @Test
    fun `test directional particle effect builder calls`() {
        val direction = Vector(3, 3, 3)
        val builder = DirectionalParticleEffectBuilder(particle, 10, direction = direction, velocity = 11F).build()

        assertEquals(10, builder.quantity)
        assertTrue(builder.data is DirectionalParticleData)
        assertEquals(11F, (builder.data as DirectionalParticleData).velocity)
        assertEquals(direction, (builder.data as DirectionalParticleData).direction)
    }

    @Test
    fun `test particle effect values`() {
        val effect = ParticleEffect(particle, 10, Vector.ZERO, false)
        assertEquals(particle, effect.type)
        assertEquals(10, effect.quantity)
        assertEquals(Vector.ZERO, effect.offset)
        assertEquals(false, effect.longDistance)
    }

    @Test
    fun `test bounds`() {
        assertThrows<IllegalArgumentException> { ParticleEffect(particle, -1, Vector.ZERO, false) }
        assertThrows<IllegalArgumentException> { ParticleEffect(particle, 0, Vector.ZERO, false) }
        assertThrows<IllegalArgumentException> { NoteParticleData(100u) }
        assertDoesNotThrow { NoteParticleData(0u) }
        assertDoesNotThrow { NoteParticleData(24u) }
    }

    @Test
    fun `test particle data values`() {
        val itemData = ItemParticleData(10)
        assertEquals(10, itemData.id)

        val blockData = BlockParticleData(10)
        assertEquals(10, blockData.id)

        val colourData = ColorParticleData(1u, 2u, 3u)
        assertEquals(1u, colourData.red)
        assertEquals(2u, colourData.green)
        assertEquals(3u, colourData.blue)

        val dustData = DustParticleData(scale = 111F)
        assertEquals(111F, dustData.scale)

        val noteData = NoteParticleData(12u)
        assertEquals(12u, noteData.note)
    }
}
