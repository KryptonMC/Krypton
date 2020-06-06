package me.bristermitten.minekraft

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.bristermitten.minekraft.channel.NettyThread
import me.bristermitten.minekraft.encryption.Encryption
import me.bristermitten.minekraft.packet.PacketLoader
import org.slf4j.LoggerFactory.getLogger
import java.security.SecureRandom

class Server(
    port: Int
) {
    internal val encryption = Encryption()
    internal val nettyThread = NettyThread(this, port)
    internal val random: SecureRandom = SecureRandom()
    private val logger = getLogger(javaClass)

    fun start() {
        PacketLoader.loadAll()
        logger.info("Starting MineKraft Server...")
        logger.info("Starting Netty...")
        GlobalScope.launch {
            nettyThread.run()
        }
        while (true) {
            //Keep server alive
        }
    }
}
