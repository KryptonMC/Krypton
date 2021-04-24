package org.kryptonmc.krypton.api

import org.kryptonmc.krypton.api.world.WorldVersion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WorldTests {

    @Test
    fun `test world versioning`() {
        val version = WorldVersion(2568, "1.16.5", false)
        assertEquals(2568, version.id)
        assertEquals("1.16.5", version.name)
        assertFalse(version.isSnapshot)

        val snapshot = WorldVersion(2568, "snapshot", true)
        assertEquals(2568, snapshot.id)
        assertEquals("snapshot", snapshot.name)
        assertTrue(snapshot.isSnapshot)
    }
}
