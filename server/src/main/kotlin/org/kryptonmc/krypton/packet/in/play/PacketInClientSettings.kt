package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.MainHand
import org.kryptonmc.krypton.entity.metadata.toSkinSettings
import org.kryptonmc.krypton.packet.data.ChatMode
import org.kryptonmc.krypton.packet.data.ClientSettings
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readString

/**
 * Sent by the client to inform the server of its local settings.
 */
class PacketInClientSettings : PlayPacket(0x05) {

    /**
     * The client's settings
     */
    lateinit var settings: ClientSettings private set

    override fun read(buf: ByteBuf) {
        val locale = buf.readString(16)
        val viewDistance = buf.readByte()
        val chatMode = buf.readEnum(ChatMode::class)
        val chatColors = buf.readBoolean()
        val skinFlags = buf.readUnsignedByte().toSkinSettings()
        val mainHand = buf.readEnum(MainHand::class)

        settings = ClientSettings(locale, viewDistance, chatMode, chatColors, skinFlags, mainHand)
    }
}
