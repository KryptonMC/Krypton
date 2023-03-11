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
