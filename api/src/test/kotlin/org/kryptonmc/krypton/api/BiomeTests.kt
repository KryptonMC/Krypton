/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api

import org.kryptonmc.krypton.api.world.Biome
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BiomeTests {

    @Test
    fun `test biomes is not empty`() {
        assertTrue(Biome.values().isNotEmpty())
        Biome.values().forEach { assertNotNull(it) }
    }

    @Test
    fun `test id conversions`() {
        assertNotNull(Biome.OCEANS)
        assertTrue(Biome.OCEANS.isNotEmpty())
        assertEquals(Biome.OCEAN, Biome.fromId(0))
        assertEquals(Biome.SWAMP.id, 6)
    }
}
