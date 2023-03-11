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
package org.kryptonmc.krypton.network.forwarding

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.krypton.auth.KryptonProfileProperty
import org.kryptonmc.krypton.util.uuid.MojangUUIDTypeAdapter
import java.util.UUID

@JvmRecord
data class LegacyForwardedData(
    val originalAddress: String,
    override val forwardedAddress: String,
    override val uuid: UUID,
    override val properties: PersistentList<ProfileProperty>
) : ProxyForwardedData {

    override val forwardedPort: Int
        get() = -1

    companion object {

        @JvmStatic
        fun parse(string: String): LegacyForwardedData? {
            val split = string.split('\u0000')
            // We need to have the original IP, forwarded IP, and the UUID at bare minimum.
            if (split.size < 3) return null
            val properties = if (split.size > 3) KryptonProfileProperty.Adapter.readJsonList(split.get(3)) else persistentListOf()
            return LegacyForwardedData(split.get(0), split.get(1), MojangUUIDTypeAdapter.fromString(split.get(2)), properties)
        }
    }
}
