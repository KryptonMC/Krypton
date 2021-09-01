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
import ca.spottedleaf.dataconverter.converters.datatypes.DataWalker
import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.util.Long2ObjectArraySortedMap
import org.kryptonmc.krypton.util.converter.MCVersionRegistry

open class MCDataType(val name: String) : DataType<MapType<String>, MapType<String>>() {

    protected val structureConverters = ArrayList<DataConverter<MapType<String>, MapType<String>>>()
    protected val structureWalkers = Long2ObjectArraySortedMap<MutableList<DataWalker<String>>>()
    protected val structureHooks = Long2ObjectArraySortedMap<MutableList<DataHook<MapType<String>, MapType<String>>>>()

    inline fun addStructureConverter(
        toVersion: Int,
        versionStep: Int = 0,
        crossinline converter: (data: MapType<String>, fromVersion: Long, toVersion: Long) -> MapType<String>?
    ) {
        addStructureConverter(object : DataConverter<MapType<String>, MapType<String>>(toVersion, versionStep) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? =
                converter(data, sourceVersion, toVersion)
        })
    }

    fun addStructureConverter(converter: DataConverter<MapType<String>, MapType<String>>) {
        MCVersionRegistry.checkVersion(converter.encodedVersion)
        structureConverters.add(converter)
        structureConverters.sortWith(DataConverter.LOWEST_VERSION_COMPARATOR)
    }

    fun addStructureWalker(minVersion: Int, walker: DataWalker<String>) = addStructureWalker(minVersion, 0, walker)

    fun addStructureWalker(minVersion: Int, versionStep: Int, walker: DataWalker<String>) {
        structureWalkers.computeIfAbsent(DataConverter.encodeVersions(minVersion, versionStep)) { ArrayList() }.add(walker)
    }

    fun addStructureHook(minVersion: Int, hook: DataHook<MapType<String>, MapType<String>>) =
        addStructureHook(minVersion, 0, hook)

    fun addStructureHook(minVersion: Int, versionStep: Int = 0, hook: DataHook<MapType<String>, MapType<String>>) {
        structureHooks.computeIfAbsent(DataConverter.encodeVersions(minVersion, versionStep)) { ArrayList() }.add(hook)
    }

    override fun convert(data: MapType<String>, fromVersion: Long, toVersion: Long): MapType<String>? {
        var temp = data
        var ret: MapType<String>? = null

        for (converter in structureConverters) {
            val converterVersion = converter.encodedVersion
            if (converterVersion <= fromVersion) continue
            if (converterVersion > toVersion) break

            val hooks = structureHooks.getFloor(converterVersion)
            if (hooks != null) {
                for (i in 0 until hooks.size) {
                    val replace = hooks[i].preHook(temp, fromVersion, toVersion)
                    if (replace != null) {
                        temp = replace
                        ret = temp
                    }
                }
            }

            val replace = converter.convert(temp, fromVersion, toVersion)
            if (replace != null) {
                temp = replace
                ret = temp
            }

            if (hooks != null) {
                for (i in hooks.size - 1 downTo 0) {
                    val postReplace = hooks[i].postHook(temp, fromVersion, toVersion)
                    if (postReplace != null) {
                        temp = postReplace
                        ret = temp
                    }
                }
            }
        }

        val hooks = structureHooks.getFloor(toVersion)
        if (hooks != null) {
            for (i in 0 until hooks.size) {
                val replace = hooks[i].preHook(temp, fromVersion, toVersion)
                if (replace != null) {
                    temp = replace
                    ret = temp
                }
            }
        }

        val walkers = structureWalkers.getFloor(toVersion)
        if (walkers != null) {
            for (i in 0 until walkers.size) {
                val replace = walkers[i].walk(temp, fromVersion, toVersion)
                if (replace != null) {
                    temp = replace
                    ret = temp
                }
            }
        }

        if (hooks != null) {
            for (i in hooks.size - 1 downTo 0) {
                val postReplace = hooks[i].postHook(temp, fromVersion, toVersion)
                if (postReplace != null) {
                    temp = postReplace
                    ret = temp
                }
            }
        }

        return ret
    }
}
