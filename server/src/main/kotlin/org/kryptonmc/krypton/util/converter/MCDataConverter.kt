/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.util.converter

import ca.spottedleaf.dataconverter.converters.DataConverter
import ca.spottedleaf.dataconverter.converters.datatypes.DataType
import com.google.gson.JsonObject
import org.kryptonmc.krypton.util.converter.types.MCDataType
import org.kryptonmc.krypton.util.converter.types.json.JsonMapType
import org.kryptonmc.krypton.util.converter.types.nbt.NBTMapType
import org.kryptonmc.nbt.CompoundTag
import kotlin.math.min

object MCDataConverter {

    private val BREAKPOINTS = MCVersionRegistry.BREAKPOINTS

    fun CompoundTag.convertData(type: MCDataType, fromVersion: Int, toVersion: Int): CompoundTag {
        val wrapped = NBTMapType(this)
        return (convert(type, wrapped, fromVersion, toVersion) as? NBTMapType)?.map ?: wrapped.map
    }

    fun JsonObject.convertData(type: MCDataType, fromVersion: Int, toVersion: Int, compressed: Boolean): JsonObject {
        val wrapped = JsonMapType(this, compressed)
        return (convert(type, wrapped, fromVersion, toVersion) as? JsonMapType)?.map ?: wrapped.map
    }

    @Suppress("UNCHECKED_CAST")
    fun <T, R> convert(type: DataType<T, R>, data: T, fromVersion: Int, toVersion: Int): R {
        var ret: Any = data as Any
        var currentVersion = DataConverter.encodeVersions(if (fromVersion < 99) 99 else fromVersion, Int.MAX_VALUE)
        val nextVersion = DataConverter.encodeVersions(toVersion, Int.MAX_VALUE)

        for (breakpoint in BREAKPOINTS.longIterator()) {
            if (currentVersion >= breakpoint) continue
            val converted = type.convert(ret as T, currentVersion, min(nextVersion, breakpoint - 1))
            if (converted != null) ret = converted

            currentVersion = min(nextVersion, breakpoint - 1)
            if (currentVersion == nextVersion) break
        }

        if (currentVersion != nextVersion) {
            val converted = type.convert(ret as T, currentVersion, nextVersion)
            if (converted != null) ret = converted
        }
        return ret as R
    }
}
