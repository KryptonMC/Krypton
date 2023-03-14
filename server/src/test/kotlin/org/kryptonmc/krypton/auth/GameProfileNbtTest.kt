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
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ImmutableCompoundTag
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class GameProfileNbtTest {

    @Test
    fun `ensure empty compound tag deserializes to null`() {
        assertNull(GameProfileUtil.deserialize(CompoundTag.EMPTY))
    }

    @Test
    fun `ensure compound tag with no id deserializes to null`() {
        assertNull(GameProfileUtil.deserialize(ImmutableCompoundTag.builder().putString("Name", NAME).build()))
    }

    @Test
    fun `ensure compound tag with no name deserializes to null`() {
        assertNull(GameProfileUtil.deserialize(ImmutableCompoundTag.builder().putUUID("Id", ID).build()))
    }

    @Test
    fun `ensure compound tag with no properties deserializes to non null`() {
        assertNotNull(GameProfileUtil.deserialize(ImmutableCompoundTag.builder().putUUID("Id", ID).putString("Name", NAME).build()))
    }

    @Test
    fun `ensure compound tag with no properties deserializes correctly`() {
        val input = ImmutableCompoundTag.builder().putUUID("Id", ID).putString("Name", NAME).build()
        assertEquals(KryptonGameProfile.basic(ID, NAME), GameProfileUtil.deserialize(input))
    }

    @Test
    fun `ensure compound tag with properties deserializes to non null`() {
        val input = ImmutableCompoundTag.builder()
            .putUUID("Id", ID)
            .putString("Name", NAME)
            .putCompound("Properties") { properties ->
                properties.putList("textures") { textures ->
                    textures.add(createTextureCompound())
                }
            }
            .build()
        assertNotNull(GameProfileUtil.deserialize(input))
    }

    @Test
    fun `ensure compound tag with properties deserializes correctly`() {
        val input = ImmutableCompoundTag.builder()
            .putUUID("Id", ID)
            .putString("Name", NAME)
            .putCompound("Properties") { properties ->
                properties.putList("textures") { textures ->
                    textures.add(createTextureCompound())
                }
            }
            .build()
        assertEquals(KryptonGameProfile.full(ID, NAME, persistentListOf(TEXTURE_PROPERTY)), GameProfileUtil.deserialize(input))
    }

    @Test
    fun `ensure put with empty compound and null profile always returns empty compound`() {
        assertSame(CompoundTag.EMPTY, GameProfileUtil.putProfile(CompoundTag.EMPTY, "does_not_matter", null))
    }

    @Test
    fun `ensure put with non empty compound and null profile always returns input`() {
        val input = ImmutableCompoundTag.builder().putString("does_not_matter", "does_not_matter").build()
        assertSame(input, GameProfileUtil.putProfile(input, "does_not_matter", null))
    }

    @Test
    fun `ensure put with basic profile outputs correct nbt`() {
        val output = ImmutableCompoundTag.builder()
            .put("profile", ImmutableCompoundTag.builder()
                .putUUID("Id", ID)
                .putString("Name", NAME)
                .build())
            .build()
        assertEquals(output, GameProfileUtil.putProfile(CompoundTag.EMPTY, "profile", KryptonGameProfile.basic(ID, NAME)))
    }

    @Test
    fun `ensure put with full profile outputs correct nbt`() {
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

    @Test
    fun `ensure get with legacy string name deserializes correctly`() {
        val input = ImmutableCompoundTag.builder().putString("profile", NAME).build()
        assertEquals(KryptonGameProfile.partial(NAME), GameProfileUtil.getProfile(input, "profile"))
    }

    companion object {

        private val ID = UUID.randomUUID()
        private const val NAME = "BomBardyGamer"
        private const val TEXTURE_VALUE = "aaaabbbbccccddddeeeeffffgggghhhh"
        private const val TEXTURE_SIGNATURE = "9a0417016f345c934a1a88f55ca17c05014eeeba" // above text in SHA-1
        private val TEXTURE_PROPERTY = KryptonProfileProperty("textures", TEXTURE_VALUE, TEXTURE_SIGNATURE)

        @JvmStatic
        private fun createTextureCompound(): CompoundTag = ImmutableCompoundTag.builder()
            .putString("Value", TEXTURE_VALUE)
            .putString("Signature", TEXTURE_SIGNATURE)
            .build()
    }
}
