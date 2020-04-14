package me.bristermitten.minekraft.packet.codec

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.extension.writeVarInt

class PacketEncoder : MessageToByteEncoder<Packet>() {

    override fun encode(ctx: ChannelHandlerContext, msg: Packet, out: ByteBuf) {
        out.writeVarInt(msg.info.id)
        msg.write(out)
    }
}
