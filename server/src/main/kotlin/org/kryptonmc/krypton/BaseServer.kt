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

import org.kryptonmc.api.event.GlobalEventNode
import org.kryptonmc.api.registry.RegistryHolder
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.krypton.command.KryptonCommandManager
import org.kryptonmc.krypton.console.KryptonConsole
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.scheduling.KryptonScheduler
import org.kryptonmc.krypton.service.KryptonServicesManager
import org.kryptonmc.krypton.user.KryptonUserManager
import org.kryptonmc.krypton.util.KryptonFactoryProvider
import org.kryptonmc.krypton.world.KryptonWorldManager
import java.util.UUID

interface BaseServer : ServerAudience {

    override val console: KryptonConsole

    override val worldManager: KryptonWorldManager
    override val commandManager: KryptonCommandManager
    override val pluginManager: KryptonPluginManager
    override val eventNode: GlobalEventNode
    override val servicesManager: KryptonServicesManager
    override val scheduler: KryptonScheduler
    override val userManager: KryptonUserManager

    override val platform: KryptonPlatform
        get() = KryptonPlatform
    override val registryHolder: RegistryHolder
        get() = KryptonRegistries.StaticHolder
    override val factoryProvider: FactoryProvider
        get() = KryptonFactoryProvider

    fun isRunning(): Boolean

    fun stop()

    override fun getPlayer(name: String): KryptonPlayer? = playerManager.getPlayer(name)

    override fun getPlayer(uuid: UUID): KryptonPlayer? = playerManager.getPlayer(uuid)
}
