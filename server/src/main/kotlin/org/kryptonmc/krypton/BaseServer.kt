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

import org.kryptonmc.api.registry.RegistryManager
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.krypton.auth.KryptonProfileCache
import org.kryptonmc.krypton.command.KryptonCommandManager
import org.kryptonmc.krypton.console.KryptonConsole
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.event.KryptonEventManager
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.scheduling.KryptonScheduler
import org.kryptonmc.krypton.service.KryptonServicesManager
import org.kryptonmc.krypton.user.KryptonUserManager
import org.kryptonmc.krypton.util.KryptonFactoryProvider
import org.kryptonmc.krypton.world.KryptonWorldManager
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import java.util.UUID

interface BaseServer : ServerAudience {

    override val console: KryptonConsole
    override val scoreboard: KryptonScoreboard

    override val profileCache: KryptonProfileCache
    override val worldManager: KryptonWorldManager
    override val commandManager: KryptonCommandManager
    override val pluginManager: KryptonPluginManager
    override val eventManager: KryptonEventManager
    override val servicesManager: KryptonServicesManager
    override val scheduler: KryptonScheduler
    override val userManager: KryptonUserManager

    override val platform: KryptonPlatform
        get() = KryptonPlatform
    override val registryManager: RegistryManager
        get() = KryptonRegistries.ManagerImpl
    override val factoryProvider: FactoryProvider
        get() = KryptonFactoryProvider

    fun isRunning(): Boolean

    fun run()

    fun stop(waitForServer: Boolean)

    override fun getPlayer(name: String): KryptonPlayer? = playerManager.getPlayer(name)

    override fun getPlayer(uuid: UUID): KryptonPlayer? = playerManager.getPlayer(uuid)
}
