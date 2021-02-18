package me.bristermitten.minekraft

import java.net.InetAddress
import java.util.concurrent.atomic.AtomicInteger

object ServerStorage {

    val playerCount = AtomicInteger(0)
    val nextEntityId = AtomicInteger(0)

    val SERVER_IP: InetAddress = InetAddress.getLocalHost()
}
