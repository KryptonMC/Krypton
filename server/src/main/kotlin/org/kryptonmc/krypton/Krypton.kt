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
import org.kryptonmc.krypton.auth.KryptonProfileCache
import org.kryptonmc.krypton.config.KryptonConfig
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.locale.TranslationManager
import org.kryptonmc.krypton.util.Bootstrap
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.world.DataPackConfig
import org.kryptonmc.krypton.world.KryptonGameRuleHolder
import org.kryptonmc.krypton.world.data.PrimaryWorldData
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.storage.WorldDataStorage
import java.nio.file.Path
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference

// max memory in megabytes (bytes / 1024 / 1024)
private val MAX_MEMORY = Runtime.getRuntime().maxMemory() / 1024L / 1024L
private const val MEMORY_WARNING_THRESHOLD = 512

fun main(args: Array<String>) = KryptonCLI().main(args)

class KryptonCLI : CliktCommand(
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

    // Config options
    private val ip by option()
        .help("The IP to listen on")
    private val port by option("-p", "--port")
        .int()
        .help("The port to bind to")
    private val online by option("-o", "--online", "--online-mode")
        .flag(default = true)
        .help("Whether users should be authenticated with Mojang")
    private val maxPlayers by option("-P", "--players", "--max-players")
        .int()
        .help("The maximum amount of players")
    private val motd by option("--motd", metavar = "COMPONENT")
        .convert { LegacyComponentSerializer.legacyAmpersand().deserialize(it) }
        .help("The message of the day sent to players")
    private val worldName by option("-w", "--world", "--world-name")
        .help("The name of the world folder for the default world")

    // Folders
    private val configFile by option("-c", "--config")
        .help("Configuration file path for the server")
        .path(canBeDir = false, canBeSymlink = false, mustBeReadable = true, mustBeWritable = true)
        .default(Path.of("config.conf"))
    private val worldFolder by option("-W", "--world-folder", "--world-directory", "--world-dir", "--universe")
        .help("Folder where worlds are stored")
        .path(canBeFile = false, canBeSymlink = false, mustBeReadable = true, mustBeWritable = true)
        .default(Path.of("."))
    private val userCacheFile by option("--usercache-file", "-ucf")
        .help("The file where authenticated users have their details cached")
        .path(canBeDir = false, canBeSymlink = false, mustBeReadable = true, mustBeWritable = true)
        .default(Path.of("usercache.json"))

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
            Messages.VERSION_INFO.print(KryptonPlatform.version, KryptonPlatform.minecraftVersion)
            return
        }
        val logger = logger("Krypton")
        Messages.LOAD.info(logger, KryptonPlatform.version, KryptonPlatform.minecraftVersion)
        if (MAX_MEMORY < MEMORY_WARNING_THRESHOLD) Messages.LOAD_LOW_MEMORY.warn(logger, MEMORY_WARNING_THRESHOLD.toString(), KryptonPlatform.version)

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
            server = loadedConfig.server.copy(ip = ip ?: loadedConfig.server.ip, port = port ?: loadedConfig.server.port, onlineMode = online),
            status = loadedConfig.status.copy(motd = motd ?: loadedConfig.status.motd, maxPlayers = maxPlayers ?: loadedConfig.status.maxPlayers),
            world = loadedConfig.world.copy(name = worldName ?: loadedConfig.world.name)
        )

        // Setup registries and create world storage access
        val storageAccess = WorldDataStorage(worldFolder).createAccess(config.world.name)
        val profileCache = KryptonProfileCache(userCacheFile)

        val worldData = storageAccess.loadData(NBTOps, DataPackConfig.DEFAULT) ?: PrimaryWorldData(
            config.world.name,
            config.world.gamemode,
            config.world.difficulty,
            config.world.hardcore,
            KryptonGameRuleHolder(),
            DataPackConfig.DEFAULT,
            WorldGenerationSettings.default()
        )

        storageAccess.saveData(worldData)
        val reference = AtomicReference<KryptonServer>()
        val serverThread = Thread({ reference.get().start() }, "Server Thread").apply {
            setUncaughtExceptionHandler { _, exception ->
                logger<KryptonServer>().error("Caught an uncaught exception whilst ticking", exception)
            }
        }
        val server = KryptonServer(
            storageAccess,
            worldData,
            config,
            profileCache,
            configFile,
            worldFolder
        )
        reference.set(server)
        serverThread.start()
    }
}
