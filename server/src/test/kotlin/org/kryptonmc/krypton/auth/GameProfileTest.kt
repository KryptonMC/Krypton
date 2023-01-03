/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.auth

import kotlinx.collections.immutable.persistentListOf
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertEquals

class GameProfileTest {

    @Test
    fun `verify equals and hash code`() {
        EqualsVerifier.forClass(KryptonGameProfile::class.java).verify()
    }

    @Test
    fun `ensure with properties with empty list returns basic profile with no properties`() {
        assertEquals(KryptonGameProfile.basic(ID, NAME), PROFILE.withProperties(persistentListOf()))
    }

    @Test
    fun `ensure with properties with same list returns equal profile`() {
        assertEquals(PROFILE, PROFILE.withProperties(PROPERTIES))
    }

    @Test
    fun `ensure with properties with different list returns new profile with properties added`() {
        val properties = persistentListOf(KryptonProfileProperty("aaaa", "bbbb", null), KryptonProfileProperty("cccc", "dddd", "ddddccccbbbbaaaa"))
        assertEquals(KryptonGameProfile.full(ID, NAME, properties), PROFILE.withProperties(properties))
    }

    @Test
    fun `ensure with property not already in list returns new profile with property added`() {
        val property = KryptonProfileProperty("aaaa", "bbbb", null)
        assertEquals(KryptonGameProfile.full(ID, NAME, PROPERTIES.add(property)), PROFILE.withProperty(property))
    }

    @Test
    fun `ensure without property index out of bounds throws`() {
        assertThrows<IndexOutOfBoundsException> { PROFILE.withoutProperty(-1) }
    }

    @Test
    fun `ensure without property index in bounds returns new profile with property removed`() {
        assertEquals(KryptonGameProfile.full(ID, NAME, PROPERTIES.removeAt(0)), PROFILE.withoutProperty(0))
    }

    @Test
    fun `ensure without property not in list returns equal profile`() {
        assertEquals(PROFILE, PROFILE.withoutProperty(KryptonProfileProperty("aaaa", "bbbb", null)))
    }

    @Test
    fun `ensure without property in list returns new profile with property removed`() {
        val property = KryptonProfileProperty("aaaa", "bbbb", null)
        assertEquals(KryptonGameProfile.full(ID, NAME, PROPERTIES.remove(property)), PROFILE.withoutProperty(property))
    }

    companion object {

        private val ID = UUID.fromString("12345678-1234-1234-1234-123456789012")
        private const val NAME = "Dave"
        private val PROPERTIES = persistentListOf(
            KryptonProfileProperty("Hello", "World", "aaaabbbbccccdddd"),
            KryptonProfileProperty("World", "Hello", "ddddccccbbbbaaaa"),
        )
        private val PROFILE = KryptonGameProfile.full(ID, NAME, PROPERTIES)
    }
}
