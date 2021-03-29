package org.kryptonmc.krypton.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.Packet

class PacketEncoder : MessageToByteEncoder<Packet>() {

    override fun encode(ctx: ChannelHandlerContext, msg: Packet, out: ByteBuf) {
        LOGGER.debug("Outgoing packet of type ${msg.javaClass} id ${msg.info.id}")
        out.writeVarInt(msg.info.id)
        msg.write(out)
    }

    companion object {

        const val NETTY_NAME = "encoder"
        private val LOGGER = logger<PacketEncoder>()
    }
}