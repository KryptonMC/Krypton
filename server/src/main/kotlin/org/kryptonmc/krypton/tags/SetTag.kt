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
package org.kryptonmc.krypton.tags

class SetTag<T : Any> private constructor(
    private val valueSet: Set<T>,
    private val closestCommonSuperType: Class<*>
) : Tag<T> {

    override val values = valueSet.toList()

    constructor(set: Set<T>) : this(set, set.findCommonSuperClass())

    override fun contains(value: T) = closestCommonSuperType.isInstance(value) && valueSet.contains(value)

    companion object {

        fun <T : Any> empty() = SetTag<T>(emptySet(), Void::class.java)
    }
}

private fun <T : Any> Set<T>.findCommonSuperClass(): Class<*> {
    if (isEmpty()) return Void::class.java
    var temp: Class<*>? = null
    forEach { temp = if (temp == null) it.javaClass else temp!!.findClosestAncestor(it.javaClass) }
    return temp!!
}

private fun Class<*>.findClosestAncestor(other: Class<*>): Class<*> {
    var temp = this
    while (!temp.isAssignableFrom(other)) temp = temp.superclass
    return temp
}
