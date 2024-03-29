/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
