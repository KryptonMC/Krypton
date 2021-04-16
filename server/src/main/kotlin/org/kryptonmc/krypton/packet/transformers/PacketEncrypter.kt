package org.kryptonmc.krypton.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.extension.readAllAvailableBytes
import javax.crypto.Cipher

/**
 * Encrypts packets using a stream cipher provided by the specified [cipher]. The algorithm for this [cipher] should
 * always be [AES/CFB8/NoPadding][org.kryptonmc.krypton.encryption.Encryption.SHARED_SECRET_ALGORITHM]
 *
 * @author Alex Wood
 * @author Callum Seabrook
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