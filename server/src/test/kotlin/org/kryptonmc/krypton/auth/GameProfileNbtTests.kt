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
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ImmutableCompoundTag
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GameProfileNbtTests {

    @Test
    fun `test compound tag to profile is null when no name and id`() {
        assertNull(GameProfileUtil.deserialize(CompoundTag.EMPTY))
        val nameNoId = ImmutableCompoundTag.builder().putString("Name", NAME).build()
        assertNull(GameProfileUtil.deserialize(nameNoId))
        val idNoName = ImmutableCompoundTag.builder().putUUID("Id", ID).build()
        assertNull(GameProfileUtil.deserialize(idNoName))
    }

    @Test
    fun `test compound tag to profile without properties`() {
        val input = ImmutableCompoundTag.builder().putUUID("Id", ID).putString("Name", NAME).build()
        val profile = assertNotNull(GameProfileUtil.deserialize(input))
        assertEquals(NAME, profile.name)
        assertEquals(ID, profile.uuid)
        assertTrue(profile.properties.isEmpty())
    }

    @Test
    fun `test compound tag to profile with properties`() {
        val input = ImmutableCompoundTag.builder()
            .putUUID("Id", ID)
            .putString("Name", NAME)
            .putCompound("Properties") { properties ->
                properties.putList("textures") { textures ->
                    textures.add(createTextureCompound())
                }
            }
            .build()
        val profile = assertNotNull(GameProfileUtil.deserialize(input))
        assertEquals(NAME, profile.name)
        assertEquals(ID, profile.uuid)
        assertEquals(1, profile.properties.size)
        val property = profile.properties.get(0)
        assertEquals("textures", property.name)
        assertEquals(TEXTURE_VALUE, property.value)
        assertEquals(TEXTURE_SIGNATURE, property.signature)
    }

    @Test
    fun `test put with null profile returns input compound tag`() {
        assertEquals(CompoundTag.EMPTY, GameProfileUtil.putProfile(CompoundTag.EMPTY, "does_not_matter", null))
        val input = ImmutableCompoundTag.builder().putString("does_not_matter", "does_not_matter").build()
        assertEquals(input, GameProfileUtil.putProfile(input, "does_not_matter", null))
    }

    @Test
    fun `test put with profile no properties`() {
        val profile = KryptonGameProfile.basic(ID, NAME)
        val output = ImmutableCompoundTag.builder()
            .put("profile", ImmutableCompoundTag.builder()
                .putUUID("Id", ID)
                .putString("Name", NAME)
                .build())
            .build()
        assertEquals(output, GameProfileUtil.putProfile(CompoundTag.EMPTY, "profile", profile))
    }

    @Test
    fun `test put with profile with properties`() {
        val profile = KryptonGameProfile.full(ID, NAME, persistentListOf(KryptonProfileProperty("textures", TEXTURE_VALUE, TEXTURE_SIGNATURE)))
        val output = ImmutableCompoundTag.builder()
            .put("profile", ImmutableCompoundTag.builder()
                .putUUID("Id", ID)
                .putString("Name", NAME)
                .putCompound("Properties") { properties ->
                    properties.putList("textures") { textures ->
                        textures.add(createTextureCompound())
                    }
                }
                .build())
            .build()
        assertEquals(output, GameProfileUtil.putProfile(CompoundTag.EMPTY, "profile", profile))
    }

    companion object {

        private val ID = UUID.randomUUID()
        private const val NAME = "BomBardyGamer"
        private const val TEXTURE_VALUE = "aaaabbbbccccddddeeeeffffgggghhhh"
        private const val TEXTURE_SIGNATURE = "9a0417016f345c934a1a88f55ca17c05014eeeba" // above text in SHA-1

        @JvmStatic
        private fun createTextureCompound(): CompoundTag = ImmutableCompoundTag.builder()
            .putString("Value", TEXTURE_VALUE)
            .putString("Signature", TEXTURE_SIGNATURE)
            .build()
    }
}
