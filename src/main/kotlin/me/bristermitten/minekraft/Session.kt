package me.bristermitten.minekraft

import io.netty.channel.Channel
import me.bristermitten.minekraft.extension.pollEach
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketHandler
import me.bristermitten.minekraft.packet.PacketState
import java.util.*

class Session(val channel: Channel)
{

    private val handler = PacketHandler()

    @Volatile
    var currentState: PacketState = PacketState.HANDSHAKE

    private val outQueue: Queue<Packet> = LinkedList()
    private val inQueue: Queue<Packet> = LinkedList()

    fun sendPacket(packet: Packet)
    {
        synchronized(outQueue) {
            outQueue.add(packet)
        }
    }

    fun pulse()
    {
        synchronized(inQueue) {
            inQueue.pollEach {
                handler.handle(this, it)
            }
        }

        synchronized(outQueue) {
            outQueue.pollEach {
                channel.writeAndFlush(it)
            }
        }


    }

    fun receive(msg: Packet)
    {
        synchronized(inQueue) {
            inQueue.add(msg)
        }
    }
}
