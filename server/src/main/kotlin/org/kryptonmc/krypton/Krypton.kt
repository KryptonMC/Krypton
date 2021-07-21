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
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
import org.kryptonmc.krypton.config.KryptonConfig
import org.kryptonmc.krypton.config.category.ServerCategory
import org.kryptonmc.krypton.config.category.StatusCategory
import org.kryptonmc.krypton.config.category.WorldCategory
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.locale.TranslationManager
import org.kryptonmc.krypton.pack.repository.FolderRepositorySource
import org.kryptonmc.krypton.pack.repository.KryptonRepositorySource
import org.kryptonmc.krypton.pack.repository.PackRepository
import org.kryptonmc.krypton.pack.repository.PackSource
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.registry.ops.RegistryReadOps
import org.kryptonmc.krypton.server.ServerResources
import org.kryptonmc.krypton.util.BACKGROUND_EXECUTOR
import org.kryptonmc.krypton.util.Bootstrap
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.world.DataPackConfig
import java.nio.file.Path
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference

// max memory in megabytes (bytes / 1024 / 1024)
private val MAX_MEMORY = Runtime.getRuntime().maxMemory() / 1024L / 1024L
private const val MEMORY_WARNING_THRESHOLD = 512

fun main(args: Array<String>) = KryptonCLI().main(args)

class KryptonCLI : CliktCommand() {

    // Flags
    private val version by option("-v", "--version").flag().help("Prints the version and exits")
    private val configOnly by option("--init", "--config-only", "--init-settings").flag()
        .help("Creates the config file and exits")
    private val safeMode by option("-s", "--safe", "--safe-mode").flag().help("If set, only the built-in data pack is loaded")

    // Config options
    private val ip by option("-ip", "--ip").help("The IP to listen on").default(ServerCategory.DEFAULT_IP)
    private val port by option("-p", "--port").int().help("The port to bind to").default(ServerCategory.DEFAULT_PORT)
    private val online by option("-o", "--online-mode").flag(default = true)
        .help("Whether users should be authenticated with Mojang")
    private val maxPlayers by option("-p", "--players", "--max-players").int()
        .help("The maximum amount of players")
        .default(StatusCategory.DEFAULT_MAX_PLAYERS)
    private val motd by option("--motd").convert { LegacyComponentSerializer.legacyAmpersand().deserialize(it) }
        .help("The message of the day sent to players")
        .default(StatusCategory.DEFAULT_MOTD)
    private val worldName by option("-w", "--world", "--world-name")
        .help("The name of the world folder for the default world")
        .default(WorldCategory.DEFAULT_NAME)

    // Folders
    private val configFile by option("-c", "--config")
        .path(canBeDir = false, canBeSymlink = false, mustBeReadable = true, mustBeWritable = true)
        .default(Path.of("config.conf"))
        .help("Configuration file path for the server")
    private val worldFolder by option("--world-folder", "--world-directory", "--world-dir", "--universe")
        .path(canBeFile = false, canBeSymlink = false, mustBeReadable = true, mustBeWritable = true)
        .default(Path.of("."))
        .help("Folder where worlds are stored")

    // Other settings
    private val locale by option("-l", "--locale").convert {
        val split = it.split("[_-]".toRegex())
        if (split.isEmpty()) return@convert Locale.ENGLISH
        if (split.size == 2) return@convert Locale(split[0], split[1])
        Locale(split[0])
    }.default(Locale.ENGLISH)

    override fun run() {
        TranslationManager.reload(locale)
        if (version) {
            Messages.VERSION_INFO.print(KryptonServerInfo.version, KryptonServerInfo.minecraftVersion)
            return
        }
        val logger = logger("Krypton")
        Messages.LOAD.info(logger, KryptonServerInfo.version, KryptonServerInfo.minecraftVersion)
        if (MAX_MEMORY < MEMORY_WARNING_THRESHOLD) Messages.LOAD_LOW_MEMORY.warn(logger, MEMORY_WARNING_THRESHOLD.toString(), KryptonServerInfo.version)

        // Run the bootstrap
        Bootstrap.preload()
        Bootstrap.validate()

        // Load the config
        val loadedConfig = KryptonConfig.load(configFile)
        if (configOnly) {
            logger.info("Successfully initialized config file located at $configFile.")
            return
        }
        // Populate the config with CLI parameters
        val config = loadedConfig.copy(
            server = loadedConfig.server.copy(ip = ip, port = port, onlineMode = online),
            status = loadedConfig.status.copy(motd = motd, maxPlayers = maxPlayers),
            world = loadedConfig.world.copy(name = worldName)
        )

        // Load the resources
        val registryHolder = RegistryHolder.builtin()
        val previousPacks = DataPackConfig.DEFAULT // TODO: Actually load the data packs properly
        if (safeMode) logger.warn("Safe mode active, only the built-in data pack will be loaded.")

        val packRepository = PackRepository(KryptonRepositorySource(), FolderRepositorySource(CURRENT_DIRECTORY.resolve("datapacks"), PackSource.WORLD))
        val packConfig = packRepository.configure(previousPacks, safeMode) // TODO: Use this with the new world data

        val resources = try {
            ServerResources.load(packRepository.openAllSelected(), registryHolder, BACKGROUND_EXECUTOR, Runnable::run).get()
        } catch (exception: Exception) {
            logger.warn("Failed to load data packs! Cannot proceed with server load! To run the server with only the built-in data packs, set the safe mode flag on start up.")
            return
        }.apply { updateGlobals() }

        // TODO: Use this to load world generation settings
        val ops = RegistryReadOps.createAndLoad(NBTOps, resources.manager, registryHolder)

        spin { KryptonServer(registryHolder, packRepository, resources, config, worldFolder) }
    }
}

// I could not resist
private fun spin(creator: (Thread) -> KryptonServer): KryptonServer {
    val reference = AtomicReference<KryptonServer>()
    val serverThread = Thread({ reference.get().start() }, "Server Thread").apply {
        setUncaughtExceptionHandler { _, exception -> logger<KryptonServer>().error(exception) }
    }
    val server = creator(serverThread)
    reference.set(server)
    serverThread.start()
    return server
}
