package org.kryptonmc.krypton.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.kryptonmc.krypton.util.writeVarInt
import java.util.zip.Deflater

/**
 * Compresses packets that meet or exceed the specified [threshold] in length.
 */
class PacketCompressor(var threshold: Int) : MessageToByteEncoder<ByteBuf>() {

    private val encodeBuffer = ByteArray(8192)
    private val deflater = Deflater()

    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        val uncompressedSize = msg.readableBytes()
        if (uncompressedSize < threshold) {
            out.writeVarInt(0)
            out.writeBytes(msg)
            return
        }
        val uncompressedBytes = ByteArray(uncompressedSize)
        msg.readBytes(uncompressedBytes)
        out.writeVarInt(uncompressedBytes.size)
        deflater.setInput(uncompressedBytes, 0, uncompressedSize)
        deflater.finish()
        while (!deflater.finished()) {
            val someLength = deflater.deflate(encodeBuffer)
            out.writeBytes(encodeBuffer, 0, someLength)
        }
        deflater.reset()
    }

    companion object {

        const val NETTY_NAME = "compressor"
    }
}
