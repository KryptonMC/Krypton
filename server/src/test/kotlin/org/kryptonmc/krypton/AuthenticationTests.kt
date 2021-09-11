/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton

import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.auth.KryptonProfileProperty
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthenticationTests {

    @Test
    fun `test game profile property retention`() {
        val uuid = UUID.randomUUID()
        val profile = KryptonGameProfile(
            uuid,
            "Test",
            listOf(KryptonProfileProperty("hello", "world", "xxx"))
        )

        assertEquals(uuid, profile.uuid)
        assertEquals("Test", profile.name)
        assertEquals(listOf(KryptonProfileProperty("hello", "world", "xxx")), profile.properties)

        assertEquals("hello", profile.properties[0].name)
        assertEquals("world", profile.properties[0].value)
        assertEquals("xxx", profile.properties[0].signature!!)
    }
}
