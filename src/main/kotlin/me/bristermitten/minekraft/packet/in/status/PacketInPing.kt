package me.bristermitten.minekraft.packet.`in`.status

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.state.PacketState
import me.bristermitten.minekraft.packet.state.StatusPacket

class PacketInPing : StatusPacket(0x01) {

    var payload = -1L
        private set

    override fun read(buf: ByteBuf) {
        payload = buf.readLong()
    }
}