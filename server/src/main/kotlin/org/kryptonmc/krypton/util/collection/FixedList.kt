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
package org.kryptonmc.krypton.util.collection

import java.util.AbstractList

/**
 * A fixed-sized list that essentially acts as a wrapper around an array.
 *
 * This list does not support null elements.
 */
class FixedList<E : Any>(override val size: Int, private val fillElement: E) : AbstractList<E>() {

    @Suppress("UNCHECKED_CAST")
    private val array = Array<Any>(size) { fillElement } as Array<E>

    override fun get(index: Int): E = array[index]

    override fun set(index: Int, element: E): E {
        val old = array[index]
        array[index] = element
        return old
    }
}
