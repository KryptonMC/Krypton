/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.service

/**
 * The manager of services.
 */
public interface ServicesManager {

    /**
     * Registers a new service to this services manager.
     *
     * @param plugin the plugin to register the service for
     * @param clazz the class of the service to register
     * @param service the service being provided
     */
    public fun <T> register(plugin: Any, clazz: Class<T>, service: T)

    /**
     * Retrieves a provider for a given class.
     *
     * @param clazz the class of the service
     * @return a [ServiceProvider] containing the service to be provided, or
     * null if there is no service registered with the given class [clazz]
     */
    public fun <T> provide(clazz: Class<T>): ServiceProvider<T>?
}

/**
 * Allows registration with the use of reified types, removing that ugly class
 * parameter.
 */
@JvmSynthetic
public inline fun <reified T> ServicesManager.register(plugin: Any, provider: T): Unit =
    register(plugin, T::class.java, provider)

/**
 * Allows retrieval of providers with the use of reified types, removing that
 * ugly class parameter.
 */
@JvmSynthetic
public inline fun <reified T> ServicesManager.provide(): ServiceProvider<T>? = provide(T::class.java)
