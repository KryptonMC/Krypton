package me.bristermitten.minekraft.packet.login

abstract class LoginPluginPacket(
    val packetType: PluginPacketType
) : LoginPacket(packetType.id) {

    abstract val messageID: Int

    abstract val data: Array<Byte>
}

enum class PluginPacketType(val id: Int) {

    REQUEST(0x04),
    RESPONSE(0x02)
}