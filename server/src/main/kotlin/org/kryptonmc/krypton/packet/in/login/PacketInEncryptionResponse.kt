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
package org.kryptonmc.krypton.packet.`in`.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.LoginPacket
import org.kryptonmc.krypton.util.readVarIntByteArray

/**
 * Sent by the client to inform the server of the shared secret to be used for encryption.
 *
 * Also returns the verify token sent by the server, as verification that we are actually talking to
 * the same client we were talking to before.
 */
class PacketInEncryptionResponse : LoginPacket(0x01) {

    /**
     * The shared secret used for encryption, encrypted with the server's public key
     */
    lateinit var secret: ByteArray private set

    /**
     * The verify token earlier sent by the server, also encrypted with the server's public key
     */
    lateinit var verifyToken: ByteArray private set

    override fun read(buf: ByteBuf) {
        secret = buf.readVarIntByteArray()
        verifyToken = buf.readVarIntByteArray()
    }
}
