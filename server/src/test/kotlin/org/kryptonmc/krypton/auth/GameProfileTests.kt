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
import java.util.UUID
import kotlin.test.assertEquals

class GameProfileTests {

    @Test
    fun `verify equals and hash code`() {
        EqualsVerifier.forClass(KryptonGameProfile::class.java).verify()
    }

    @Test
    fun `verify to string consistency`() {
        assertEquals("GameProfile(uuid=$ID, name=$NAME, properties=$PROPERTIES)", PROFILE.toString())
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
