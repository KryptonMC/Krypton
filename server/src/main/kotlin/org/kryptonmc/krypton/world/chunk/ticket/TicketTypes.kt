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

import org.kryptonmc.api.util.Catalogue
import java.util.UUID

@Catalogue(TicketType::class)
object TicketTypes {

    @JvmField
    val START: TicketType<Unit> = TicketType("start") { _, _ -> 0 }

    @JvmField
    val PLAYER: TicketType<UUID> = TicketType("player", UUID::compareTo)

    @JvmField
    val API_LOAD: TicketType<Long> = TicketType("api_load", Long::compareTo)
}
