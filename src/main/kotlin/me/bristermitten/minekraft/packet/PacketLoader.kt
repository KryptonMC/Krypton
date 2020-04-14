package me.bristermitten.minekraft.packet

import me.bristermitten.minekraft.packet.`in`.*

object PacketLoader
{

    fun loadAll()
    {
        //HANDSHAKE STATE
        PacketState.HANDSHAKE.registerPacketType(0, ::PacketInHandshake)

        //STATUS

        PacketState.STATUS.registerPacketType(0, ::PacketInStatusRequest)
        PacketState.STATUS.registerPacketType(1, ::PacketInPing)

        //LOGIN
        PacketState.LOGIN.registerPacketType(0, ::PacketInLoginStart)
        PacketState.LOGIN.registerPacketType(1, ::PacketInEncryptionResponse)
    }
}
