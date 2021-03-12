package org.kryptonmc.krypton.api.world

import org.kryptonmc.krypton.api.Server
import org.kryptonmc.krypton.api.registry.NamespacedKey

/**
 * The world manager for this server. Can be used to retrieve loaded worlds,
 * or to load, save and update existing worlds.
 *
 * @author Callum Seabrook
 */
interface WorldManager {

    /**
     * The server this [WorldManager] is bound to
     */
    val server: Server

    /**
     * A map of currently loaded worlds by name.
     */
    val worlds: Map<String, World>

    /**
     * Load a world by its namespaced key
     */
    fun load(name: String): World
}