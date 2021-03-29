package org.kryptonmc.krypton.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.DecoderException
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.extension.readAllAvailableBytes
import org.kryptonmc.krypton.extension.readVarInt
import java.util.zip.Inflater

class PacketDecompressor(var threshold: Int) : ByteToMessageDecoder() {

    private val inflater = Inflater()

    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: MutableList<Any>) {
        if (msg.readableBytes() == 0) return

        val dataLength = msg.readVarInt()
        if (dataLength == 0) {
            out.add(msg.readBytes(msg.readableBytes()))
            return
        }
        if (dataLength < threshold) {
            LOGGER.error("Packet badly compressed! Size of $dataLength is below threshold of $threshold!")
        }
        if (dataLength > PROTOCOL_MAX_SIZE) {
            LOGGER.error("Packet badly compressed! Size of $dataLength is larger than protocol maximum of $PROTOCOL_MAX_SIZE!")
        }
        inflater.setInput(msg.readBytes(msg.readableBytes()).array())
        val bytes = ByteArray(dataLength)
        inflater.inflate(bytes)
        out.add(Unpooled.wrappedBuffer(bytes))
        inflater.reset()
    }

    companion object {

        const val NETTY_NAME = "decompressor"
        private const val PROTOCOL_MAX_SIZE = 0x200000
        private val LOGGER = logger<PacketDecompressor>()
    }
}