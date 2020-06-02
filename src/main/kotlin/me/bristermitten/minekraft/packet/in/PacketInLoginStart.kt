package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.logger
import me.bristermitten.minekraft.extension.readString
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.state.PacketState

class PacketInLoginStart : Packet
{
    override val info = Companion

    companion object : PacketInfo
    {
        override val id: Int = 0x00
        override val state = PacketState.LOGIN
    }

    private val logger by logger(javaClass)

    override fun read(buf: ByteBuf)
    {
        val name = buf.readString(16)
        logger.info("$name connecting to Server")
    }
}
