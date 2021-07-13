/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

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
