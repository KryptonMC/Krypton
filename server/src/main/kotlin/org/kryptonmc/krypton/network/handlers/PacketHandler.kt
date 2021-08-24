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
package org.kryptonmc.krypton.network.handlers

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.network.Session

/**
 * The base interface for packet handlers. This exists primarily to abstract away the [handle]
 * function, so we can call it without actually knowing which handler will handle it. :wesmart:
 */
interface PacketHandler {

    /**
     * The server that this handler is running on.
     */
    val server: KryptonServer

    /**
     * The session that this handler handles packets for.
     */
    val session: Session

    /**
     * Handles the given [packet].
     */
    fun handle(packet: Packet)

    fun onDisconnect() = Unit
}
