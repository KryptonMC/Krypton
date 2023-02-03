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
