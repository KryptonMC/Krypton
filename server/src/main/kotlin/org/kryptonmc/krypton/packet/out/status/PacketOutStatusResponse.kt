package org.kryptonmc.krypton.packet.out.status

import io.netty.buffer.ByteBuf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.packet.data.StatusResponse
import org.kryptonmc.krypton.packet.state.StatusPacket

/**
 * Response to the client's earlier [status request][org.kryptonmc.krypton.packet.in.status.PacketInStatusRequest] packet.
 *
 * @author Callum Seabrook
 */
class PacketOutStatusResponse(private val response: StatusResponse) : StatusPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeString(Json.encodeToString(response))
    }
}