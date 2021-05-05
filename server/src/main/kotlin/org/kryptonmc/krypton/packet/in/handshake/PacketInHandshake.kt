/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.`in`.handshake

import io.netty.buffer.ByteBuf
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.kryptonmc.krypton.auth.ProfileProperty
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketInfo
import org.kryptonmc.krypton.packet.data.HandshakeData
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt
import java.util.UUID

/**
 * This is the only packet in the [Handshake][PacketState.HANDSHAKE] state.
 *
 * The client uses this packet to inform the server of its intention for the connection (either
 * login or status)
 */
class PacketInHandshake : Packet {

    override val info = PacketInfo(0x00, PacketState.HANDSHAKE)

    /**
     * The data in the handshake
     */
    lateinit var data: HandshakeData private set

    override fun read(buf: ByteBuf) {
        val protocol = buf.readVarInt()
        val address = buf.readString()
        val port = buf.readUnsignedShort().toUShort()
        val nextState = PacketState.fromId(buf.readVarInt())

        data = HandshakeData(protocol, address, port, nextState)
    }
}

data class BungeeCordHandshakeData(
    val originalIp: String,
    val forwardedIp: String,
    val uuid: UUID,
    val properties: List<ProfileProperty>
)

fun String.splitData(): BungeeCordHandshakeData? {
    val split = split('\u0000')
    if (split.size <= 2) return null
//    require(split.size > 2) { "String does not contain at least the proxy, forwarded IP address and UUID" }
    return BungeeCordHandshakeData(
        split[0],
        split[1],
        UUID.fromString(split[2].replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex(), "$1-$2-$3-$4-$5")),
        if (split.size > 3) Json.decodeFromString(split[3]) else emptyList()
    )
}
