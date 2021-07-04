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
package org.kryptonmc.krypton.network

import io.netty.channel.Channel
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.network.handlers.HandshakeHandler
import org.kryptonmc.krypton.network.handlers.PacketHandler
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.packet.out.play.PacketOutDisconnect

/**
 * Represents a session, which is a connection between the server and a client.
 *
 * @param server the server this session is connected to
 * @param channel the backing Netty channel that does all the IO stuff
 */
class Session(
    server: KryptonServer,
    internal val channel: Channel
) {

    var lastKeepAliveId = 0L
    var latency = 0

    @Volatile
    internal var currentState: PacketState = PacketState.HANDSHAKE

    @Volatile
    internal var handler: PacketHandler = HandshakeHandler(server, this)

    fun sendPacket(packet: Packet) {
        channel.writeAndFlush(packet)
    }

    fun disconnect(reason: Component) {
        when (currentState) {
            PacketState.PLAY -> sendPacket(PacketOutDisconnect(reason))
            PacketState.LOGIN -> sendPacket(PacketOutLoginDisconnect(reason))
            else -> Unit
        }
        if (channel.isOpen) channel.close().awaitUninterruptibly()
    }
}
