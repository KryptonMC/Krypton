package me.bristermitten.minekraft

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.bristermitten.minekraft.channel.NettyProcess
import me.bristermitten.minekraft.encryption.Encryption
import me.bristermitten.minekraft.extension.logger
import me.bristermitten.minekraft.packet.PacketLoader
import me.bristermitten.minekraft.registry.RegistryManager
import me.bristermitten.minekraft.registry.tags.TagManager
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