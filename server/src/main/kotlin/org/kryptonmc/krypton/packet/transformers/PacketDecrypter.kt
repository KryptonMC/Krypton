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
import io.netty.handler.codec.MessageToMessageDecoder
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.readAllAvailableBytes
import javax.crypto.Cipher

/**
 * Decrypts packets using a stream cipher provided by the specified [cipher]. The algorithm for this [cipher] should
 * always be [AES/CFB8/NoPadding][org.kryptonmc.krypton.util.encryption.Encryption.SHARED_SECRET_ALGORITHM]
 */
class PacketDecrypter(private val cipher: Cipher) : MessageToMessageDecoder<ByteBuf>() {

    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: MutableList<Any>) {
        val data = msg.readAllAvailableBytes()

        // allocate a buffer to store decrypted output
        val decryptedBuffer = ctx.alloc().heapBuffer(cipher.getOutputSize(data.size))

        // write decrypted data into the buffer
        val decryptedContent = decrypt(data, decryptedBuffer.array(), decryptedBuffer.arrayOffset())

        decryptedBuffer.writerIndex(decryptedContent)
        out.add(decryptedBuffer)

        LOGGER.debug("Decrypted bytes length: $decryptedContent")
    }

    private fun decrypt(data: ByteArray, writeTo: ByteArray, offset: Int): Int {
        return cipher.update(data, 0, data.size, writeTo, offset)
    }

    companion object {

        const val NETTY_NAME = "decrypter"
        private val LOGGER = logger<PacketDecrypter>()
    }
}
