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
package org.kryptonmc.krypton.util.converter.types

import ca.spottedleaf.dataconverter.converters.DataConverter
import ca.spottedleaf.dataconverter.converters.datatypes.DataHook
import ca.spottedleaf.dataconverter.converters.datatypes.DataType
import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.util.Long2ObjectArraySortedMap
import org.kryptonmc.krypton.util.converter.MCVersionRegistry

open class MCValueType(val name: String) : DataType<Any, Any>() {

    private val converters = ArrayList<DataConverter<Any, Any>>()
    private val structureHooks = Long2ObjectArraySortedMap<MutableList<DataHook<Any, Any>>>()

    fun addStructureHook(minVersion: Int, hook: DataHook<Any, Any>) = addStructureHook(minVersion, 0, hook)

    fun addStructureHook(minVersion: Int, versionStep: Int, hook: DataHook<Any, Any>) {
        structureHooks.computeIfAbsent(DataConverter.encodeVersions(minVersion, versionStep)) { ArrayList() }.add(hook)
    }

    inline fun addConverter(
        toVersion: Int,
        versionStep: Int = 0,
        crossinline converter: (data: Any, fromVersion: Long, toVersion: Long) -> Any?
    ) = addConverter(object : DataConverter<Any, Any>(toVersion, versionStep) {
        override fun convert(data: Any, sourceVersion: Long, toVersion: Long): Any? = converter(data, sourceVersion, toVersion)
    })

    fun addConverter(converter: DataConverter<Any, Any>) {
        MCVersionRegistry.checkVersion(converter.encodedVersion)
        converters.add(converter)
        converters.sortWith(DataConverter.LOWEST_VERSION_COMPARATOR)
    }

    override fun convert(data: Any, fromVersion: Long, toVersion: Long): Any? {
        var ret: Any? = null

        for (converter in converters) {
            val converterVersion = converter.encodedVersion
            if (converterVersion <= fromVersion) continue
            if (converterVersion > toVersion) break

            val hooks = structureHooks.getFloor(converterVersion)
            if (hooks != null) {
                for (i in 0 until hooks.size) {
                    val replace = hooks[i].preHook(ret ?: data, fromVersion, toVersion)
                    if (replace != null) ret = replace
                }
            }

            val converted = converter.convert(ret ?: data, fromVersion, toVersion)
            if (converted != null) ret = converted

            if (hooks != null) {
                for (i in 0 until hooks.size) {
                    val replace = hooks[i].postHook(ret ?: data, fromVersion, toVersion)
                    if (replace != null) ret = replace
                }
            }
        }

        return ret
    }
}
