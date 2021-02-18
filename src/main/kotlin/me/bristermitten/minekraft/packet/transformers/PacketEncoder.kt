package me.bristermitten.minekraft.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import me.bristermitten.minekraft.extension.logger
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.Packet

class PacketEncoder : MessageToByteEncoder<Packet>() {

    override fun encode(ctx: ChannelHandlerContext, msg: Packet, out: ByteBuf) {
        LOGGER.debug("Outgoing packet of type ${msg.javaClass} id ${msg.info.id}")
        out.writeVarInt(msg.info.id)
        msg.write(out)
    }

    companion object {

        private val LOGGER = logger<PacketEncoder>()

        const val NETTY_NAME = "encoder"
    }
}