/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
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
