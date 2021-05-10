/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
import org.kryptonmc.krypton.util.logger
import java.util.concurrent.atomic.AtomicReference

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

        val reference = AtomicReference<KryptonServer>()
        val mainThread = Thread({ reference.get().start() }, "Server Thread").apply {
            setUncaughtExceptionHandler { _, exception -> logger<KryptonServer>().error("Caught previously unhandled exception", exception) }
        }
        val server = KryptonServer(mainThread, disableGUI)
        reference.set(server)
        mainThread.start()
    }
}
