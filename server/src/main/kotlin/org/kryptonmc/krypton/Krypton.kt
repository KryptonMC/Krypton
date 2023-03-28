/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.auth.GameProfileCache
import org.kryptonmc.krypton.config.KryptonConfig
import org.kryptonmc.krypton.server.Bootstrap
import org.kryptonmc.krypton.server.InitContext
import org.kryptonmc.krypton.server.StatisticsSerializer
import org.kryptonmc.krypton.ticking.TickSchedulerThread
import org.kryptonmc.krypton.util.executor.DefaultUncaughtExceptionHandler
import org.kryptonmc.krypton.world.chunk.VanillaChunkLoader
import org.kryptonmc.krypton.world.data.DefaultPlayerDataSerializer
import org.kryptonmc.krypton.world.data.DefaultWorldDataSerializer
import java.nio.file.Files
import java.nio.file.Path

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

        val cache = GameProfileCache(userCacheFile)
        cache.loadAll()

        val defaultWorldFolder = worldFolder.resolve(config.world.name)

        val playerDataFolder = defaultWorldFolder.resolve("playerdata")
        if (config.advanced.serializePlayerData && !Files.exists(playerDataFolder)) {
            try {
                Files.createDirectories(playerDataFolder)
            } catch (exception: Exception) {
                logger.error("Unable to create player data directory!", exception)
                return
            }
        }

        val worldDataSerializer = DefaultWorldDataSerializer(worldFolder)
        val playerDataSerializer = DefaultPlayerDataSerializer(playerDataFolder)
        val statsSerializer = StatisticsSerializer(defaultWorldFolder.resolve("stats"))
        val chunkLoader = VanillaChunkLoader(defaultWorldFolder)

        val initContext = InitContext(statsSerializer, worldDataSerializer, playerDataSerializer, chunkLoader)
        val server = KryptonServer(config, cache, initContext)
        if (!server.initialize()) {
            // We just return here if initialisation fails. The error will already have been logged.
            return
        }

        TickSchedulerThread(server).start()

        val shutdownThread = Thread({ server.stop() }, "Server Shutdown Thread")
        shutdownThread.uncaughtExceptionHandler = DefaultUncaughtExceptionHandler(logger)
        Runtime.getRuntime().addShutdownHook(shutdownThread)
    }
}
