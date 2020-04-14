package me.bristermitten.minekraft

import me.bristermitten.minekraft.channel.NettyThread
import me.bristermitten.minekraft.packet.PacketLoader

fun main()
{
    PacketLoader.loadAll()

    NettyThread().start()
    println("Started Netty Thread.")

    while (true)
    {
        SessionStorage.pulse()
    }
}
