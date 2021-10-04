/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

import net.kyori.adventure.key.Key
import org.kryptonmc.api.Server
import org.kryptonmc.api.resource.ResourceKey
import java.util.concurrent.CompletableFuture

/**
 * The world manager for this server. Can be used to retrieve loaded worlds,
 * or to load, save and update existing worlds.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface WorldManager {

    /**
     * The server this world manager is bound to.
     */
    @get:JvmName("server")
    public val server: Server

    /**
     * The map of all currently loaded worlds by dimension.
     */
    @get:JvmName("worlds")
    public val worlds: Map<ResourceKey<World>, World>

    /**
     * The default world for this server.
     *
     * What the default world is will be defined by the implementation.
     */
    @get:JvmName("default")
    public val default: World

    /**
     * Gets the loaded world with the given resource [key], or null if there is
     * no world loaded with the given resource [key].
     *
     * @param key the resource key
     * @return the loaded world with the key, or null if not present
     */
    public operator fun get(key: Key): World?

    /**
     * Returns true if there is currently a world loaded with the given [key],
     * or false otherwise.
     *
     * @param key the resource key for the world
     * @return true if there is a world loaded with the given key, false
     * otherwise
     */
    public operator fun contains(key: Key): Boolean

    /**
     * Loads a world by its resource key.
     *
     * @param key the resource key for the world
     * @return a future representing the result of loading the world with the
     * given key
     */
    public fun load(key: Key): CompletableFuture<out World>

    /**
     * Saves the given [world] to disk.
     *
     * @param world the world to save
     */
    public fun save(world: World): CompletableFuture<Unit>
}
