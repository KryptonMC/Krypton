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
package org.kryptonmc.krypton.packet.`in`.status

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.handlers.StatusPacketHandler
import org.kryptonmc.krypton.packet.InboundPacket

/**
 * Sent by the client to request the server's status information.
 */
object PacketInStatusRequest : InboundPacket<StatusPacketHandler> {

    override fun write(buf: ByteBuf) {
        // there is nothing to write here, and nothing is read for this either
    }

    override fun handle(handler: StatusPacketHandler) {
        handler.handleStatusRequest()
    }
}
