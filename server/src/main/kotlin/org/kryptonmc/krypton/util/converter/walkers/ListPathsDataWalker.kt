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

import ca.spottedleaf.dataconverter.converters.datatypes.DataType
import ca.spottedleaf.dataconverter.converters.datatypes.DataWalker
import ca.spottedleaf.dataconverter.types.MapType

open class ListPathsDataWalker<T, R>(
    protected val type: DataType<T, R>,
    protected val paths: Array<out String>
) : DataWalker<String> {

    @Suppress("UNCHECKED_CAST", "NULLABLE_TYPE_PARAMETER_AGAINST_NOT_NULL_TYPE_PARAMETER")
    override fun walk(data: MapType<String>, fromVersion: Long, toVersion: Long): MapType<String>? {
        val type = type
        paths.forEach {
            val list = data.getListUnchecked(it) ?: return@forEach
            for (i in 0 until list.size()) {
                val current = list.getGeneric(i)
                val converted = type.convert(current as T, fromVersion, toVersion)
                if (converted != null) list.setGeneric(i, converted)
            }
        }
        return null
    }
}
