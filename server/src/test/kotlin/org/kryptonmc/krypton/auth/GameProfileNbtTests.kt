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
        assertNull(CompoundTag.EMPTY.toGameProfile())
        val nameNoId = ImmutableCompoundTag.builder().putString("Name", NAME).build()
        assertNull(nameNoId.toGameProfile())
        val idNoName = ImmutableCompoundTag.builder().putUUID("Id", ID).build()
        assertNull(idNoName.toGameProfile())
    }

    @Test
    fun `test compound tag to profile without properties`() {
        val input = ImmutableCompoundTag.builder().putString("Name", NAME).putUUID("Id", ID).build()
        val profile = assertNotNull(input.toGameProfile())
        assertEquals(NAME, profile.name)
        assertEquals(ID, profile.uuid)
        assertTrue(profile.properties.isEmpty())
    }

    @Test
    fun `test compound tag to profile with properties`() {
        val input = ImmutableCompoundTag.builder()
            .putString("Name", NAME)
            .putUUID("Id", ID)
            .putCompound("Properties") { properties ->
                properties.putList("textures") { textures ->
                    textures.add(createTextureCompound())
                }
            }
            .build()
        val profile = assertNotNull(input.toGameProfile())
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
        assertEquals(CompoundTag.EMPTY, CompoundTag.EMPTY.putGameProfile("does_not_matter", null))
        val input = ImmutableCompoundTag.builder().putString("does_not_matter", "does_not_matter").build()
        assertEquals(input, input.putGameProfile("does_not_matter", null))
    }

    @Test
    fun `test put with profile no properties`() {
        val profile = KryptonGameProfile(NAME, ID, persistentListOf())
        val output = ImmutableCompoundTag.builder()
            .put("profile", ImmutableCompoundTag.builder()
                .putString("Name", NAME)
                .putUUID("Id", ID)
                .build())
            .build()
        assertEquals(output, CompoundTag.EMPTY.putGameProfile("profile", profile))
    }

    @Test
    fun `test put with profile with properties`() {
        val profile = KryptonGameProfile(NAME, ID, persistentListOf(KryptonProfileProperty("textures", TEXTURE_VALUE, TEXTURE_SIGNATURE)))
        val output = ImmutableCompoundTag.builder()
            .put("profile", ImmutableCompoundTag.builder()
                .putString("Name", NAME)
                .putUUID("Id", ID)
                .putCompound("Properties") { properties ->
                    properties.putList("textures") { textures ->
                        textures.add(createTextureCompound())
                    }
                }
                .build())
            .build()
        assertEquals(output, CompoundTag.EMPTY.putGameProfile("profile", profile))
    }

    companion object {

        private const val NAME = "BomBardyGamer"
        private val ID = UUID.randomUUID()
        private const val TEXTURE_VALUE = "aaaabbbbccccddddeeeeffffgggghhhh"
        private const val TEXTURE_SIGNATURE = "9a0417016f345c934a1a88f55ca17c05014eeeba" // above text in SHA-1

        @JvmStatic
        private fun createTextureCompound(): CompoundTag = ImmutableCompoundTag.builder()
            .putString("Value", TEXTURE_VALUE)
            .putString("Signature", TEXTURE_SIGNATURE)
            .build()
    }
}
