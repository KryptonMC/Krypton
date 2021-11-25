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
package org.kryptonmc.krypton.service

import org.kryptonmc.api.service.ServiceProvider
import org.kryptonmc.api.service.ServicesManager
import org.kryptonmc.api.service.register
import org.kryptonmc.api.service.VanishService
import org.kryptonmc.krypton.plugin.server.ServerPluginContainer
import java.util.concurrent.ConcurrentHashMap

object KryptonServicesManager : ServicesManager {

    private val providers = ConcurrentHashMap<Class<*>, KryptonServiceProvider<*>>()

    fun bootstrap() {
        register<VanishService>(ServerPluginContainer, KryptonVanishService())
    }

    override fun <T> register(plugin: Any, clazz: Class<T>, service: T) {
        providers[clazz] = KryptonServiceProvider(plugin, clazz, service)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> provide(clazz: Class<T>): T? = providers[clazz]?.service as? T

    @Suppress("UNCHECKED_CAST")
    override fun <T> provider(clazz: Class<T>): ServiceProvider<T>? = providers[clazz] as? ServiceProvider<T>
}
