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
