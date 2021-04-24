package org.kryptonmc.krypton

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
import org.kryptonmc.krypton.util.logger

// max memory in megabytes (bytes / 1024 / 1024)
private val MAX_MEMORY = Runtime.getRuntime().maxMemory() / 1024L / 1024L
private const val MEMORY_WARNING_THRESHOLD = 512

fun main(args: Array<String>) = KryptonCLI().main(args)

/**
 * The CLI handler for Krypton
 */
class KryptonCLI : CliktCommand() {
    private val disableGUI by option("-nogui", "--disable-gui").flag()
    private val version by option("-v", "--version").flag()

    override fun run() {
        if (version) {
            println("Krypton version ${KryptonServerInfo.version} for Minecraft ${KryptonServerInfo.minecraftVersion}")
            return
        }
        val logger = logger("Krypton")
        logger.info("Starting Krypton server version ${KryptonServerInfo.version} for Minecraft ${KryptonServerInfo.minecraftVersion}")

        if (MAX_MEMORY < MEMORY_WARNING_THRESHOLD) {
            logger.warn("You're starting the server with $MEMORY_WARNING_THRESHOLD megabytes of RAM.")
            logger.warn("Consider starting it with more by using \"java -Xmx1024M -Xms1024M -jar Krypton-${KryptonServerInfo.version}.jar\" to start it with 1 GB RAM")
        }

        KryptonServer(disableGUI).start()
    }
}
