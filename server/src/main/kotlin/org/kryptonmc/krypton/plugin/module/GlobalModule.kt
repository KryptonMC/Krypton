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
package org.kryptonmc.krypton.plugin.module

import com.google.inject.name.Names
import dev.misfitlabs.kotlinguice4.KotlinModule
import org.kryptonmc.api.Platform
import org.kryptonmc.api.Server
import org.kryptonmc.api.command.CommandManager
import org.kryptonmc.api.command.ConsoleSender
import org.kryptonmc.api.event.GlobalEventNode
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.api.scheduling.Scheduler
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.service.ServicesManager
import org.kryptonmc.api.user.UserManager
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.world.WorldManager
import org.kryptonmc.krypton.KryptonServer

class GlobalModule(private val server: KryptonServer, private val pluginContainers: Collection<PluginContainer>) : KotlinModule() {

    override fun configure() {
        bind<Server>().toInstance(server)
        bind<Platform>().toInstance(server.platform)
        bind<WorldManager>().toInstance(server.worldManager)
        bind<CommandManager>().toInstance(server.commandManager)
        bind<PluginManager>().toInstance(server.pluginManager)
        bind<ServicesManager>().toInstance(server.servicesManager)
        bind<GlobalEventNode>().toInstance(server.eventNode)
        bind<FactoryProvider>().toInstance(server.factoryProvider)
        bind<UserManager>().toInstance(server.userManager)
        bind<Scheduler>().toInstance(server.scheduler)
        bind<ConsoleSender>().toInstance(server.console)
        bind<Scoreboard>().toInstance(server.scoreboard)
        pluginContainers.forEach { bind<PluginContainer>().annotatedWith(Names.named(it.description.id)).toInstance(it) }
    }
}
