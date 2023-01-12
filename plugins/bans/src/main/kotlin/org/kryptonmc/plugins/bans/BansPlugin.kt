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
package org.kryptonmc.plugins.bans

import com.google.inject.Inject
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.Server
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.command.CommandMeta
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.server.ServerStartEvent
import org.kryptonmc.api.event.server.ServerStopEvent
import org.kryptonmc.api.plugin.annotation.DataFolder
import org.kryptonmc.api.scheduling.Task
import org.kryptonmc.plugins.bans.api.BanManager
import org.kryptonmc.plugins.bans.commands.BanCommand
import org.kryptonmc.plugins.bans.commands.BanIpCommand
import org.kryptonmc.plugins.bans.commands.PardonCommand
import org.kryptonmc.plugins.bans.commands.PardonIpCommand
import org.kryptonmc.plugins.bans.config.BansConfig
import org.kryptonmc.plugins.bans.storage.BanStorage
import org.kryptonmc.plugins.bans.storage.KryptonBanManager
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.nio.file.Path
import java.util.concurrent.TimeUnit

class BansPlugin @Inject constructor(
    private val server: Server,
    private val logger: Logger,
    @DataFolder
    private val dataFolder: Path
) {

    private val manager = KryptonBanManager(server, BanStorage(dataFolder.resolve("bans.json"), logger))
    private val config = loadConfig()
    private var autoSaveTask: Task? = null

    @Listener
    fun onStart(event: ServerStartEvent) {
        logger.info("Loading ban list...")
        manager.load()
        setupAutoSave()
        registerCommands()
        server.eventManager.registerListener(this, BanListener(logger, manager))
        server.servicesManager.register(this, BanManager::class.java, manager)
    }

    @Listener
    fun onStop(event: ServerStopEvent) {
        autoSaveTask?.cancel()
        logger.info("Saving ban list...")
        manager.save()
    }

    private fun loadConfig(): BansConfig {
        val loader = HoconConfigurationLoader.builder().path(dataFolder.resolve("config.conf")).build()
        val node = loader.load()
        val config = node.get(BansConfig::class.java) ?: error("Cannot load config!")
        loader.save(node)
        return config
    }

    private fun setupAutoSave() {
        val interval = config.autoSaveInterval * MILLISECONDS_PER_TICK
        autoSaveTask = server.scheduler.schedule(this, interval, interval, TimeUnit.MILLISECONDS) {
            if (config.logAutoSave) logger.info("Starting ban list auto save...")
            manager.save()
            if (config.logAutoSave) logger.info("Ban list auto save complete.")
        }
    }

    private fun registerCommands() {
        val banCommand = BrigadierCommand.of(BanCommand(manager).create())
        val banIpCommand = BrigadierCommand.of(BanIpCommand(manager).create())
        server.commandManager.register(banCommand, CommandMeta.builder("ban").build())
        server.commandManager.register(banIpCommand, CommandMeta.builder("ban-ip").build())
        val pardonCommand = BrigadierCommand.of(PardonCommand(manager).create())
        val pardonIpCommand = BrigadierCommand.of(PardonIpCommand(manager).create())
        server.commandManager.register(pardonCommand, CommandMeta.builder("pardon").build())
        server.commandManager.register(pardonIpCommand, CommandMeta.builder("pardon-ip").build())
    }

    companion object {

        private const val MILLISECONDS_PER_TICK = 50
    }
}
