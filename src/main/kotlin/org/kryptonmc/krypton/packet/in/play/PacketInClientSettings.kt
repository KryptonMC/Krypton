package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.MainHand
import org.kryptonmc.krypton.entity.metadata.toSkinFlags
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.extension.readString
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.packet.data.ChatMode
import org.kryptonmc.krypton.packet.data.ClientSettings
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketInClientSettings : PlayPacket(0x05) {

    lateinit var settings: ClientSettings
        private set

    override fun read(buf: ByteBuf) {
        val locale = buf.readString(16)
        val viewDistance = buf.readByte()
        val chatMode = ChatMode.fromId(buf.readVarInt())
        val chatColors = buf.readBoolean()
        val skinFlags = buf.readUnsignedByte().toSkinFlags()
        val mainHand = MainHand.fromId(buf.readVarInt())

        settings = ClientSettings(locale, viewDistance, chatMode, chatColors, skinFlags, mainHand)
    }
}