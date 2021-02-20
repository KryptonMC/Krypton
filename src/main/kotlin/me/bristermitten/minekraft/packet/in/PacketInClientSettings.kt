package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entity.MainHand
import me.bristermitten.minekraft.entity.metadata.toSkinFlags
import me.bristermitten.minekraft.extension.logger
import me.bristermitten.minekraft.extension.readString
import me.bristermitten.minekraft.extension.readVarInt
import me.bristermitten.minekraft.packet.data.ChatMode
import me.bristermitten.minekraft.packet.data.ClientSettings
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketInClientSettings : PlayPacket(0x05) {

    lateinit var settings: ClientSettings

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