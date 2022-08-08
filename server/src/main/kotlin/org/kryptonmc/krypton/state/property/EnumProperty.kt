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
package org.kryptonmc.krypton.state.property

import kotlinx.collections.immutable.ImmutableSet

class EnumProperty<E : Enum<E>>(name: String, type: Class<E>, override val values: ImmutableSet<E>) : KryptonProperty<E>(name, type) {

    private val names = HashMap<String, E>()
    private var idLookupTable = IntArray(type.enumConstants.size) { -1 }

    init {
        var id = 0
        values.forEach {
            val constantName = it.name.lowercase()
            require(!names.containsKey(constantName)) { "Multiple values have the same name $constantName!" }
            names[constantName] = it
            idLookupTable[it.ordinal] = id++
        }
    }

    override fun idFor(value: E): Int = idLookupTable[value.ordinal]

    override fun fromString(value: String): E? = names[value]

    override fun toString(value: E): String = value.name.lowercase()

    override fun generateHashCode(): Int {
        var value = super.generateHashCode()
        value = 31 * value + values.hashCode()
        return 31 * value + names.hashCode()
    }
}
