package me.bristermitten.minekraft.packet

import me.bristermitten.minekraft.Session
import me.bristermitten.minekraft.encryption.Encryption
import me.bristermitten.minekraft.lang.Color
import me.bristermitten.minekraft.packet.`in`.PacketInLoginStart
import me.bristermitten.minekraft.packet.`in`.PacketInPing
import me.bristermitten.minekraft.packet.`in`.PacketInStatusRequest
import me.bristermitten.minekraft.packet.data.*
import me.bristermitten.minekraft.packet.out.PacketOutEncryptionRequest
import me.bristermitten.minekraft.packet.out.PacketOutPingResponse
import me.bristermitten.minekraft.packet.out.PacketOutStatusResponse
import kotlin.random.Random

class PacketHandler
{

    fun handleStatusPacket(session: Session)
    {
        session.sendPacket(PacketOutStatusResponse(
                StatusResponse(
                        ServerVersion("1.15.2", 578),
                        Players(
                                100, 2,
                                setOf(
                                        PlayerInfo(Chat("Hello", bold = true, color = Color.RED).toChatString()),
                                        PlayerInfo(Chat("World!", bold = true, color = Color.BLUE).toChatString())
                                )
                        ),
                        Chat(
                                "MineKraft is a Minecraft Server written in Kotlin!", bold = true, color = Color.values().random()
                        )
                )
        )
        )
    }

    fun handle(session: Session, packet: Packet)
    {
        if (packet is PacketInPing)
            handlePing(session, packet)

        if (packet is PacketInStatusRequest)
        {
            handleStatusPacket(session)
        }

        if (packet is PacketInLoginStart)
        {
            handleLoginStart(session, packet)
        }
    }

    private fun handleLoginStart(session: Session, packet: PacketInLoginStart)
    {
        session.sendPacket(
                PacketOutEncryptionRequest(
                        "",
                        Encryption.keyPair.public,
                        Random.nextBytes(4)
                )
        )
    }

    fun handlePing(session: Session, packet: PacketInPing)
    {
        session.sendPacket(PacketOutPingResponse(packet.payload))
    }
}
