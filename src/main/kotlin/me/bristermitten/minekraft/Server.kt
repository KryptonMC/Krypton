package me.bristermitten.minekraft

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.bristermitten.minekraft.channel.NettyProcess
import me.bristermitten.minekraft.encryption.Encryption
import me.bristermitten.minekraft.packet.PacketLoader
import me.bristermitten.minekraft.registry.RegistryManager
import me.bristermitten.minekraft.registry.tags.TagManager
import org.slf4j.LoggerFactory.getLogger
import java.security.SecureRandom

class Server(port: Int)
{
    internal val encryption = Encryption()
    internal val nettyThread = NettyProcess(this, port)
    internal val random: SecureRandom = SecureRandom()
    private val logger = getLogger(javaClass)

    val registryManager = RegistryManager()
    val tagManager = TagManager()

    fun start()
    {
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
