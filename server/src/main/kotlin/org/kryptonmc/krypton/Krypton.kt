package org.kryptonmc.krypton

import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
import org.kryptonmc.krypton.extension.logger

fun main() {
    val logger = logger("Krypton")
    logger.info("Starting Krypton server version ${KryptonServerInfo.version} for Minecraft ${KryptonServerInfo.minecraftVersion}")

    if (MAX_MEMORY < MEMORY_WARNING_THRESHOLD) {
        logger.warn("You're starting the server with $MEMORY_WARNING_THRESHOLD megabytes of RAM.")
        logger.warn("Consider starting it with more by using \"java -Xmx1024M -Xms1024M -jar Krypton-${KryptonServerInfo.version}.jar\" to start it with 1 GB RAM")
    }

    KryptonServer().start()
}

// max memory in megabytes (bytes / 1024 / 1024)
private val MAX_MEMORY = Runtime.getRuntime().maxMemory() / 1024L / 1024L
private const val MEMORY_WARNING_THRESHOLD = 512