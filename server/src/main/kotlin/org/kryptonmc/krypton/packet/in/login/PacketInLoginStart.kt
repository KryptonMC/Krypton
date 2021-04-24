package org.kryptonmc.krypton.packet.`in`.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.LoginPacket
import org.kryptonmc.krypton.util.readString

/**
 * Sent by the client to inform the server of the client's username. This is either used to construct
 * the player's offline UUID if we are not in online mode, or sent to Mojang to attempt to authenticate
 * the user if we are in online mode.
 */
class PacketInLoginStart : LoginPacket(0x00) {

    /**
     * The username of the player logging in
     */
    lateinit var name: String private set

    override fun read(buf: ByteBuf) {
        name = buf.readString(16)
    }
}
