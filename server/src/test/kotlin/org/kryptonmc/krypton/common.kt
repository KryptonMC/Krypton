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
package org.kryptonmc.krypton

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import java.net.InetSocketAddress
import java.util.UUID

val sender = mockk<Sender> {
    every { name } returns "Dave"
    every { permissions } returns emptyMap()
}

val player = mockk<Player> {
    every { name } returns "Dorothy"
    every { permissions } returns emptyMap()
    every { uuid } returns UUID.randomUUID()
    every { address } returns InetSocketAddress.createUnresolved("0.0.0.0", 25565)
    every { sendMessage(any()) } just runs
}
