package org.kryptonmc.krypton.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.kryptonmc.krypton.channel.ChannelHandler
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.extension.readVarInt

class PacketDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (buf.readableBytes() == 0) return

        val id = buf.readVarInt()
        val session = ctx.pipeline().get(ChannelHandler::class.java).session

        val packet = session.currentState.createPacket(id)
        if (packet == null) {
            LOGGER.debug("Skipping packet with state ${session.currentState} and ID $id because a packet object was not found")
            buf.skipBytes(buf.readableBytes())
            return
        }

        LOGGER.debug("Incoming packet of type ${packet.javaClass}")

        packet.read(buf)

        if (buf.readableBytes() != 0) {
            LOGGER.debug("More bytes from packet $packet (${buf.readableBytes()})")
        }

        out.add(packet)
    }

    companion object {

        const val NETTY_NAME = "decoder"

        private val LOGGER = logger<PacketDecoder>()
    }
}