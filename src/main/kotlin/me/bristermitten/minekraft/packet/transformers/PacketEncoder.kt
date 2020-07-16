package me.bristermitten.minekraft.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.Packet

class PacketEncoder : MessageToByteEncoder<Packet>() {

    override fun encode(ctx: ChannelHandlerContext, msg: Packet, out: ByteBuf) {
        println("Outgoing packet of type ${msg.javaClass} id ${msg.info.id}")
        out.writeVarInt(msg.info.id)
        msg.write(out)
    }

    companion object {
        const val NETTY_NAME = "Encoder"
    }
}
