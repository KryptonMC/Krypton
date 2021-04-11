package org.kryptonmc.krypton.api.service

import org.kryptonmc.krypton.api.plugin.Plugin

/**
 * Manager of services
 *
 * @author Callum Seabrook
 */
interface ServicesManager {

    /**
     * Register a new service to this services manager
     *
     * @param plugin the plugin to register the service for
     * @param clazz the class of the service to register
     * @param service the service being provided
     */
    fun <T> register(plugin: Plugin, clazz: Class<T>, service: T)

    /**
     * Retrieve a provider for a given class
     *
     * @param clazz the class of the service
     * @return a [ServiceProvider] containing the service to be provided, or null if
     * there is no service registered with the given class [clazz]
     */
    fun <T> getProvider(clazz: Class<T>): ServiceProvider<T>?
}

/**
 * Allows registration with the use of reified types, removing that
 * ugly class parameter
 */
inline fun <reified T> ServicesManager.register(plugin: Plugin, provider: T) = register(plugin, T::class.java, provider)

/**
 * Allows retrieval of providers with the use of reified types,
 * removing that ugly class parameter
 */
inline fun <reified T> ServicesManager.getProvider() = getProvider(T::class.java)