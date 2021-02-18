package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.logger
import me.bristermitten.minekraft.extension.readString
import me.bristermitten.minekraft.extension.readVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketInClientSettings : PlayPacket(0x05) {

    lateinit var locale: String
    var viewDistance: Byte = -1
    var chatMode: Byte = -1
    var chatColors: Boolean = false
    var displayedSkinParts: Byte = 0
    var mainHand: Byte = 0

    override fun read(buf: ByteBuf) {
        locale = buf.readString(16)
        viewDistance = buf.readByte()
        chatMode = buf.readVarInt().toByte()
        chatColors = buf.readBoolean()
        displayedSkinParts = buf.readByte()
        mainHand = buf.readVarInt().toByte()

        LOGGER.debug("locale = ${locale}")
    }

    companion object {

        private val LOGGER = logger<PacketInClientSettings>()
    }
}