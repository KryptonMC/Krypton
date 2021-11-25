/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
package org.kryptonmc.api.service

import org.kryptonmc.api.plugin.PluginContainer

/**
 * The manager of services.
 */
public interface ServicesManager {

    /**
     * Registers a new service to this services manager.
     *
     * @param plugin the plugin that registered the service
     * @param clazz the service class
     * @param service the service being provided
     */
    public fun <T> register(plugin: Any, clazz: Class<T>, service: T)

    /**
     * Gets the service for the given [clazz], or returns null if there is no
     * service registered for the given [clazz].
     *
     * @param clazz the service class
     * @return the service, or null if not present
     */
    public fun <T> provide(clazz: Class<T>): T?

    /**
     * Gets the service provider for the service of the given [clazz] type, or
     * returns null if there is no service provider for the given [clazz].
     *
     * @param clazz the service class
     * @return the service provider, or null if not present
     */
    public fun <T> provider(clazz: Class<T>): ServiceProvider<T>?
}

/**
 * Registers a new service to this services manager.
 *
 * @param plugin the plugin that registered the service
 * @param T the service type
 * @param service the service
 */
@JvmSynthetic
public inline fun <reified T> ServicesManager.register(plugin: PluginContainer, service: T) {
    register(plugin, T::class.java, service)
}

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
public inline fun <reified T> ServicesManager.provider(): ServiceProvider<T>? = provider(T::class.java)
