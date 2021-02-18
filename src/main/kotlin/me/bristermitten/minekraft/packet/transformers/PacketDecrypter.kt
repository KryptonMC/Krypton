package me.bristermitten.minekraft.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.MessageToMessageDecoder
import me.bristermitten.minekraft.extension.logger
import me.bristermitten.minekraft.extension.readAllAvailableBytes
import javax.crypto.Cipher

class PacketDecrypter(
        private val cipher: Cipher
) : MessageToMessageDecoder<ByteBuf>() {

    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: MutableList<Any>) {
        val data = msg.readAllAvailableBytes()
        val dataSize = data.size

        //Allocate a ByteBuf for decrypted output
        val decryptedBuffer = ctx.alloc().heapBuffer(cipher.getOutputSize(dataSize))

        //Write decrypted data into the ByteBuf
        val decryptedContent = decrypt(data, decryptedBuffer.array(), decryptedBuffer.arrayOffset())

        decryptedBuffer.writerIndex(decryptedContent)
        out.add(decryptedBuffer)

        LOGGER.debug("Decrypted bytes length: $decryptedContent")
    }

    private fun decrypt(data: ByteArray, writeTo: ByteArray, offset: Int): Int {
        return cipher.update(data, 0, data.size, writeTo, offset)
    }

    companion object {

        private val LOGGER = logger<PacketDecrypter>()

        const val NETTY_NAME = "decrypter"
    }
}