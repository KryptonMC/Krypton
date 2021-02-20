package org.kryptonmc.krypton

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kryptonmc.krypton.channel.NettyProcess
import org.kryptonmc.krypton.encryption.Encryption
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.packet.PacketLoader
import org.kryptonmc.krypton.registry.RegistryManager
import org.kryptonmc.krypton.registry.tags.TagManager
import java.security.SecureRandom

class Server(port: Int) {

    internal val encryption = Encryption()
    private val nettyThread = NettyProcess(this, port)
    internal val random: SecureRandom = SecureRandom()

    val registryManager = RegistryManager()
    val tagManager = TagManager()

    fun start() {
        PacketLoader.loadAll()
        LOGGER.info("Starting MineKraft Server...")
        LOGGER.info("Starting Netty...")

        GlobalScope.launch {
            nettyThread.run()
        }

        while (true) {
            //Keep server alive
        }
    }

    companion object {

        private val LOGGER = logger<Server>()
    }
}