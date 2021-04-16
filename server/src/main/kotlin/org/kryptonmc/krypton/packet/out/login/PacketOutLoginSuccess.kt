package org.kryptonmc.krypton.packet.out.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeUUID
import org.kryptonmc.krypton.packet.state.LoginPacket
import java.util.*

/**
 * Sent to the client on successful login, to inform them of their own UUID. Not sure why we return their
 * username to them though, as they already know it because they told us it in login start.
 *
 * @author Alex Wood
 */
class PacketOutLoginSuccess(
    private val uuid: UUID,
    private val username: String
) : LoginPacket(0x02) {

    override fun write(buf: ByteBuf) {
        buf.writeUUID(uuid)
        buf.writeString(username)
    }
}