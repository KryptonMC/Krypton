package me.bristermitten.minekraft.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import me.bristermitten.minekraft.extension.readAllAvailableBytes
import javax.crypto.Cipher

class PacketEncrypter(
        private val cipher: Cipher
) : MessageToByteEncoder<ByteBuf>()
{


    override fun encode(ctx: ChannelHandlerContext, message: ByteBuf, out: ByteBuf)
    {
        //Load in all of the written data
        val dataToWrite = message.readAllAvailableBytes()

        //Encrypt the data
        val encryptedBytesLength = encrypt(dataToWrite)
        //Write it back
        message.writeBytes(dataToWrite, 0, encryptedBytesLength)
    }

    fun encrypt(bytes: ByteArray): Int
    {
        return cipher.update(bytes, 0, bytes.size, bytes)
    }
}
