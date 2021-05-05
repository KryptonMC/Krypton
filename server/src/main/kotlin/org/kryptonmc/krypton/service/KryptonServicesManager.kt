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

import org.kryptonmc.krypton.api.plugin.Plugin
import org.kryptonmc.krypton.api.service.ServiceProvider
import org.kryptonmc.krypton.api.service.ServicesManager
import java.util.concurrent.ConcurrentHashMap

class KryptonServicesManager : ServicesManager {

    private val providers = ConcurrentHashMap<Class<*>, MutableList<KryptonServiceProvider<*>>>()

    override fun <T> register(plugin: Plugin, clazz: Class<T>, service: T) = synchronized(providers) {
        providers.getOrPut(clazz) { mutableListOf() } += KryptonServiceProvider(plugin, clazz, service)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> provide(clazz: Class<T>): ServiceProvider<T>? = synchronized(providers) {
        providers[clazz] as? ServiceProvider<T>
    }
}
