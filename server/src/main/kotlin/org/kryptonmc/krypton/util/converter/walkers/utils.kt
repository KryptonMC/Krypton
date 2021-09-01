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
package org.kryptonmc.krypton.util.converter.walkers

import ca.spottedleaf.dataconverter.types.ListType
import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.types.MCDataType
import org.kryptonmc.krypton.util.converter.types.MCValueType

fun MapType<String>.convert(type: MCDataType, path: String, fromVersion: Long, toVersion: Long) {
    val map = getMap<String>(path) ?: return
    val replace = type.convert(map, fromVersion, toVersion)
    if (replace != null) setMap(path, replace)
}

fun MapType<String>.convertList(type: MCDataType, path: String, fromVersion: Long, toVersion: Long) {
    val list = getList(path, ObjectType.MAP) ?: return
    for (i in 0 until list.size()) {
        val replace = type.convert(list.getMap(i), fromVersion, toVersion)
        if (replace != null) list.setMap(i, replace)
    }
}

fun MapType<String>.convert(type: MCValueType, path: String, fromVersion: Long, toVersion: Long) {
    val value = getGeneric(path) ?: return
    val converted = type.convert(value, fromVersion, toVersion)
    if (converted != null) setGeneric(path, converted)
}

fun ListType.convert(type: MCValueType, fromVersion: Long, toVersion: Long) {
    for (i in 0 until size()) {
        val value = getGeneric(i)
        val converted = type.convert(value, fromVersion, toVersion)
        if (converted != null) setGeneric(i, converted)
    }
}

fun MapType<String>.convertList(type: MCValueType, path: String, fromVersion: Long, toVersion: Long) {
    getListUnchecked(path)?.convert(type, fromVersion, toVersion)
}

fun MapType<String>.convertKeys(type: MCValueType, path: String, fromVersion: Long, toVersion: Long) {
    getMap<String>(path)?.convertKeys(type, fromVersion, toVersion)
}

fun MapType<String>.convertKeys(type: MCValueType, fromVersion: Long, toVersion: Long) {
    keys().toList().forEach {
        val updated = type.convert(it, fromVersion, toVersion) as? String
        if (updated != null) {
            setGeneric(updated, getGeneric(it)!!)
            remove(it)
        }
    }
}

fun MapType<String>.convertValues(type: MCDataType, path: String, fromVersion: Long, toVersion: Long) {
    getMap<String>(path)?.convertValues(type, fromVersion, toVersion)
}

fun MapType<String>.convertValues(type: MCDataType, fromVersion: Long, toVersion: Long) {
    keys().toList().forEach {
        val value = getMap<String>(it) ?: return@forEach
        val replace = type.convert(value, fromVersion, toVersion)
        if (replace != null) setMap(it, replace) // no CME, key is in map already
    }
}
