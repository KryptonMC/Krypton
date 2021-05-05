/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api

import io.mockk.every
import io.mockk.mockk
import org.kryptonmc.krypton.api.effect.particle.BlockParticle
import org.kryptonmc.krypton.api.effect.particle.ColorParticle
import org.kryptonmc.krypton.api.effect.particle.DirectionalParticle
import org.kryptonmc.krypton.api.effect.particle.DustParticle
import org.kryptonmc.krypton.api.effect.particle.ItemParticle
import org.kryptonmc.krypton.api.effect.particle.NoteParticle
import org.kryptonmc.krypton.api.effect.particle.Particle
import org.kryptonmc.krypton.api.effect.particle.ParticleType
import org.kryptonmc.krypton.api.effect.particle.SimpleParticle
import org.kryptonmc.krypton.api.registry.NamespacedKey
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private val KEY = NamespacedKey("krypton", "test")

class ParticleTests {

    @Test
    fun `test particles hold data correctly`() {
        SimpleParticle(KEY, 100).test()
        DirectionalParticle(KEY, 100).test()
        BlockParticle(KEY, 100).test()
        ItemParticle(KEY, 100).test()
        ColorParticle(KEY, 100).test()
        DustParticle(KEY, 100).test()
        NoteParticle(KEY, 100).test()
    }

    @Test
    fun `test particle type list is not empty and iterates`() {
        assertTrue(ParticleType.values.isNotEmpty())
        assertNotNull(ParticleType.iterator())
    }

    @Test
    fun `test all particle type entry ids and nullability`() {
        var id = 0
        ParticleType.values.forEachIndexed { index, it ->
            assertNotNull(it)
            assertEquals(id, index)
            assertEquals(id, it.id)
            id++
        }
    }

    @Test
    fun `test private particle initializer functions`() {
        callMethod("simple")
        callMethod("directional")
        callMethod("block")
        callMethod("item")
        callMethod("dust")
        callMethod("color")
        callMethod("note")

        val particle = mockk<Particle> {
            every { id } returns 79
        }
        val function = ParticleType::class.java.getDeclaredMethod("add", Particle::class.java).apply { isAccessible = true }
        assertNotNull(function.invoke(null, particle))
    }

    @Test
    fun `test particle builder functions`() {
        assertNotNull(SimpleParticle(KEY, 100).builder)
        assertNotNull(DirectionalParticle(KEY, 100).builder)
        assertNotNull(BlockParticle(KEY, 100).builder)
        assertNotNull(ItemParticle(KEY, 100).builder)
        assertNotNull(ColorParticle(KEY, 100).builder)
        assertNotNull(DustParticle(KEY, 100).builder)
        assertNotNull(NoteParticle(KEY, 100).builder)
    }

    private fun Particle.test() {
        assertEquals(KEY, key)
        assertEquals(100, id)
    }

    private fun callMethod(name: String) {
        val function = ParticleType::class.java.getDeclaredMethod(name, String::class.java).apply { isAccessible = true }
        assertNotNull(function.invoke(null, "test"))
    }
}
