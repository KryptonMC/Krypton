package org.kryptonmc.krypton.api.plugin

/**
 * The plugin manager.
 *
 * @author Callum Seabrook
 */
interface PluginManager {

    /**
     * The list of currently loaded plugins
     */
    val plugins: Set<Plugin>

    /**
     * If this plugin is present in the list and is enabled, this will
     * return true. Otherwise, it will return false.
     *
     * @param plugin the plugin to check
     * @return if the plugin is loaded and enabled
     */
    fun isInitialized(plugin: Plugin): Boolean
}