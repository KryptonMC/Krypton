package org.kryptonmc.krypton.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.extension.writeVarInt
import java.util.zip.Deflater

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
        val uncompressedBytes = msg.readBytes(uncompressedSize).array()
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