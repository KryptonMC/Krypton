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

import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.spongepowered.math.vector.Vector3i

object TicketTypes {

    val START = TicketType<Unit>("start") { _, _ -> 0 }
    val LOGIN = TicketType<Long>("login", 100, Long::compareTo)
    val PLAYER = TicketType("player", POSITION_COMPARATOR)
    val FORCED = TicketType("forced", POSITION_COMPARATOR)
    val LIGHT = TicketType("light", POSITION_COMPARATOR)
    val PORTAL = TicketType("portal", 300, Vector3i::compareTo)
    val POST_TELEPORT = TicketType<Int>("post_teleport", 5, Int::compareTo)
    val ASYNC_LOAD = TicketType<Long>("async_load", Long::compareTo)
    val DELAYED_UNLOAD = TicketType<Long>("delayed_unload", 300, Long::compareTo)
    val URGENT = TicketType("urgent", 300, POSITION_COMPARATOR)
    val TEMPORARY = TicketType("temporary", 1, POSITION_COMPARATOR)
}

private val POSITION_COMPARATOR = Comparator<ChunkPosition> { o1, o2 -> o1.toLong().compareTo(o2.toLong()) }
