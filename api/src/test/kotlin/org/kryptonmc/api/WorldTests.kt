/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import org.kryptonmc.api.world.GameVersion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WorldTests {

    @Test
    fun `test world versioning`() {
        val version = GameVersion(2568, "1.16.5", false)
        assertEquals(2568, version.id)
        assertEquals("1.16.5", version.name)
        assertFalse(version.isSnapshot)

        val snapshot = GameVersion(2568, "snapshot", true)
        assertEquals(2568, snapshot.id)
        assertEquals("snapshot", snapshot.name)
        assertTrue(snapshot.isSnapshot)
    }
}
