package me.bristermitten.minekraft.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import me.bristermitten.minekraft.extension.writeVarInt


/**
 * Writes a packet size in an appropriate VarInt before the data is written
 */
class SizeEncoder : MessageToByteEncoder<ByteBuf>() {

    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        val packetSize = msg.readableBytes()
        val headerLen = varIntSize(packetSize)
        out.ensureWritable(headerLen + packetSize)
        out.writeVarInt(packetSize)
        out.writeBytes(msg)
    }

    private fun varIntSize(paramInt: Int): Int {
        return when {
            paramInt and VARINT_THRESHOLD_1 == 0 -> 1
            paramInt and VARINT_THRESHOLD_2 == 0 -> 2
            paramInt and VARINT_THRESHOLD_3 == 0 -> 3
            paramInt and VARINT_THRESHOLD_4 == 0 -> 4
            else -> 5
        }
    }
}

const val VARINT_THRESHOLD_1 = -0x80
const val VARINT_THRESHOLD_2 = -16384
const val VARINT_THRESHOLD_3 = -0x200000
const val VARINT_THRESHOLD_4 = -0x10000000
