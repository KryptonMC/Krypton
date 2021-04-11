package org.kryptonmc.krypton.api.service

import org.kryptonmc.krypton.api.plugin.Plugin

/**
 * Represents a provider of a service of type [T]
 *
 * Plugins can use these to provide classes to other plugins in a way that
 * allows them to not need to know who they are actually providing the service
 * to (if anyone), which is a neat abstraction layer
 *
 * @author Callum Seabrook
 */
interface ServiceProvider<T> {

    /**
     * The plugin that provided this service
     */
    val plugin: Plugin

    /**
     * The class of the service being provided
     */
    val serviceClass: Class<T>

    /**
     * The service provided
     */
    val service: T
}