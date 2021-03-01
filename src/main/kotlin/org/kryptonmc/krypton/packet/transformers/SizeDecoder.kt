package org.kryptonmc.krypton.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.CorruptedFrameException
import org.kryptonmc.krypton.extension.readVarInt

class SizeDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        buf.markReaderIndex()

        val buffer = ByteArray(3)
        for (i in buffer.indices) {
            if (!buf.isReadable) {
                buf.resetReaderIndex()
                return
            }
            buffer[i] = buf.readByte()
            if (buffer[i] < 0) continue

            val length = Unpooled.wrappedBuffer(buffer).readVarInt()
            if (buf.readableBytes() < length) {
                buf.resetReaderIndex()
                return
            }
            out.add(buf.readBytes(length))
            return
        }
        throw CorruptedFrameException("length wider than 21 bits")
    }

    companion object {

        const val NETTY_NAME = "splitter"
    }
}