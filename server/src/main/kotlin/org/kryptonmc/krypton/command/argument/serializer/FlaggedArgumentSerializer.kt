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
package org.kryptonmc.krypton.command.argument.serializer

import com.mojang.brigadier.arguments.ArgumentType

sealed interface FlaggedArgumentSerializer<T : ArgumentType<*>> : ArgumentSerializer<T> {

    /**
     * Packs the minimum and maximum values in to a byte, where the first bit
     * (LSB) indicates the presence of a minimum value, and the second bit
     * (from LSB) represents the presence of a maximum value.
     */
    fun createFlags(minimum: Boolean, maximum: Boolean): Int {
        var flags = 0
        if (minimum) flags = flags or 1
        if (maximum) flags = flags or 2
        return flags
    }
}
