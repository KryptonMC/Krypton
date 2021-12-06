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

import com.google.inject.name.Names
import dev.misfitlabs.kotlinguice4.KotlinModule
import org.kryptonmc.api.Platform
import org.kryptonmc.api.Server
import org.kryptonmc.api.auth.ProfileCache
import org.kryptonmc.api.command.CommandManager
import org.kryptonmc.api.command.ConsoleSender
import org.kryptonmc.api.event.EventManager
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.api.registry.RegistryManager
import org.kryptonmc.api.scheduling.Scheduler
import org.kryptonmc.api.service.ServicesManager
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.world.WorldManager
import org.kryptonmc.krypton.KryptonServer

class GlobalModule(
    private val server: KryptonServer,
    private val pluginContainers: Collection<PluginContainer>
) : KotlinModule() {

    override fun configure() {
        bind<Server>().toInstance(server)
        bind<Platform>().toInstance(server.platform)
        bind<WorldManager>().toInstance(server.worldManager)
        bind<CommandManager>().toInstance(server.commandManager)
        bind<PluginManager>().toInstance(server.pluginManager)
        bind<ServicesManager>().toInstance(server.servicesManager)
        bind<EventManager>().toInstance(server.eventManager)
        bind<RegistryManager>().toInstance(server.registryManager)
        bind<FactoryProvider>().toInstance(server.factoryProvider)
        bind<ProfileCache>().toInstance(server.profileCache)
        bind<Scheduler>().toInstance(server.scheduler)
        bind<ConsoleSender>().toInstance(server.console)

        pluginContainers.forEach {
            bind<PluginContainer>().annotatedWith(Names.named(it.description.id)).toInstance(it)
        }
    }
}
