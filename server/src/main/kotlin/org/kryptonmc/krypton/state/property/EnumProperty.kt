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

import com.google.common.collect.ImmutableSet
import java.util.Arrays
import java.util.EnumSet
import java.util.function.Predicate

open class EnumProperty<E : Enum<E>>(name: String, type: Class<E>, values: Collection<E>) : KryptonProperty<E>(name, type) {

    final override val values: Collection<E> = ImmutableSet.copyOf(values)
    private val names = HashMap<String, E>()
    private val idLookupTable: IntArray

    init {
        for (value in values) {
            val constantName = value.name.lowercase()
            require(!names.containsKey(constantName)) { "Multiple values have the same name $constantName!" }
            names.put(constantName, value)
        }
        var id = 0
        idLookupTable = IntArray(type.enumConstants.size)
        Arrays.fill(idLookupTable, -1)
        for (value in values) {
            idLookupTable[value.ordinal] = id++
        }
    }

    override fun fromString(value: String): E? = names.get(value)

    override fun toString(value: E): String = value.name.lowercase()

    override fun idFor(value: E): Int = idLookupTable[value.ordinal]

    override fun generateHashCode(): Int {
        var value = super.generateHashCode()
        value = 31 * value + values.hashCode()
        return 31 * value + names.hashCode()
    }

    companion object {

        @JvmStatic
        inline fun <reified E : Enum<E>> of(name: String): EnumProperty<E> =
            EnumProperty(name, E::class.java, EnumSet.allOf(E::class.java))

        @JvmStatic
        inline fun <reified E : Enum<E>> of(name: String, predicate: Predicate<E>): EnumProperty<E> =
            EnumProperty(name, E::class.java, enumValues<E>().filter(predicate::test))

        @JvmStatic
        inline fun <reified E : Enum<E>> of(name: String, vararg values: E): EnumProperty<E> = of(name, ImmutableSet.copyOf(values))

        @JvmStatic
        inline fun <reified E : Enum<E>> of(name: String, values: Collection<E>): EnumProperty<E> = EnumProperty(name, E::class.java, values)
    }
}
