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
package org.kryptonmc.krypton.service

import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.service.AFKService
import org.kryptonmc.api.service.ServiceProvider
import org.kryptonmc.api.service.ServicesManager
import org.kryptonmc.api.service.VanishService
import org.kryptonmc.api.service.register
import org.kryptonmc.api.user.ban.BanService
import org.kryptonmc.api.user.whitelist.WhitelistService
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.plugin.server.ServerPluginContainer
import org.kryptonmc.krypton.server.ban.KryptonBanService
import org.kryptonmc.krypton.server.whitelist.KryptonWhitelistService
import java.util.concurrent.ConcurrentHashMap

class KryptonServicesManager(private val server: KryptonServer) : ServicesManager {

    private val providers = ConcurrentHashMap<Class<*>, KryptonServiceProvider<*>>()

    fun bootstrap() {
        register<VanishService>(ServerPluginContainer, KryptonVanishService())
        register<AFKService>(ServerPluginContainer, KryptonAFKService())
        register<WhitelistService>(ServerPluginContainer, KryptonWhitelistService(server))
        register<BanService>(ServerPluginContainer, KryptonBanService(server))
    }

    override fun <T> register(plugin: Any, type: Class<T>, service: T): ServiceProvider<T> {
        val container = requireNotNull(KryptonPluginManager.fromInstance(plugin)) { "Provided plugin $plugin is not a valid plugin instance!" }
        return register(container, type, service)
    }

    override fun <T> register(plugin: PluginContainer, type: Class<T>, service: T): ServiceProvider<T> {
        val provider = KryptonServiceProvider(plugin, type, service)
        providers[type] = provider
        return provider
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> provide(clazz: Class<T>): T? = providers[clazz]?.service as? T

    @Suppress("UNCHECKED_CAST")
    override fun <T> provider(clazz: Class<T>): ServiceProvider<T>? = providers[clazz] as? ServiceProvider<T>
}
