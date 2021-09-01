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
package org.kryptonmc.krypton.util.converters.helpers

import org.kryptonmc.krypton.util.converter.types.MCValueType

object RenameStringValueTypeHelper {

    fun register(version: Int, type: MCValueType, renamer: (String) -> String?) = register(version, 0, type, renamer)

    fun register(version: Int, subVersion: Int, type: MCValueType, renamer: (String) -> String?) {
        type.addConverter(version, subVersion) { data, _, _ ->
            val ret = if (data is String) renamer(data) else null
            if (ret === data) null else ret
        }
    }
}
