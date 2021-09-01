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
import ca.spottedleaf.dataconverter.converters.datatypes.DataWalker
import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.util.Long2ObjectArraySortedMap

open class KeyDataType(name: String) : MCDataType(name) {

    private val walkersById = HashMap<String, Long2ObjectArraySortedMap<MutableList<DataWalker<String>>>>()

    inline fun addConverterForId(
        id: String,
        toVersion: Int,
        versionStep: Int = 0,
        crossinline converter: (data: MapType<String>, fromVersion: Long, toVersion: Long) -> MapType<String>?
    ) = addConverterForId(id, object : DataConverter<MapType<String>, MapType<String>>(toVersion, versionStep) {
        override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? =
            converter(data, sourceVersion, toVersion)
    })

    fun addConverterForId(id: String, converter: DataConverter<MapType<String>, MapType<String>>) {
        addStructureConverter(object : DataConverter<MapType<String>, MapType<String>>(converter.toVersion, converter.versionStep) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                if (id != data.getString("id")) return null
                return converter.convert(data, sourceVersion, toVersion)
            }
        })
    }

    fun addWalker(minVersion: Int, id: String, walker: DataWalker<String>) = addWalker(minVersion, 0, id, walker)

    fun addWalker(minVersion: Int, versionStep: Int, id: String, walker: DataWalker<String>) {
        walkersById.computeIfAbsent(id) { Long2ObjectArraySortedMap() }
            .computeIfAbsent(DataConverter.encodeVersions(minVersion, versionStep)) { ArrayList() }
            .add(walker)
    }

    fun copyWalkers(minVersion: Int, fromId: String, toId: String) = copyWalkers(minVersion, 0, fromId, toId)

    fun copyWalkers(minVersion: Int, versionStep: Int, fromId: String, toId: String) {
        val version = DataConverter.encodeVersions(minVersion, versionStep)
        val walkersForId = walkersById[fromId] ?: return

        val nearest = walkersForId.getFloor(version) ?: return
        nearest.forEach { addWalker(minVersion, versionStep, toId, it) }
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

        // run pre hooks
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

        // run all walkers
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

        val walkersByVersion = walkersById[data.getString("id")]
        if (walkersByVersion != null) {
            val walkersForId = walkersByVersion.getFloor(toVersion)
            if (walkersForId != null) {
                for (i in 0 until walkersForId.size) {
                    val replace = walkersForId[i].walk(temp, fromVersion, toVersion)
                    if (replace != null) {
                        temp = replace
                        ret = temp
                    }
                }
            }
        }

        // run post hooks
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
