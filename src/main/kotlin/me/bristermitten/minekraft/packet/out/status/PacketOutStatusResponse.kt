package me.bristermitten.minekraft.packet.out.status

import io.netty.buffer.ByteBuf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.data.StatusResponse
import me.bristermitten.minekraft.packet.state.PacketState
import me.bristermitten.minekraft.packet.state.StatusPacket

//class PacketOutStatusResponse(private val value: StatusResponse) : Packet
//{
//    override val info = object : PacketInfo {
//        override val id = 0x00
//        override val state = PacketState.STATUS
//    }
//
//    private val json = Json {}
//
//    override fun write(buf: ByteBuf)
//    {
//        val jsonResponse = json.encodeToString(StatusResponse.serializer(), value)
//        buf.writeString(jsonResponse)
//    }
//}

class PacketOutStatusResponse(private val response: StatusResponse) : StatusPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeString(JSON.encodeToString(StatusResponse.serializer(), response))
    }

    companion object {

        private val JSON = Json {}
    }
}
