package me.bristermitten.minekraft.packet.codec

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.CorruptedFrameException
import me.bristermitten.minekraft.extension.readVarInt

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
            if (buffer[i] >= 0) {
                val length: Int = Unpooled.wrappedBuffer(buffer).readVarInt()
                if (buf.readableBytes() < length) {
                    buf.resetReaderIndex()
                    return
                } else {
                    out.add(buf.readBytes(length))
                    return
                }
            }
        }
        throw CorruptedFrameException("length wider than 21 bits")
    }
}
