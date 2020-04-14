package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.PacketState
import me.bristermitten.minekraft.packet.data.StatusResponse

class PacketOutStatusResponse(private val value: StatusResponse) : Packet
{
    override val info= object: PacketInfo {
        override val id = 0x00
        override val state = PacketState.STATUS
    }


    @UnstableDefault
    private val json = Json(JsonConfiguration.Default.copy(encodeDefaults = false))

    override fun write(buf: ByteBuf)
    {
        val jsonResponse = json.stringify(StatusResponse.serializer(), value)
        buf.writeString(jsonResponse)
    }
}
