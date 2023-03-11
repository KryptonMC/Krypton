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
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.krypton.util.uuid.MojangUUIDTypeAdapter
import org.skyscreamer.jsonassert.JSONAssert
import java.util.UUID
import kotlin.test.assertEquals

class GameProfileJsonTest {

    @Test
    fun `ensure basic game profile serializes to correct json`() {
        JSONAssert.assertEquals(basicProfileToJsonString(BASIC_PROFILE), KryptonGameProfile.Adapter.toJson(BASIC_PROFILE), true)
    }

    @Test
    fun `ensure full game profile serializes to correct json`() {
        JSONAssert.assertEquals(profileToJsonString(FULL_PROFILE), KryptonGameProfile.Adapter.toJson(FULL_PROFILE), true)
    }

    @Test
    fun `ensure basic game profile json deserializes correctly`() {
        assertEquals(BASIC_PROFILE, KryptonGameProfile.Adapter.fromJson(basicProfileToJsonString(BASIC_PROFILE)))
    }

    @Test
    fun `ensure full game profile json deserializes correctly`() {
        assertEquals(FULL_PROFILE, KryptonGameProfile.Adapter.fromJson(profileToJsonString(FULL_PROFILE)))
    }

    companion object {


        private const val NAME = "Dave"
        private val ID = UUID.fromString("12345678-1234-1234-1234-123456789012")
        private val PROPERTIES = persistentListOf(
            KryptonProfileProperty("Hello", "World", "aaaabbbbccccdddd"),
            KryptonProfileProperty("World", "Hello", "ddddccccbbbbaaaa")
        )

        private val BASIC_PROFILE = KryptonGameProfile.basic(ID, NAME)
        private val FULL_PROFILE = KryptonGameProfile.full(ID, NAME, PROPERTIES)

        @JvmStatic
        private fun propertyToJson(property: ProfileProperty): JSONObject {
            val json = JSONObject()
            json.put("name", property.name)
            json.put("value", property.value)
            if (property.signature != null) json.put("signature", property.signature)
            return json
        }

        @JvmStatic
        private fun profileToJsonString(profile: GameProfile): String {
            val json = JSONObject()
            json.put("id", MojangUUIDTypeAdapter.toString(profile.uuid))
            json.put("name", profile.name)
            if (profile.properties.isNotEmpty()) {
                val properties = JSONArray()
                profile.properties.forEach { properties.put(propertyToJson(it)) }
                json.put("properties", properties)
            }
            return json.toString(4)
        }

        @JvmStatic
        private fun basicProfileToJsonString(profile: GameProfile): String {
            val json = JSONObject()
            json.put("id", MojangUUIDTypeAdapter.toString(profile.uuid))
            json.put("name", profile.name)
            return json.toString(4)
        }
    }
}
