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
package org.kryptonmc.krypton.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.readAllAvailableBytes
import javax.crypto.Cipher

/**
 * Encrypts packets using a stream cipher provided by the specified [cipher]. The algorithm for this [cipher] should
 * always be [AES/CFB8/NoPadding][org.kryptonmc.krypton.util.encryption.Encryption.SHARED_SECRET_ALGORITHM]
 */
class PacketEncrypter(private val cipher: Cipher) : MessageToByteEncoder<ByteBuf>() {

    override fun encode(ctx: ChannelHandlerContext, message: ByteBuf, out: ByteBuf) {
        // load in all of the written data
        val dataToWrite = message.readAllAvailableBytes()

        // encrypt the data
        val encryptedBytes = encrypt(dataToWrite)
        // write it back
        out.writeBytes(encryptedBytes)

        LOGGER.debug("Encrypted bytes length ${encryptedBytes.size}")
    }

    private fun encrypt(bytes: ByteArray): ByteArray {
        return cipher.update(bytes)
    }

    companion object {

        const val NETTY_NAME = "encrypter"
        private val LOGGER = logger<PacketEncrypter>()
    }
}
