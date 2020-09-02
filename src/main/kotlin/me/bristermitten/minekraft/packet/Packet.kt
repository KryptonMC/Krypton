package me.bristermitten.minekraft.packet

import io.netty.buffer.ByteBuf

interface Packet {

    val info: PacketInfo

    fun read(buf: ByteBuf) {
        throw UnsupportedOperationException("$javaClass does not support reading")
    }

    fun write(buf: ByteBuf) {
        throw UnsupportedOperationException("$javaClass does not support writing")
    }
}
