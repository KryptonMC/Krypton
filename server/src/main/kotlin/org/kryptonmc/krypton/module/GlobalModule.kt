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
package org.kryptonmc.krypton.module

import dev.misfitlabs.kotlinguice4.KotlinModule
import org.kryptonmc.api.Server
import org.kryptonmc.api.command.CommandManager
import org.kryptonmc.api.command.ConsoleSender
import org.kryptonmc.api.event.EventBus
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.api.scheduling.Scheduler
import org.kryptonmc.api.service.ServicesManager
import org.kryptonmc.api.status.StatusInfo
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.WorldManager
import org.kryptonmc.krypton.KryptonServer

class GlobalModule(private val server: KryptonServer) : KotlinModule() {

    override fun configure() {
        bind<Server>().toInstance(server)
        bind<Server.ServerInfo>().toInstance(server.info)
        bind<WorldManager>().toInstance(server.worldManager)
        bind<CommandManager>().toInstance(server.commandManager)
        bind<PluginManager>().toInstance(server.pluginManager)
        bind<ServicesManager>().toInstance(server.servicesManager)
        bind<EventBus>().toInstance(server.eventBus)
        bind<Scheduler>().toInstance(server.scheduler)
        bind<StatusInfo>().toInstance(server.status)
        bind<ConsoleSender>().toInstance(server.console)
        bind<World>().toInstance(server.worldManager.default)
    }
}
