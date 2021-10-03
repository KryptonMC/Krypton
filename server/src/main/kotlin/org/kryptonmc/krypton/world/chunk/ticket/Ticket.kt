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
package org.kryptonmc.krypton.world.chunk.ticket

@JvmRecord
data class Ticket<T>(
    val type: TicketType<T>,
    val level: Int,
    val key: T
) : Comparable<Ticket<*>> {

    @Suppress("UNCHECKED_CAST")
    override fun compareTo(other: Ticket<*>): Int {
        val i = level.compareTo(other.level)
        if (i != 0) return i
        val j = System.identityHashCode(type).compareTo(System.identityHashCode(other.type))
        return if (j != 0) j else type.comparator.compare(key, other.key as T)
    }
}
