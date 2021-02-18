package me.bristermitten.minekraft.packet.out.status

import io.netty.buffer.ByteBuf
import kotlinx.serialization.json.Json
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.packet.data.StatusResponse
import me.bristermitten.minekraft.packet.state.StatusPacket

class PacketOutStatusResponse(private val response: StatusResponse) : StatusPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeString(JSON.encodeToString(StatusResponse.serializer(), response))
    }

    companion object {

        private val JSON = Json {}
    }
}