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
package org.kryptonmc.krypton.util

import ca.spottedleaf.dataconverter.minecraft.MCDataConverter
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCDataType
import com.google.gson.JsonObject
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.nbt.CompoundTag

object DataConversion {

    @JvmStatic
    fun upgrade(data: CompoundTag, type: MCDataType, fromVersion: Int, defensiveCopy: Boolean = false): CompoundTag {
        if (fromVersion < KryptonPlatform.worldVersion) {
            val input = if (defensiveCopy) data.copy() else data
            return MCDataConverter.convertTag(type, input, fromVersion, KryptonPlatform.worldVersion)
        }
        return data
    }

    @JvmStatic
    fun upgrade(data: JsonObject, type: MCDataType, fromVersion: Int): JsonObject {
        if (fromVersion < KryptonPlatform.worldVersion) {
            return MCDataConverter.convertJson(type, data, false, fromVersion, KryptonPlatform.worldVersion)
        }
        return data
    }
}
