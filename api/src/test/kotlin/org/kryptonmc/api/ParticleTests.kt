/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.particle.BlockParticle
import org.kryptonmc.api.effect.particle.ColorParticle
import org.kryptonmc.api.effect.particle.DirectionalParticle
import org.kryptonmc.api.effect.particle.DustParticle
import org.kryptonmc.api.effect.particle.ItemParticle
import org.kryptonmc.api.effect.particle.NoteParticle
import org.kryptonmc.api.effect.particle.Particle
import org.kryptonmc.api.effect.particle.SimpleParticle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val KEY = Key.key("krypton", "test")

class ParticleTests {

    @Test
    fun `test particles hold data correctly`() {
        SimpleParticle(KEY).test()
        DirectionalParticle(KEY).test()
        BlockParticle(KEY).test()
        ItemParticle(KEY).test()
        ColorParticle(KEY).test()
        DustParticle(KEY).test()
        NoteParticle(KEY).test()
    }

    @Test
    fun `test particle builder functions`() {
        assertNotNull(SimpleParticle(KEY).builder)
        assertNotNull(DirectionalParticle(KEY).builder)
        assertNotNull(BlockParticle(KEY).builder)
        assertNotNull(ItemParticle(KEY).builder)
        assertNotNull(ColorParticle(KEY).builder)
        assertNotNull(DustParticle(KEY).builder)
        assertNotNull(NoteParticle(KEY).builder)
    }

    private fun Particle.test() {
        assertEquals(KEY, key)
    }
}
