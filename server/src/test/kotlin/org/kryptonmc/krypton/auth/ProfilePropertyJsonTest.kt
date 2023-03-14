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

import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.kryptonmc.api.auth.ProfileProperty
import org.skyscreamer.jsonassert.JSONAssert
import kotlin.test.assertEquals

class ProfilePropertyJsonTest {

    @Test
    fun `ensure signed property serializes to correct json`() {
        JSONAssert.assertEquals(propertyToJsonString(PROPERTY), KryptonProfileProperty.Adapter.toJson(PROPERTY), true)
    }

    @Test
    fun `ensure unsigned property serializes to correct json`() {
        JSONAssert.assertEquals(propertyToJsonString(PROPERTY_UNSIGNED), KryptonProfileProperty.Adapter.toJson(PROPERTY_UNSIGNED), true)
    }

    @Test
    fun `ensure signed property deserializes correctly`() {
        assertEquals(PROPERTY, KryptonProfileProperty.Adapter.fromJson(propertyToJsonString(PROPERTY)))
    }

    @Test
    fun `ensure unsigned property deserializes correctly`() {
        assertEquals(PROPERTY_UNSIGNED, KryptonProfileProperty.Adapter.fromJson(propertyToJsonString(PROPERTY_UNSIGNED)))
    }

    companion object {

        private val PROPERTY = KryptonProfileProperty("Hello", "World", "aaaabbbbccccdddd")
        private val PROPERTY_UNSIGNED = KryptonProfileProperty("Hello", "World", null)

        @JvmStatic
        private fun propertyToJsonString(property: ProfileProperty): String {
            val json = JSONObject()
            json.put("name", property.name)
            json.put("value", property.value)
            if (property.signature != null) json.put("signature", property.signature)
            return json.toString(4)
        }
    }
}
