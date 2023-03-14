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
package org.kryptonmc.krypton.service

import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.service.ServiceProvider
import org.kryptonmc.api.service.ServicesManager
import org.kryptonmc.krypton.KryptonServer
import java.util.concurrent.ConcurrentHashMap

class KryptonServicesManager(private val server: KryptonServer) : ServicesManager {

    private val providers = ConcurrentHashMap<Class<*>, KryptonServiceProvider<*>>()

    override fun <T> register(plugin: Any, type: Class<T>, service: T): ServiceProvider<T> {
        val container = requireNotNull(server.pluginManager.fromInstance(plugin)) { "Provided plugin $plugin is not a valid plugin instance!" }
        return register(container, type, service)
    }

    override fun <T> register(plugin: PluginContainer, type: Class<T>, service: T): ServiceProvider<T> {
        val provider = KryptonServiceProvider(plugin, type, service)
        providers.put(type, provider)
        return provider
    }

    override fun <T> provide(clazz: Class<T>): T? = getProvider(clazz)?.service

    @Suppress("UNCHECKED_CAST")
    override fun <T> getProvider(clazz: Class<T>): ServiceProvider<T>? = providers.get(clazz) as? ServiceProvider<T>
}
