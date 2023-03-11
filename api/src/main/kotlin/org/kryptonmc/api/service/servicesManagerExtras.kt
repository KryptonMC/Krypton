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
 * Gets the service for the given type [T], or returns null if there is no
 * registered service for the given type [T].
 *
 * @param T the service type
 * @return the service, or null if not present
 */
@JvmSynthetic
public inline fun <reified T> ServicesManager.provide(): T? = provide(T::class.java)

/**
 * Gets the provider for the given type [T], or returns null if there is no
 * provider for the given type [T].
 *
 * @param T the service type
 * @return the service provider, or null if not present
 */
@JvmSynthetic
public inline fun <reified T> ServicesManager.getProvider(): ServiceProvider<T>? = getProvider(T::class.java)

/**
 * Registers a new service to this services manager.
 *
 * @param T the service type
 * @param plugin the plugin that registered the service
 * @param service the service
 */
@JvmSynthetic
public inline fun <reified T> ServicesManager.register(plugin: PluginContainer, service: T) {
    register(plugin, T::class.java, service)
}

/**
 * Registers a new service to this services manager.
 *
 * @param T the service type
 * @param plugin the plugin that registered the service
 * @param service the service
 */
@JvmSynthetic
public inline fun <reified T> ServicesManager.register(plugin: Any, service: T) {
    register(plugin, T::class.java, service)
}
