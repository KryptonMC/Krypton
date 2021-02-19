package me.bristermitten.minekraft.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import me.bristermitten.minekraft.channel.ChannelHandler
import me.bristermitten.minekraft.extension.logger
import me.bristermitten.minekraft.extension.readVarInt
import me.bristermitten.minekraft.packet.`in`.handshake.PacketInHandshake

class PacketDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (buf.readableBytes() == 0) {
            return
        }

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

        //This needs to be handled early
        if (packet is PacketInHandshake) {
            session.currentState = packet.data.nextState
        }

        out.add(packet)

    }

    companion object {

        private val LOGGER = logger<PacketDecoder>()

        const val NETTY_NAME = "decoder"
    }
}