/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.auth.KryptonProfileCache
import org.kryptonmc.krypton.config.KryptonConfig
import org.kryptonmc.krypton.util.Bootstrap
import org.kryptonmc.krypton.util.logger
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicReference

fun main(args: Array<String>) {
    KryptonCLI().main(args)
}

private class KryptonCLI : CliktCommand(
    """
        All of the command-line options for configuring Krypton. Useful for use in scripts.
        Note: All options provided here will OVERRIDE those provided in the config!
    """.trimIndent(),
    name = "Krypton"
) {

    // Flags
    private val version by option("-v", "--version")
        .flag()
        .help("Prints the version and exits")
    private val configOnly by option("--init", "--config-only", "--init-settings")
        .flag()
        .help("Creates the config file and exits")

    // Folders
    private val configFile by option("--config", "--config-file")
        .help("Configuration file path for the server")
        .path(canBeDir = false, canBeSymlink = false, mustBeReadable = true, mustBeWritable = true)
        .default(Path.of("config.conf"))
    private val userCacheFile by option("--user-cache-file")
        .help("File where users' profiles are stored.")
        .path(canBeDir = false, canBeSymlink = false, mustBeReadable = true, mustBeWritable = true)
        .default(Path.of("usercache.json"))
    private val worldFolder by option("--world-folder", "--world-directory", "--world-dir", "--universe")
        .help("Folder where worlds are stored")
        .path(canBeFile = false, canBeSymlink = false, mustBeReadable = true, mustBeWritable = true)
        .default(Path.of(""))

    override fun run() {
        if (version) {
            println("Krypton version ${KryptonPlatform.version} for Minecraft ${KryptonPlatform.minecraftVersion}")
            return
        }

        val logger = LogManager.getLogger("Krypton")
        logger.info("Starting Krypton server version ${KryptonPlatform.version} for Minecraft ${KryptonPlatform.minecraftVersion}...")

        // Run the bootstrap
        Bootstrap.preload()
        Bootstrap.validate()

        val config = KryptonConfig.load(configFile)
        if (configOnly) {
            logger.info("Successfully initialized config file located at $configFile.")
            return
        }

        // This logic comes from vanilla. We should probably just use the main thread, though this may greater ensure parity
        // with vanilla's buggy mess. Not sure if this is actually the case though.
        val reference = AtomicReference<KryptonServer>()
        val serverThread = Thread({ reference.get().start() }, "Tick Thread").apply {
            setUncaughtExceptionHandler { _, exception ->
                logger<KryptonServer>().error("Caught unhandled exception in ticking thread!", exception)
                reference.get().stop()
            }
        }
        val cache = KryptonProfileCache(userCacheFile)
        cache.loadAll()
        val server = KryptonServer(config, cache, worldFolder)
        reference.set(server)
        serverThread.start()
    }
}
