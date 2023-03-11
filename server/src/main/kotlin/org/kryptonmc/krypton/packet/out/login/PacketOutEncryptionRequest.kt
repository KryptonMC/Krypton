/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.packet.out.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarIntByteArray

/**
 * Sent to instruct the client that we wish to encrypt this connection. The
 * client is provided with our public key, so they can use it to encrypt the
 * shared secret, and a verify token, to attempt to ensure the connection
 * hasn't been tampered with (no hackers listening in).
 */
@JvmRecord
@Suppress("ArrayInDataClass")
data class PacketOutEncryptionRequest(val serverId: String, val publicKey: ByteArray, val verifyToken: ByteArray) : Packet {

    constructor(buf: ByteBuf) : this(buf.readString(MAXIMUM_SERVER_ID_LENGTH), buf.readVarIntByteArray(), buf.readVarIntByteArray())

    override fun write(buf: ByteBuf) {
        buf.writeString(serverId, MAXIMUM_SERVER_ID_LENGTH)
        buf.writeVarIntByteArray(publicKey)
        buf.writeVarIntByteArray(verifyToken)
    }

    companion object {

        private const val MAXIMUM_SERVER_ID_LENGTH = 20

        @JvmStatic
        fun create(publicKey: ByteArray, verifyToken: ByteArray): PacketOutEncryptionRequest = PacketOutEncryptionRequest("", publicKey, verifyToken)
    }
}
