package org.kryptonmc.krypton.api.world

import org.kryptonmc.krypton.api.Server

/**
 * The world manager for this server. Can be used to retrieve loaded worlds,
 * or to load, save and update existing worlds.
 */
interface WorldManager {

    /**
     * The server this [WorldManager] is bound to
     */
    val server: Server

    /**
     * The default world for this [Server]
     *
     * What the default world is will be defined by the implementation
     */
    val default: World

    /**
     * A map of currently loaded worlds by name.
     */
    val worlds: Map<String, World>

    /**
     * Load a world by its namespaced key
     *
     * @param name
     * @return the world with the specified name
     * @throws IllegalArgumentException if there is no world folder with
     * the specified name in the current directory
     */
    fun load(name: String): World

    /**
     * Save a world to disk
     *
     * @param world the world to save
     * @throws IllegalArgumentException if the provided world is not of the
     * internal server type (if it is a custom implementation)
     */
    fun save(world: World)

    /**
     * If the specified world [name] is loaded into the server
     *
     * @param name the name of the world
     * @return if there is a world with this [name] loaded
     */
    operator fun contains(name: String): Boolean
}
