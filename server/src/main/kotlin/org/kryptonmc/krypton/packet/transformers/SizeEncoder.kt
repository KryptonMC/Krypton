package org.kryptonmc.krypton.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.kryptonmc.krypton.util.varIntSize
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Writes a packet size in an appropriate VarInt before the data is written
 */
class SizeEncoder : MessageToByteEncoder<ByteBuf>() {

    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        val packetSize = msg.readableBytes()
        out.ensureWritable(packetSize.varIntSize() + packetSize)
        out.writeVarInt(packetSize)
        out.writeBytes(msg)
    }

    companion object {

        const val NETTY_NAME = "prepender"
    }
}
