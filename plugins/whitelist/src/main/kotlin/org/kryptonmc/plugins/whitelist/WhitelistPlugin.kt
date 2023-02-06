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
package org.kryptonmc.plugins.whitelist

import com.google.inject.Inject
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.Server
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.command.CommandMeta
import org.kryptonmc.api.event.Event
import org.kryptonmc.api.event.EventNode
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.server.ServerStartEvent
import org.kryptonmc.api.event.server.ServerStopEvent
import org.kryptonmc.api.plugin.annotation.DataFolder
import org.kryptonmc.api.scheduling.Task
import org.kryptonmc.api.scheduling.TaskTime
import org.kryptonmc.plugins.whitelist.api.WhitelistManager
import org.kryptonmc.plugins.whitelist.commands.WhitelistCommand
import org.kryptonmc.plugins.whitelist.config.WhitelistConfig
import org.kryptonmc.plugins.whitelist.storage.KryptonWhitelistManager
import org.kryptonmc.plugins.whitelist.storage.WhitelistStorage
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.nio.file.Path

class WhitelistPlugin @Inject constructor(
    private val server: Server,
    private val logger: Logger,
    private val eventNode: EventNode<Event>,
    @DataFolder
    private val dataFolder: Path
) {

    private val storage = WhitelistStorage(dataFolder.resolve("whitelist.json"), logger)
    private val manager = KryptonWhitelistManager(server, storage)
    private val config = loadConfig()
    private var autoSaveTask: Task? = null

    @Listener
    fun onStart(event: ServerStartEvent) {
        logger.info("Loading whitelist...")
        manager.load()
        setupAutoSave()
        registerCommands()
        eventNode.registerListeners(WhitelistListener(logger, storage))
        server.servicesManager.register(this, WhitelistManager::class.java, manager)
    }

    @Listener
    fun onStop(event: ServerStopEvent) {
        autoSaveTask?.cancel()
        logger.info("Saving whitelist...")
        manager.save()
    }

    private fun loadConfig(): WhitelistConfig {
        val loader = HoconConfigurationLoader.builder().path(dataFolder.resolve("config.conf")).build()
        val node = loader.load()
        val config = node.get(WhitelistConfig::class.java) ?: error("Cannot load config!")
        loader.save(node)
        return config
    }

    private fun setupAutoSave() {
        autoSaveTask = server.scheduler.buildTask {
            if (config.logAutoSave) logger.info("Starting whitelist auto save...")
            manager.save()
            if (config.logAutoSave) logger.info("Whitelist auto save complete.")
        }.delay(TaskTime.ticks(config.autoSaveInterval)).period(TaskTime.ticks(config.autoSaveInterval)).schedule()
    }

    private fun registerCommands() {
        val whitelistCommand = BrigadierCommand.of(WhitelistCommand(config, storage).create())
        server.commandManager.register(whitelistCommand, CommandMeta.builder("whitelist").build())
    }
}
