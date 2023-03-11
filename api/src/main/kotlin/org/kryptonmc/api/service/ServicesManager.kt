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
@file:JvmSynthetic
package org.kryptonmc.api.service

import org.kryptonmc.api.plugin.PluginContainer

/**
 * The manager of services.
 */
public interface ServicesManager {

    /**
     * Gets the service for the given [clazz], or returns null if there is no
     * service registered for the given [clazz].
     *
     * @param T the service type
     * @param clazz the service class
     * @return the service, or null if not present
     */
    public fun <T> provide(clazz: Class<T>): T?

    /**
     * Gets the service provider for the service of the given [clazz] type, or
     * returns null if there is no service provider for the given [clazz].
     *
     * @param T the service type
     * @param clazz the service class
     * @return the service provider, or null if not present
     */
    public fun <T> getProvider(clazz: Class<T>): ServiceProvider<T>?

    /**
     * Registers a new service to this services manager.
     *
     * @param T the service type
     * @param plugin the plugin that registered the service
     * @param type the type of the service being provided
     * @param service the service being provided
     * @return the registered service provider
     */
    public fun <T> register(plugin: Any, type: Class<T>, service: T): ServiceProvider<T>

    /**
     * Registers a new service to this services manager.
     *
     * @param T the service type
     * @param plugin the plugin that registered the service
     * @param type the type of the service being provided
     * @param service the service being provided
     * @return the registered service provider
     */
    public fun <T> register(plugin: PluginContainer, type: Class<T>, service: T): ServiceProvider<T>
}
