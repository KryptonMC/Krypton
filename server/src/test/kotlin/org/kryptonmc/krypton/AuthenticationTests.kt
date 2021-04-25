package org.kryptonmc.krypton

import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.auth.ProfileProperty
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthenticationTests {

    @Test
    fun `test game profile property retention`() {
        val uuid = UUID.randomUUID()
        val profile = GameProfile(uuid, "Test", listOf(ProfileProperty("hello", "world", "xxx")))

        assertEquals(uuid, profile.uuid)
        assertEquals("Test", profile.name)
        assertEquals(listOf(ProfileProperty("hello", "world", "xxx")), profile.properties)

        assertEquals("hello", profile.properties[0].name)
        assertEquals("world", profile.properties[0].value)
        assertEquals("xxx", profile.properties[0].signature)
    }
}
